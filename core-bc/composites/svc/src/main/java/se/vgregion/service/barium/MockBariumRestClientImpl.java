package se.vgregion.service.barium;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
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
import se.vgregion.portal.innovationsslussen.domain.json.Objects;

public class MockBariumRestClientImpl implements BariumRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockBariumRestClientImpl.class.getName());


    private static final String RESPONSE_GET_APPLICATION_INSTANCES = "{\"TotalCount\":8,\"Data\":[{\"Id\":\"53991770"
            + "-9f86-49ac-962a-5f4e4db31eef\",\"ReferenceId\":null,\"Name\":\"TestInstans\",\"Description\":\"\",\""
            + "Status\":\"Active\",\"State\":null,\"StartDate\":\"2013-05-24T13:11:28Z\",\"StartedBy\":\"Inovation"
            + " Slussen\",\"StartedByUserId\":\"ba9c57f1-065c-46ed-b5f9-6fb50b0ab3a9\",\"CompletedDate\":null,\""
            + "ProcessId\":null,\"DomainId\":null},{\"Id\":\"2c75baaf-6a60-476e-ad2d-724f965f7817\",\"ReferenceId\""
            + ":null,\"Name\":\"TestInstans\",\"Description\":\"\",\"Status\":\"Active\",\"State\":null,\"StartDate"
            + "\":\"2013-05-24T12:42:53Z\",\"StartedBy\":\"Inovation Slussen\",\"StartedByUserId\":\"ba9c57f1"
            + "-065c-46ed-b5f9-6fb50b0ab3a9\",\"CompletedDate\":null,\"ProcessId\":null,\"DomainId\":null},"
            + "{\"Id\":\"329ffbcc-5a3d-457e-87c2-fb42cf630346\",\"ReferenceId\":null,\"Name\":\"null\",\""
            + "Description\":\"\",\"Status\":\"Active\",\"State\":null,\"StartDate\":\"2013-05-24T12:25:44Z\",\""
            + "StartedBy\":\"Inovation Slussen\",\"StartedByUserId\":\"ba9c57f1-065c-46ed-b5f9-6fb50b0ab3a9\",\""
            + "CompletedDate\":null,\"ProcessId\":null,\"DomainId\":null},{\"Id\":\"f3d2aca1-acb0-4853-b9a5-"
            + "9e743d77b6a0\",\"ReferenceId\":null,\"Name\":\"patriktest2\",\"Description\":\"\",\"Status\":"
            + "\"Active\",\"State\":null,\"StartDate\":\"2013-05-22T11:27:58Z\",\"StartedBy\":\"Inovation "
            + "Slussen\",\"StartedByUserId\":\"ba9c57f1-065c-46ed-b5f9-6fb50b0ab3a9\",\"CompletedDate\":null,"
            + "\"ProcessId\":null,\"DomainId\":null},{\"Id\":\"7730f873-74b1-44fd-8d1f-8cc21f9c8b4e\",\""
            + "ReferenceId\":null,\"Name\":\"patriktest\",\"Description\":\"\",\"Status\":\"Active\",\"State\""
            + ":null,\"StartDate\":\"2013-05-22T11:12:34Z\",\"StartedBy\":\"Inovation Slussen\",\"StartedByUserId\""
            + ":\"ba9c57f1-065c-46ed-b5f9-6fb50b0ab3a9\",\"CompletedDate\":null,\"ProcessId\":null,\"DomainId\""
            + ":null},{\"Id\":\"0ab5f6df-d441-41a6-aa62-2157d1d3fe75\",\"ReferenceId\":null,\"Name\":\"Inovation"
            + " Slussen (2013-05-22 11:20:05)\",\"Description\":null,\"Status\":\"Active\",\"State\":null,\""
            + "StartDate\":\"2013-05-22T09:20:05Z\",\"StartedBy\":\"Inovation Slussen\",\"StartedByUserId\":\""
            + "ba9c57f1-065c-46ed-b5f9-6fb50b0ab3a9\",\"CompletedDate\":null,\"ProcessId\":null,\"DomainId\":null}"
            + ",{\"Id\":\"d6139087-d722-4200-9691-91a5d5697905\",\"ReferenceId\":null,\"Name\":\"Test Susanne\""
            + ",\"Description\":\"\",\"Status\":\"Active\",\"State\":null,\"StartDate\":\"2013-05-20T07:40:57Z\""
            + ",\"StartedBy\":\"Susanne Lindqvist\",\"StartedByUserId\":\"87b13dda-6ad5-4a81-b5f8-ac33e906d0c5\""
            + ",\"CompletedDate\":null,\"ProcessId\":null,\"DomainId\":null},{\"Id\":\"ef7ea30b-5089-428f-87e8"
            + "-f10acd285cb6\",\"ReferenceId\":null,\"Name\":\"NY IDE\",\"Description\":\"\",\"Status\":\"Active"
            + "\",\"State\":null,\"StartDate\":\"2013-05-03T08:50:38Z\",\"StartedBy\":\"Kristofer Johansson\","
            + "\"StartedByUserId\":\"dffc2654-cb10-433d-aeb8-9d87f6a05b96\",\"CompletedDate\":null,\"ProcessId"
            + "\":null,\"DomainId\":null}]}";
    private static final String RESPONSE_GET_IDEA_OBJECTS_FIELDS = "[{\"Id\":\"behov\",\"Name\":\"Behov eller"
            + " problem som din ide löser\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\""
            + ":\"att det tidigare inte fanns några ideer\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\""
            + "AllowBlank\":true,\"Index\":9999},{\"Id\":\"beslutsforslagsskommentar\",\"Name\":\"Kommentar\",\""
            + "DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"Vi föreslår att detta godtages"
            + "\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\""
            + "bilaga\",\"Name\":\"Bifoga gärna en skiss eller andra dokument som du tror kan vara till nytta "
            + "för att beskriva din ide\",\"DataType\":\"System.Binary\",\"FieldType\":\"fileuploadfield\",\""
            + "Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999}"
            + ",{\"Id\":\"e-post\",\"Name\":\"E-post\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\""
            + ",\"Value\":\"christofer.johansson@vgregion.se\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\""
            + "AllowBlank\":true,\"Index\":9999},{\"Id\":\"fodelsear\",\"Name\":\"Födelseår\",\"DataType\":\""
            + "System.String\",\"FieldType\":\"textfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\""
            + ":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"godkprio1\",\"Name\":\"godkprio1\",\"DataType\""
            + ":\"System.Bool\",\"FieldType\":\"checkbox\",\"Value\":\"off\",\"FieldTypeNamespace\":\"\",\""
            + "ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"godkprio2\",\"Name\":\"godkprio2\","
            + "\"DataType\":\"System.Bool\",\"FieldType\":\"checkbox\",\"Value\":\"off\",\"FieldTypeNamespace\""
            + ":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"HSA-ID.KIVenhet\",\"Name\""
            + ":\"Förvaltning\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"\",\""
            + "FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"ide\","
            + "\"Name\":\"Beskriv din ide kortfattat\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\","
            + "\"Value\":\"En ny ide\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\""
            + "Index\":9999},{\"Id\":\"Idetranportorenskommentar\",\"Name\":\"Kommentar\",\"DataType\":\""
            + "System.String\",\"FieldType\":\"textarea\",\"Value\":\"Kommentar\",\"FieldTypeNamespace\":\"\",\""
            + "ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"instance.name\",\"Name\":\"* "
            + "Projekttitel\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"NY IDE\""
            + ",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":false,\"Index\":9999},{\"Id\":\""
            + "kommavidare\",\"Name\":\"Hur tror du att Innovationsslussen kan hjälpa dig att komma vidare med din"
            + " ide\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\""
            + "FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\""
            + "koordinatornsskommentar\",\"Name\":\"Kommentar\",\"DataType\":\"System.String\",\"FieldType\":\""
            + "textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\""
            + "Index\":9999},{\"Id\":\"prio1kommentar\",\"Name\":\"Kommentar\",\"DataType\":\"System.String\",\""
            + "FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\""
            + "AllowBlank\":true,\"Index\":9999},{\"Id\":\"prio2kommentar\",\"Name\":\"Kommentar\",\"DataType\":\""
            + "System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\""
            + ":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"prioriteringsradsmote\",\"Name\":\""
            + "Prioriteringsrådsmöte\",\"DataType\":\"System.DateTime\",\"FieldType\":\"datefield\",\"Value\""
            + ":\"2013-05-23T22:00:00Z\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\""
            + "Index\":9999},{\"Id\":\"telefonnummer\",\"Name\":\"Telefonnummer\",\"DataType\":\"System.String\",\""
            + "FieldType\":\"textfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\""
            + "AllowBlank\":true,\"Index\":9999},{\"Id\":\"VGR-ID.fullname\",\"Name\":\"Namn\",\"DataType\":\""
            + "System.String\",\"FieldType\":\"textfield\",\"Value\":\"Kristofer Johansson\",\"FieldTypeNamespace"
            + "\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"VGR-ID.hsapostaladress\""
            + ",\"Name\":\"Postadress\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":"
            + "\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\""
            + "VGR-ID.titel\",\"Name\":\"Yrkesroll\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\""
            + "Value\":\"Systemutvecklare    \",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,"
            + "\"Index\":9999}]";
    private static final String RESPONE_GET_INSTANCE_OBJECTS = "{\"TotalCount\":0,\"Data\":[]}";
    private static final String RESPONSE_CREATE_INSTANCE = "{\"success\":true,\"InstanceId\":\"42be3964-1ada"
            + "-40c9-ab96-c3eca1cd2515\"}";


    /**
     * Instantiates a new mock barium rest client impl.
     *
     * @param apiLocation the api location
     * @param apiKey the api key
     * @param username the username
     * @param password the password
     * @param applicationId the application id
     * @throws BariumException the barium exception
     */
    public MockBariumRestClientImpl(String apiLocation, String apiKey, String username, String password,
            String applicationId)
                    throws BariumException {
    }

    @Override
    public boolean connect() throws BariumException {
        return true;
    }

    @Override
    public ApplicationInstances getApplicationInstances() throws BariumException {

        String instancesJson = RESPONSE_GET_APPLICATION_INSTANCES;

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

    /* (non-Javadoc)
     * @see se.vgregion.service.barium.BariumRestClient#getBariumInstance()
     */
    @Override
    public BariumInstance getBariumInstance(String instanceId) throws BariumException {

        /*
        String instanceJson = doGet("/Apps/" + applicationId + "/Instances" + "/" + instanceId);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(instanceJson, ApplicationInstance.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         */
        BariumInstance bariumInstance = null;

        return bariumInstance;
    }

    public String createInstance(String data) {
        return RESPONSE_CREATE_INSTANCE;
    }

    @Override
    public Objects getInstanceObjects(String instanceId) throws BariumException {
        String objectsJson = RESPONE_GET_INSTANCE_OBJECTS;

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(objectsJson, Objects.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Objects getObjectObjects(String objectId) throws BariumException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ObjectField> getIdeaObjectFields(ApplicationInstance instance) {
        String objectJson = RESPONSE_GET_IDEA_OBJECTS_FIELDS;
        LOGGER.info(objectJson);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(objectJson, TypeFactory.defaultInstance().constructCollectionType(List.class,
                    ObjectField.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ObjectField> getIdeaObjectFields(String instanceId) {


        /*
        String objectJson = RESPONSE_GET_IDEA_OBJECTS_FIELDS;
        LOGGER.info(objectJson);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(objectJson, TypeFactory.defaultInstance().constructCollectionType(List.class,
                    ObjectField.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         */

        List<ObjectField> objectFields = new ArrayList<ObjectField>();

        return objectFields;
    }

    @Override
    public String deleteBariumInstance(String id) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void uploadFile(String instanceId, String fileName, InputStream inputStream) throws BariumException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ObjectEntry getObject(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream doGetFileStream(String objectId) throws BariumException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String updateField(String id, String field, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String findFolder(String instanceId, String folderName) throws BariumException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void uploadFile(String instanceId, String folderName, String fileName, InputStream inputStream) throws BariumException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getIdeaState(String instanceId) {
        throw new UnsupportedOperationException();
    }


    @Override
    public String createIdeaInstance(IdeaObjectFields ideaObjectFields) {

        String bariumId = "";

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

        try {
            JSONObject jsonObject = new JSONObject(replyJson);

            bariumId = jsonObject.getString("InstanceId");

            System.out.println("BariumService - createIdea - InstanceId: " + bariumId);

        } catch (JSONException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return bariumId;
    }

    @Override
    public boolean updateInstance(String values, String objectId) {
        // TODO Auto-generated method stub
        return false;
    }
}


