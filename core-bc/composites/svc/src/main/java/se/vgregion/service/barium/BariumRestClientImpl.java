/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.service.barium;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.BeanMap;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
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
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstance;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstances;
import se.vgregion.portal.innovationsslussen.domain.json.BariumInstance;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectEntry;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectField;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectFieldsHolder;
import se.vgregion.portal.innovationsslussen.domain.json.Objects;
import se.vgregion.util.Util;

/**
 * The Class BariumRestClientImpl.
 */
public class BariumRestClientImpl implements BariumRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BariumRestClientImpl.class);

    private String apiLocation;

    private String apiKey;
    private String username;
    private String password;
    private String applicationId;
    private String ticket;
    private boolean hasValidTicket = false;

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);


    /**
     * Instantiates a new barium rest client impl.
     */
    public BariumRestClientImpl() {

    }

    /**
     * Instantiates a new barium rest client impl.
     *
     * @param apiLocation   the api location
     * @param apiKey        the api key
     * @param username      the username
     * @param password      the password
     * @param applicationId the application id
     * @throws BariumException the barium exception
     */
    public BariumRestClientImpl(String apiLocation, String apiKey, String username, String password,
                                String applicationId)
            throws BariumException {

        this.apiLocation = apiLocation;
        this.apiKey = apiKey;
        this.username = username;
        this.password = password;
        this.applicationId = applicationId;

    }

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#connect()
     */
    @Override
    public boolean connect() throws BariumException {
        return connectInternal();
    }

    /**
     * Do get.
     *
     * @param uri the uri
     * @return the string
     * @throws BariumException the barium exception
     */
    public String doGet(String uri) throws BariumException {
        return doRequest("GET", uri, null, 1);
    }

    /**
     * Do delete.
     *
     * @param uri the uri
     * @return the string
     * @throws BariumException the barium exception
     */
    public String doDelete(String uri) throws BariumException {
        return doRequest("DELETE", uri, null, 1);
    }

    /**
     * Do post.
     *
     * @param uri         the uri
     * @param requestBody the request body
     * @return the string
     * @throws BariumException the barium exception
     */
    public String doPost(String uri, String requestBody) throws BariumException {
        try {
            return doRequest("POST", uri, requestBody.getBytes("UTF-8"), 1);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#deleteBariumInstance(java.lang.String)
     */
    @Override
    public String deleteBariumInstance(String instanceId) throws BariumException {

        String parameterString = "/Instances" + "/" + instanceId;
        String repyJson = doDelete(parameterString);

        return repyJson;
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#getApplicationInstance(String instanceId)
     */
    @Override
    public BariumInstance getBariumInstance(String instanceId) throws BariumException {

        String parameterString = "/Instances" + "/" + instanceId;

        String instanceJson = doGet(parameterString);

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

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#uploadFile(
     * java.lang.String, java.lang.String, java.io.InputStream)
     */
    @Override
    public void uploadFile(String instanceId, String fileName, InputStream inputStream) throws BariumException {
        doPostMultipart("/Instances/" + instanceId + "/Objects", fileName, inputStream);
    }

    /**
     * Creates the folder.
     *
     * @param instanceId the instance id
     * @param folderName the folder name
     * @return the string
     * @throws BariumException the barium exception
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

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#findFolder(java.lang.String, java.lang.String)
     */
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

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#uploadFile(
     * java.lang.String, java.lang.String, java.lang.String, java.io.InputStream)
     */
    @Override
    public String uploadFile(String instanceId, String folderName, String fileName, InputStream inputStream)
            throws BariumException {
        String folderId = findFolder(instanceId, folderName);

        if (folderId == null) {
            folderId = createFolder(instanceId, folderName);
        }
        return doPostMultipart("/Objects/" + folderId + "/Objects", fileName, inputStream);

    }

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#getObject(java.lang.String)
     */
    @Override
    public ObjectEntry getObject(String id) throws BariumException {
        String objectJson = null;
        objectJson = doGet("/Objects/" + id);
        LOGGER.debug(objectJson);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(objectJson, ObjectEntry.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Do post multipart.
     *
     * @param endpoint    the endpoint
     * @param fileName    the file name
     * @param inputStream the input stream
     * @return the string
     * @throws BariumException the barium exception
     */
    String doPostMultipart(String endpoint, String fileName, InputStream inputStream) throws BariumException {
        DefaultHttpClient httpClient = new DefaultHttpClient();

        //httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("140.166.209.205", 8888));

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

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new BariumException("Failed to post multipart. Reason: "
                        + response.getStatusLine().getReasonPhrase());
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

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#doGetFileStream(java.lang.String)
     */
    @Override
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

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#updateField(
     * java.lang.String, java.lang.String, java.lang.String)
     */
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

    private String doRequest(String method, String uri, byte[] data, int methodCallCount) throws BariumException {

        LOGGER.debug("doRequest: apiLocation: " + apiLocation + ", method: " + method + ", uri: " + uri + ", data: "
                + (data != null ? new String(data) : ""));

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
            conn.setConnectTimeout(120000);
            conn.setReadTimeout(120000);
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
            if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
                ticket = null; // We weren't authorized, possibly due to an old ticket.
                this.connect();
                if (methodCallCount <= 3) {
                    return doRequest(method, uri, data, ++methodCallCount);
                } else {
                    String msg = "Error - Unable to authenticate to Barium: " + uri;
                    readResponseAndThrowBariumException(conn, msg);
                }
            } else if (responseCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                if (methodCallCount <= 3) {
                    return doRequest(method, uri, data, ++methodCallCount);
                } else {
                    String msg = "Error - Internal Server Error - From Barium - for idea: " + uri;
                    readResponseAndThrowBariumException(conn, msg);
                }
            }

            inputStream = conn.getInputStream();
            bis = new BufferedInputStream(inputStream);

            response = toString(bis);
        } catch (IOException e) {

            if (conn != null) {
                inputStream = conn.getErrorStream();
                bis = new BufferedInputStream(inputStream);
                try {
                    response = toString(bis);
                } catch (Exception e2) {
                    LOGGER.error(e2.getMessage(), e2);
                }
                throw new BariumException(response, e);
            }
        } finally {
            Util.closeClosables(bis, inputStream, bos, outputStream);
        }
        LOGGER.debug("Response: " + response);
        return response;
    }

    private void readResponseAndThrowBariumException(HttpURLConnection conn, String msg) throws BariumException {
        LOGGER.error(msg);
        if (conn != null) {
            BufferedInputStream bis = null;
            InputStream inputStream = null;
            try {
                inputStream = conn.getErrorStream();
                bis = new BufferedInputStream(inputStream);
                String errorBody = toString(bis);
                throw new BariumException(errorBody);
            } finally {
                Util.closeClosables(bis, inputStream);
            }
        } else {
            throw new BariumException(msg);
        }
    }

    /**
     * To string.
     *
     * @param inputStream the input stream
     * @return the string
     * @throws BariumException the barium exception
     */
    private String toString(InputStream inputStream) throws BariumException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();;
        try {
            final int byteSize = 1024;
            byte[] buf = new byte[byteSize];
            int n;
            while ((n = inputStream.read(buf)) != -1) {
                baos.write(buf, 0, n);
            }
            return baos.toString("UTF-8");
        } catch (IOException e) {
            throw new BariumException("Error parsing response from server when authenticating (" + baos.toString() + ")",
                    e);
        }
    }

    private boolean connectInternal() throws BariumException {

        if (this.apiLocation.isEmpty()) {
            throw new BariumException("Not ready to connect, no URL specified.");
        }
        if (this.apiKey.isEmpty()) {
            throw new BariumException("Not ready to connect, no API key specified.");
        }
        if (this.username.isEmpty()) {
            throw new BariumException("Not ready to connect, no username specified.");
        }
        if (this.password.isEmpty()) {
            throw new BariumException("Not ready to connect, no password specified.");
        }

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

    /**
     * Delete instance.
     *
     * @param instanceId the instance id
     * @return true, if successful
     */
    public boolean deleteInstance(String instanceId) {

        try {
            doDelete("/Instances/" + instanceId);
        } catch (BariumException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Creates the instance.
     *
     * @param data the data
     * @return the string
     */
    public String createInstance(String data) {

        try {
            return doPost("/apps/" + applicationId, data);
        } catch (BariumException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * Shutdown the connection to Barium.
     */
    @Override
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
     * @see se.vgregion.service.barium.BariumRestClient#getInstanceObjects(java.lang.String)
     */
    @Override
    public Objects getInstanceObjects(String instanceId) throws BariumException {
        String objectsJson = doGet("/Instances/" + instanceId + "/Objects");

        return objectJsonToObjects(objectsJson);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#getObjectObjects(java.lang.String)
     */
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

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#getIdeaObjectFields(
     * se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstance)
     */
    @Override
    public List<ObjectField> getIdeaObjectFields(ApplicationInstance instance) {
        String objectJson = null;
        try {
            objectJson = doGet("/instances/" + instance.getId() + "/Objects/IDE/Fields");
            LOGGER.debug(objectJson);
        } catch (BariumException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectFieldsHolder holder = mapper.readValue(objectJson, TypeFactory.defaultInstance().constructType(
                    ObjectFieldsHolder.class));

            return holder.getData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#getIdeaState(java.lang.String)
     */
    @Override
    public String getIdeaState(String instanceId) {
        String objectJson;
        try {
            objectJson = doGet("/instances/" + instanceId + "/Objects/IDE/");
            LOGGER.debug(objectJson);
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

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#getIdeaObjectFields(java.lang.String)
     */
    @Override
    public List<ObjectField> getIdeaObjectFields(String instanceId) {
        String objectJson = null;
        try {
            objectJson = doGet("/instances/" + instanceId + "/Objects/IDE/Fields");
            LOGGER.debug(objectJson);
        } catch (BariumException e) {

            // TODO - we might want to check what kind of error we receive from Barium. (parse json string)
            LOGGER.error(e.getMessage(), e);

            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectFieldsHolder holder = mapper.readValue(objectJson, TypeFactory.defaultInstance().constructType(
                    ObjectFieldsHolder.class));

            return holder.getData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * To url.
     *
     * @param ideaObjectFields the idea object fields
     * @return the string
     */
    String toUrl(IdeaObjectFields ideaObjectFields) {
        StringBuilder sb = new StringBuilder();

        sb.append("message=START");
        sb.append("&template=565d4c81-4baa-451b-aacc-5f7ae295bfaf");

        BeanMap bm = new BeanMap(ideaObjectFields);
        for (Object entryObj : bm.entrySet()) {

            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) entryObj;

            if ("class".equals(entry.getKey())) {
                continue;
            }
            Object value = entry.getValue();
            if (value != null) {
                String name = IdeaObjectFields.SPECIAL_FIELD_MAPPINGS_REVERSE.get(entry.getKey());
                if (name == null) {
                    name = entry.getKey();
                }
                sb.append("&");
                sb.append(name);
                sb.append("=");
                try {
                    value = URLEncoder.encode(value + "", "utf-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                sb.append(value);
            }
        }
        return sb.toString();
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#createIdeaInstance(
     * se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields)
     */
    @Override
    public String createIdeaInstance(IdeaObjectFields ideaObjectFields) {
        String replyJson = createInstance(toUrl(ideaObjectFields));
        LOGGER.info("createIdeaInstance - replyJson is: " + replyJson);
        return replyJson;
    }
}


