package se.vgregion.service.barium;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.*;
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
import se.vgregion.portal.innovationsslussen.domain.*;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstance;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstances;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectField;
import se.vgregion.portal.innovationsslussen.domain.json.Objects;
import se.vgregion.util.Util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class MockBariumRestClientImpl implements BariumRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockBariumRestClientImpl.class);
    
    private static final String APPLICATION_INSTANCES = "{\"TotalCount\":8,\"Data\":[{\"Id\":\"53991770-9f86-49ac-962a-5f4e4db31eef\",\"ReferenceId\":null,\"Name\":\"TestInstans\",\"Description\":\"\",\"Status\":\"Active\",\"State\":null,\"StartDate\":\"2013-05-24T13:11:28Z\",\"StartedBy\":\"Inovation Slussen\",\"StartedByUserId\":\"ba9c57f1-065c-46ed-b5f9-6fb50b0ab3a9\",\"CompletedDate\":null,\"ProcessId\":null,\"DomainId\":null},{\"Id\":\"2c75baaf-6a60-476e-ad2d-724f965f7817\",\"ReferenceId\":null,\"Name\":\"TestInstans\",\"Description\":\"\",\"Status\":\"Active\",\"State\":null,\"StartDate\":\"2013-05-24T12:42:53Z\",\"StartedBy\":\"Inovation Slussen\",\"StartedByUserId\":\"ba9c57f1-065c-46ed-b5f9-6fb50b0ab3a9\",\"CompletedDate\":null,\"ProcessId\":null,\"DomainId\":null},{\"Id\":\"329ffbcc-5a3d-457e-87c2-fb42cf630346\",\"ReferenceId\":null,\"Name\":\"null\",\"Description\":\"\",\"Status\":\"Active\",\"State\":null,\"StartDate\":\"2013-05-24T12:25:44Z\",\"StartedBy\":\"Inovation Slussen\",\"StartedByUserId\":\"ba9c57f1-065c-46ed-b5f9-6fb50b0ab3a9\",\"CompletedDate\":null,\"ProcessId\":null,\"DomainId\":null},{\"Id\":\"f3d2aca1-acb0-4853-b9a5-9e743d77b6a0\",\"ReferenceId\":null,\"Name\":\"patriktest2\",\"Description\":\"\",\"Status\":\"Active\",\"State\":null,\"StartDate\":\"2013-05-22T11:27:58Z\",\"StartedBy\":\"Inovation Slussen\",\"StartedByUserId\":\"ba9c57f1-065c-46ed-b5f9-6fb50b0ab3a9\",\"CompletedDate\":null,\"ProcessId\":null,\"DomainId\":null},{\"Id\":\"7730f873-74b1-44fd-8d1f-8cc21f9c8b4e\",\"ReferenceId\":null,\"Name\":\"patriktest\",\"Description\":\"\",\"Status\":\"Active\",\"State\":null,\"StartDate\":\"2013-05-22T11:12:34Z\",\"StartedBy\":\"Inovation Slussen\",\"StartedByUserId\":\"ba9c57f1-065c-46ed-b5f9-6fb50b0ab3a9\",\"CompletedDate\":null,\"ProcessId\":null,\"DomainId\":null},{\"Id\":\"0ab5f6df-d441-41a6-aa62-2157d1d3fe75\",\"ReferenceId\":null,\"Name\":\"Inovation Slussen (2013-05-22 11:20:05)\",\"Description\":null,\"Status\":\"Active\",\"State\":null,\"StartDate\":\"2013-05-22T09:20:05Z\",\"StartedBy\":\"Inovation Slussen\",\"StartedByUserId\":\"ba9c57f1-065c-46ed-b5f9-6fb50b0ab3a9\",\"CompletedDate\":null,\"ProcessId\":null,\"DomainId\":null},{\"Id\":\"d6139087-d722-4200-9691-91a5d5697905\",\"ReferenceId\":null,\"Name\":\"Test Susanne\",\"Description\":\"\",\"Status\":\"Active\",\"State\":null,\"StartDate\":\"2013-05-20T07:40:57Z\",\"StartedBy\":\"Susanne Lindqvist\",\"StartedByUserId\":\"87b13dda-6ad5-4a81-b5f8-ac33e906d0c5\",\"CompletedDate\":null,\"ProcessId\":null,\"DomainId\":null},{\"Id\":\"ef7ea30b-5089-428f-87e8-f10acd285cb6\",\"ReferenceId\":null,\"Name\":\"NY IDE\",\"Description\":\"\",\"Status\":\"Active\",\"State\":null,\"StartDate\":\"2013-05-03T08:50:38Z\",\"StartedBy\":\"Kristofer Johansson\",\"StartedByUserId\":\"dffc2654-cb10-433d-aeb8-9d87f6a05b96\",\"CompletedDate\":null,\"ProcessId\":null,\"DomainId\":null}]}";

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
    	return true;
    }

    public boolean connect() throws BariumException {
        return true;
    }

    /*
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
    */

    public ApplicationInstances getApplicationInstances() throws BariumException {
        
    	String instancesJson = APPLICATION_INSTANCES;

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

    public String createInstance(String data) {
    	return "{\"success\":true,\"InstanceId\":\"42be3964-1ada-40c9-ab96-c3eca1cd2515\"}";
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

    public Objects getInstanceObjects(ApplicationInstance instance) throws BariumException {
        String objectsJson = "{\"TotalCount\":0,\"Data\":[]}";

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(objectsJson, Objects.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ObjectField> getIdeaObjectFields(ApplicationInstance instance) {
        String objectJson = null;
            objectJson = "[{\"Id\":\"behov\",\"Name\":\"Behov eller problem som din ide löser\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"att det tidigare inte fanns några ideer\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"beslutsforslagsskommentar\",\"Name\":\"Kommentar\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"Vi föreslår att detta godtages\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"bilaga\",\"Name\":\"Bifoga gärna en skiss eller andra dokument som du tror kan vara till nytta för att beskriva din ide\",\"DataType\":\"System.Binary\",\"FieldType\":\"fileuploadfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"e-post\",\"Name\":\"E-post\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"christofer.johansson@vgregion.se\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"fodelsear\",\"Name\":\"Födelseår\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"godkprio1\",\"Name\":\"godkprio1\",\"DataType\":\"System.Bool\",\"FieldType\":\"checkbox\",\"Value\":\"off\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"godkprio2\",\"Name\":\"godkprio2\",\"DataType\":\"System.Bool\",\"FieldType\":\"checkbox\",\"Value\":\"off\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"HSA-ID.KIVenhet\",\"Name\":\"Förvaltning\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"ide\",\"Name\":\"Beskriv din ide kortfattat\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"En ny ide\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"Idetranportorenskommentar\",\"Name\":\"Kommentar\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"Kommentar\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"instance.name\",\"Name\":\"* Projekttitel\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"NY IDE\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":false,\"Index\":9999},{\"Id\":\"kommavidare\",\"Name\":\"Hur tror du att Innovationsslussen kan hjälpa dig att komma vidare med din ide\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"koordinatornsskommentar\",\"Name\":\"Kommentar\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"prio1kommentar\",\"Name\":\"Kommentar\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"prio2kommentar\",\"Name\":\"Kommentar\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"prioriteringsradsmote\",\"Name\":\"Prioriteringsrådsmöte\",\"DataType\":\"System.DateTime\",\"FieldType\":\"datefield\",\"Value\":\"2013-05-23T22:00:00Z\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"telefonnummer\",\"Name\":\"Telefonnummer\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"VGR-ID.fullname\",\"Name\":\"Namn\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"Kristofer Johansson\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"VGR-ID.hsapostaladress\",\"Name\":\"Postadress\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"VGR-ID.titel\",\"Name\":\"Yrkesroll\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"Systemutvecklare    \",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999}]";
            LOGGER.info(objectJson);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(objectJson, TypeFactory.defaultInstance().constructCollectionType(List.class,
                    ObjectField.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createIdeaInstance(IdeaObjectFields ideaObjectFields) {

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
        if (ideaObjectFields.getEpost() != null) {
            sb.append("&e-post=").append(ideaObjectFields.getEpost());
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
        if (ideaObjectFields.getTelefonnummer() != null) {
            sb.append("&telefonnummer=").append(ideaObjectFields.getTelefonnummer());
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

        System.out.println(replyJson);

    }

	@Override
	public boolean updateInstance(String values, String objectId) {
		// TODO Auto-generated method stub
		return false;
	}
}


