package se.vgregion.service.barium;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.json.*;
import se.vgregion.util.Util;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BariumRestClientImpl implements BariumRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BariumRestClientImpl.class);
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

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#connect()
	 */
    @Override
    public boolean connect() throws BariumException {
        return connectInternal();
    }

    public String doGet(String uri) throws BariumException {
        return doRequest("GET", uri, null);
    }

    public String doDelete(String uri) throws BariumException {
        return doRequest("DELETE", uri, null);
    }

    public String doPost(String uri, String requestBody) throws BariumException {
        try {
            return doRequest("POST", uri, requestBody.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // won't happen
            throw new RuntimeException(e);
        }
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#getApplicationInstances()
	 */
    @Override
    public ApplicationInstances getApplicationInstances() throws BariumException {
        String instancesJson = doGet("/Apps/" + applicationId + "/Instances");

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(instancesJson, ApplicationInstances.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public String deleteBariumInstance(String instanceId) throws BariumException {

    	boolean deleteWasSuccessful = false;
    	
        String parameterString = "/Instances" + "/" + instanceId;

        System.out.println("BariumRestClientImpl - deleteBariumInstance - parameterString is: " + parameterString);

        String repyJson = doDelete(parameterString);

        System.out.println("BariumRestClientImpl - getBariumInstance - repyJson is: " + repyJson);

        return repyJson;
    }    

    /* (non-Javadoc)
	 * @see se.vgregion.service.barium.BariumRestClient#getApplicationInstance(String instanceId)
	 */
    @Override
    public BariumInstance getBariumInstance(String instanceId) throws BariumException {

        String parameterString = "/Instances" + "/" + instanceId;

        System.out.println("BariumRestClientImpl - getBariumInstance - parameterString is: " + parameterString);

        String instanceJson = doGet(parameterString);

        System.out.println("BariumRestClientImpl - getBariumInstance - instanceJson is: " + instanceJson);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(instanceJson, BariumInstance.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void uploadFile(String instanceId, String fileName, InputStream inputStream) throws BariumException {
        doPostMultipart("/Instances/" + instanceId + "/Objects", fileName, inputStream);
    }

    /**
     *
     * @param instanceId
     * @param folderName
     * @return the id of the created folder
     * @throws BariumException
     */
    public String createFolder(String instanceId, String folderName) throws BariumException {
        String response = doPost("/Instances/" + instanceId + "/Objects",
                "{objectclass: \"repository.folder\", name: \"" + folderName + "\"}");

        try {
            JSONObject jsonObject = new JSONObject(response);
            return (String) ((JSONObject) jsonObject.getJSONArray("Items").get(0)).get("Id");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String findFolder(String instanceId, String folderName) throws BariumException {
        String response = doGet("/Instances/" + instanceId + "/Objects");

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray data = jsonObject.getJSONArray("Data");
            for (int i = 0; i < data.length(); i++) {
                if (data.get(i) instanceof JSONObject) {
                    JSONObject bariumObject = (JSONObject) data.get(i);
                    if (bariumObject.get("ObjectClass").equals("repository.folder")) {
                        if (folderName.equals(bariumObject.get("Name"))) {
                            return (String) bariumObject.get("Id");
                        }
                    }
                }
            }
            return null;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadFile(String instanceId, String folderName, String fileName, InputStream inputStream) throws BariumException {
        String folderId = findFolder(instanceId, folderName);

        if (folderId == null) {
            folderId = createFolder(instanceId, folderName);
        }
        doPostMultipart("/Objects/" + folderId + "/Objects", fileName, inputStream);
    }

    @Override
    public ObjectEntry getObject(String id) {
        String objectJson = null;
        try {
            objectJson = doGet("/Objects/" + id);
            LOGGER.debug(objectJson);
        } catch (BariumException e) {
            // TODO - we might want to check what kind of error we receive from Barium. (parse json string)
            //LOGGER.error(e.getMessage(), e);
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(objectJson, ObjectEntry.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String doPostMultipart(String endpoint, String fileName, InputStream inputStream) throws BariumException {
        DefaultHttpClient httpClient = new DefaultHttpClient();

//        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("127.0.0.1", 8888));

        HttpPost httpPost = new HttpPost(this.apiLocation + endpoint);

        if (ticket != null) {
            httpPost.setHeader("ticket", ticket);
        } else {
            this.connect();
            httpPost.setHeader("ticket", ticket);
        }

        try {
            ContentBody contentBody = new InputStreamBody(inputStream, fileName);

            // Must use HttpMultipartMode.BROWSER_COMPATIBLE in order to use UTF-8 instead of US_ASCII to handle special
            // characters.
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null,
                    Charset.forName("UTF-8")) {

                // Due to a bug in Barium we must not append the charset directive to the content-type header since
                // nothing is created in Barium if doing so.
                // todo Try skipping this when we use Barium Live?
                @Override
                protected String generateContentType(final String boundary, final Charset charset) {
                    StringBuilder buffer = new StringBuilder();
                    buffer.append("multipart/form-data; boundary=");
                    buffer.append(boundary);
                    // Comment out this
                    /*if (charset != null) {
                        buffer.append("; charset=");
                        buffer.append(charset.name());
                    }*/
                    return buffer.toString();
                }
            };

            multipartEntity.addPart("Data", contentBody);

            httpPost.setEntity(multipartEntity);

            HttpResponse response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new BariumException("Failed to post multipart. Reason: " + response.getStatusLine().getReasonPhrase());
            }

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

    public InputStream doGetFileStream(String objectId) throws BariumException {
        URL url = null;
        try {
            url = new URL(this.apiLocation + "/Objects/" + objectId + "/File");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (ticket != null) {
                conn.setRequestProperty("ticket", ticket);
            } else {
                this.connect();
                conn.setRequestProperty("ticket", ticket);
            }
            conn.setRequestMethod("GET");
            conn.setRequestProperty("charset", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            return conn.getInputStream();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new BariumException("Filed to get filestream. ", e);
        }
    }

    @Override
    public String updateField(String instanceId, String field, String value) throws BariumException {
        try {
            String formId = getFormId(instanceId);
            return doPost("/Objects/" + formId + "/Fields", field + "=" + URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // won't happen
            throw new RuntimeException(e);
        }
    }

    private String getFormId(String instanceId) throws BariumException {
        String json = doGet("/Instances/" + instanceId + "/Objects/IDE/");

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            return (String) jsonObject.get("Id");
        } catch (JSONException e) {
            throw new BariumException(e);
        }
    }

    private String doRequest(String method, String uri, byte[] data) throws BariumException {

        URL url;
        HttpURLConnection conn = null;
        String response = null;

        InputStream inputStream = null;
        BufferedInputStream bis = null;

        OutputStream outputStream = null;
        BufferedOutputStream bos = null;
        try {
            if (method.equalsIgnoreCase("POST")) {
                if (uri != null) {
                    url = new URL(this.apiLocation + uri);
                } else {
                    throw new RuntimeException("For POST requests a uri is expected.");
                }
            } else {
                url = new URL(this.apiLocation + uri);
            }

//            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(8888));
            conn = (HttpURLConnection) url.openConnection(/*proxy*/);
            if (ticket != null) {
                conn.setRequestProperty("ticket", ticket);
            } else if (!uri.contains("authenticate")) {
                this.connect();
                conn.setRequestProperty("ticket", ticket);
            }
            conn.setRequestMethod(method);
            conn.setRequestProperty("charset", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            if (method.equalsIgnoreCase("POST")) {
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                outputStream = conn.getOutputStream();
                bos = new BufferedOutputStream(outputStream);
                bos.write(data);
                bos.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 401) {
                ticket = null; // We weren't authorized, possibly due to an old ticket.
            }

            inputStream = conn.getInputStream();
            bis = new BufferedInputStream(inputStream);

            response = toString(bis);
        } catch (IOException e) {
            inputStream = conn.getErrorStream();
            bis = new BufferedInputStream(inputStream);

            response = toString(bis);
            throw new BariumException(response, e);
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
     * <p/>
     * sa@param tasks tasks
     *
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

    /* (non-Javadoc)
	 * @see se.vgregion.service.barium.BariumRestClient#updateInstance(java.lang.String, java.lang.String)
	 */
    @Override
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

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /* (non-Javadoc)
	 * @see se.vgregion.service.barium.BariumRestClient#getInstanceObjects(se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstance)
	 */
    @Override
    public Objects getInstanceObjects(String instanceId) throws BariumException {
        String objectsJson = doGet("/Instances/" + instanceId + "/Objects");

        return objectJsonToObjects(objectsJson);
    }

    @Override
    public Objects getObjectObjects(String objectId) throws BariumException {
        String objectsJson = doGet("/Objects/" + objectId + "/Objects");

        return objectJsonToObjects(objectsJson);
    }

    private Objects objectJsonToObjects(String objectsJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(objectsJson, Objects.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ObjectField> getIdeaObjectFields(ApplicationInstance instance) {
        String objectJson = null;
        try {
            objectJson = doGet("/instances/" + instance.getId() + "/Objects/IDE/Fields");
            LOGGER.info(objectJson);
        } catch (BariumException e) {

            // TODO - we might want to check what kind of error we receive from Barium. (parse json string)
            //LOGGER.error(e.getMessage(), e);

            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(objectJson, TypeFactory.defaultInstance().constructCollectionType(List.class,
                    ObjectField.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getIdeaState(String instanceId) {
        String objectJson;
        try {
            objectJson = doGet("/instances/" + instanceId + "/Objects/IDE/");
            LOGGER.info(objectJson);
        } catch (BariumException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectEntry objectEntry = mapper.readValue(objectJson, ObjectEntry.class);
            return objectEntry.getState();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ObjectField> getIdeaObjectFields(String instanceId) {
        String objectJson = null;
        try {
            objectJson = doGet("/instances/" + instanceId + "/Objects/IDE/Fields");
            LOGGER.info(objectJson);
        } catch (BariumException e) {

            // TODO - we might want to check what kind of error we receive from Barium. (parse json string)
            LOGGER.error(e.getMessage(), e);

            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(objectJson, TypeFactory.defaultInstance().constructCollectionType(List.class,
                    ObjectField.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /* (non-Javadoc)
	 * @see se.vgregion.service.barium.BariumRestClient#createIdeaInstance(se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields)
	 */
    @Override
    public String createIdeaInstance(IdeaObjectFields ideaObjectFields) {

        StringBuilder sb = new StringBuilder();

        sb.append("message=START");
        sb.append("&template=565d4c81-4baa-451b-aacc-5f7ae295bfaf");
        if (ideaObjectFields.getBehov() != null) {
            sb.append("&behov=").append(ideaObjectFields.getBehov());
        }
        if (ideaObjectFields.getEpost() != null) {
            sb.append("&e-post=").append(ideaObjectFields.getEpost());
        }
        if (ideaObjectFields.getFodelsear() != null) {
            sb.append("&fodelsear=").append(ideaObjectFields.getFodelsear());
        }
        if (ideaObjectFields.getHsaIdKivEnhet() != null) {
            sb.append("&HSA-ID.KIVenhet=").append(ideaObjectFields.getHsaIdKivEnhet());
        }
        if (ideaObjectFields.getIde() != null) {
            sb.append("&ide=").append(ideaObjectFields.getIde());
        }
        if (ideaObjectFields.getKommavidare() != null) {
            sb.append("&kommavidare=").append(ideaObjectFields.getKommavidare());
        }
        if (ideaObjectFields.getKomplnamn() != null) {
            sb.append("&komplnamn=").append(ideaObjectFields.getKomplnamn());
        }
        if (ideaObjectFields.getEpost() != null) {
            sb.append("&VGR-ID.email=").append(ideaObjectFields.getEpost());
        }
        if (ideaObjectFields.getInstanceName() != null) {
            sb.append("&instance.name=").append(ideaObjectFields.getInstanceName());
        }
        if (ideaObjectFields.getPrio1kommentar() != null) {
            sb.append("&prio1kommentar=").append(ideaObjectFields.getPrio1kommentar());
        }
        if (ideaObjectFields.getPrio2kommentar() != null) {
            sb.append("&prio2kommentar=").append(ideaObjectFields.getPrio2kommentar());
        }
        if (ideaObjectFields.getSiteLank() != null) {
            sb.append("&siteLank=").append(ideaObjectFields.getSiteLank());
        }
        if (ideaObjectFields.getTestat() != null) {
            sb.append("&testat=").append(ideaObjectFields.getTestat());
        }
        if (ideaObjectFields.getTelefonnummer() != null) {
            sb.append("&VGR-ID.hsapublictelephonenumber=").append(ideaObjectFields.getTelefonnummer());
        }
        if (ideaObjectFields.getTelefonnummerMobil() != null) {
            sb.append("&VGR-ID.mobiletelephonenumber=").append(ideaObjectFields.getTelefonnummerMobil());
        }
        if (ideaObjectFields.getVgrId() != null) {
            sb.append("&VGR-ID=").append(ideaObjectFields.getVgrId());
        }
        if (ideaObjectFields.getVgrIdFullname() != null) {
            sb.append("&VGR-ID.fullname=").append(ideaObjectFields.getVgrIdFullname());
        }
        if (ideaObjectFields.getVgrIdHsaPostalAdress() != null) {
            sb.append("&VGR-ID.hsapostaladress=").append(ideaObjectFields.getVgrIdHsaPostalAdress());
        }
        if (ideaObjectFields.getVgrIdTitel() != null) {
            sb.append("&VGR-ID.titel=").append(ideaObjectFields.getVgrIdTitel());
        }

        String replyJson = createInstance(sb.toString());

        LOGGER.info("createIdeaInstance - replyJson is: " + replyJson);

        return replyJson;
    }
}


