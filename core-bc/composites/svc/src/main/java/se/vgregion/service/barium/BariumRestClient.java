package se.vgregion.service.barium;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.util.Util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class BariumRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BariumRestClient.class);
//    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888));

    private String apiLocation;

    private String apiKey;
    private String username;
    private String password;
    private String applicationId;
    private String ticket;
    private boolean hasValidTicket = false;
    private int retryAttempts = 0;

    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    public boolean connect(String apiLocation, String apiKey, String username, String password, String applicationId)
            throws BariumException {

        this.apiLocation = apiLocation;
        this.apiKey = apiKey;
        this.username = username;
        this.password = password;
        this.applicationId = applicationId;

        return connectInternal();
    }

    public boolean connect() throws BariumException {
        return connectInternal();
    }

    public String doGet(String parameters) throws BariumException {
        return doRequest(parameters, "GET", null, null);
    }

    public String doDelete(String parameters) throws BariumException {
        return doRequest(parameters, "DELETE", null, null);
    }

    public String doPost(String endpoint, String parameters) throws BariumException {
        try {
            return doRequest(null, "POST", endpoint, parameters.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // won't happen
            throw new RuntimeException(e);
        }
    }

    public String doPostMultipart(String endpoint, byte[] bytes) throws BariumException {
        DefaultHttpClient httpClient = new DefaultHttpClient();

        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("127.0.0.1", 8888));

        HttpPost httpPost = new HttpPost(this.apiLocation + endpoint);

        if (ticket != null) {
            httpPost.setHeader("ticket", ticket);
        } else {
            this.connect();
            httpPost.setHeader("ticket", ticket);
        }

        try {
            AbstractContentBody contentBody = new InputStreamBody(new ByteArrayInputStream(bytes), "filnamnet4.txt");//new ByteArrayBody("ett filinnehåll".getBytes("UTF-8"), "filnamnet");

            MultipartEntity multipartEntity = new MultipartEntity();


//            multipartEntity.addPart("field", new StringBody("fileuploadfield"));
//            multipartEntity.addPart("field", new StringBody("{name: \"fileuploadfield\", objectclass: \"repository.file\"}"));

            multipartEntity.addPart("part1", contentBody);

            httpPost.setEntity(multipartEntity);

            HttpResponse response = httpClient.execute(httpPost);

            InputStream content = response.getEntity().getContent();

            return toString(content);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String doRequest(String parameters, String method, String endpoint, byte[] data) throws BariumException {

        URL url;
        HttpURLConnection conn = null;
        String response = null;

        InputStream inputStream = null;
        BufferedInputStream bis = null;

        OutputStream outputStream = null;
        BufferedOutputStream bos = null;
        try {
            if (method.equalsIgnoreCase("POST")) {
                if (endpoint != null) {
                    url = new URL(this.apiLocation + endpoint);
                } else {
                    throw new RuntimeException("For POST requests an endpoint is expected.");
                }
            } else {
                url = new URL(this.apiLocation + parameters);
            }

            conn = (HttpURLConnection) url.openConnection(/*proxy*/);
            if (ticket != null) {
                conn.setRequestProperty("ticket", ticket);
            } else if (!(endpoint + endpoint).contains("authenticate")) {
                this.connect();
                conn.setRequestProperty("ticket", ticket);
            }
            conn.setRequestMethod(method);
            conn.setRequestProperty("charset", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
//            conn.setRequestProperty("Connection", "close");
//            conn.setSSLSocketFactory(sslContext.getSocketFactory());

            if (method.equalsIgnoreCase("POST")) {
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                outputStream = conn.getOutputStream();
                bos = new BufferedOutputStream(outputStream);
                bos.write(data);
                bos.flush();
            }

            inputStream = conn.getInputStream();
            bis = new BufferedInputStream(inputStream);

            response = toString(bis);

            // Reset
//            retryAttempts = 0;
        } catch (IOException e) {
            if (retryAttempts++ < 5) {
                LOGGER.warn("Unable to connect, retrying connect to get a new ticket. (Attempt " + retryAttempts + ")",
                        e);
                this.connect();
                return doRequest(parameters, method, endpoint, data);
            } else {
                throw new BariumException("Error connecting to API, please check server is up and api is available ("
                        + this.apiLocation + ")", e);
            }
        } finally {
            Util.closeClosables(bis, inputStream, bos, outputStream);
        }
        return response;
    }

    private String toString(InputStream inputStream) throws BariumException {
        StringBuilder sb = new StringBuilder();
        try {
            byte[] buf = new byte[1024];
            int n;
            while ((n = inputStream.read(buf)) != -1) {
                sb.append(new String(buf, 0, n, "UTF-8"));
            }
        } catch (IOException e) {
            throw new BariumException("Error parsing response from server when authenticating (" + sb.toString() + ")",
                    e);
        }
        return sb.toString();
    }

    private boolean connectInternal() throws BariumException {

        if (this.apiLocation == "") throw new BariumException("Not ready to connect, no URL specified.");
        if (this.apiKey == "") throw new BariumException("Not ready to connect, no API key specified.");
        if (this.username == "") throw new BariumException("Not ready to connect, no username specified.");
        if (this.password == "") throw new BariumException("Not ready to connect, no password specified.");

        String parameters = "username=" + this.username + "&password=" + this.password + "&apiKey=" + this.apiKey;
        String response = doPost("/authenticate", parameters);

        if (response.indexOf("failed") == -1) {
            this.ticket = response;
            this.hasValidTicket = true;
        } else {
            throw new BariumException("Unable to get ticket from server, please check authentication details. Response:"
                    + " \n\r" + response);
        }

        return hasValidTicket;
    }

    /**
     * This method returns pretty much instantaneously. It just submits tasks which get processed in separate threads.
     *
     * sa@param tasks tasks
     * @return mapping of taskIds with future objectIds
     */
    /*private Map<String, Future<String>> getObjectIdsFuture(HashMap<String, HashMap<String, String>> tasks) {
        Map<String, Future<String>> taskIdObjectId = new HashMap<String, Future<String>>();
        for (final String taskId : tasks.keySet()) {
            Future<String> objectIdFuture = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return getObjectIdFromTask(taskId);
                }
            });
            taskIdObjectId.put(taskId, objectIdFuture);
        }
        return taskIdObjectId;
    }*/

    private HashMap<String, HashMap<String, String>> getTaskIds() throws BariumException {

        HashMap<String, HashMap<String, String>> tasks = new HashMap<String, HashMap<String, String>>();
        String tasksJson = doGet("/Tasks");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(tasksJson);

            JSONArray data = jsonObject.getJSONArray("Data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject instance = (JSONObject) data.get(i);
                HashMap<String, String> taskInfo = new HashMap<String, String>();
                taskInfo.put("instanceId", instance.getString("InstanceId"));
                taskInfo.put("createdDate", instance.getString("CreatedDate"));
                tasks.put(instance.getString("Id"), taskInfo);
            }

            return tasks;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateInstance(String values, String objectId) {
        try {
            doPost("/Objects/" + objectId + "/Fields", values);
        } catch (BariumException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public boolean deleteInstance(String instanceId) {

        try {
            doDelete("/Instances/" + instanceId);
        } catch (BariumException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public String createInstance(String data) {

        try {
            return doPost("/apps/" + applicationId, data);
        } catch (BariumException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "";
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public String getApiLocation() {
        return apiLocation;
    }

    public void setApiLocation(String apiLocation) {
        this.apiLocation = apiLocation;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}


