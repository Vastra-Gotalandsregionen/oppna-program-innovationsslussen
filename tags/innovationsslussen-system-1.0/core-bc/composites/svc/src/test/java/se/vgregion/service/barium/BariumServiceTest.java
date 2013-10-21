package se.vgregion.service.barium;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.vgregion.portal.innovationsslussen.domain.BariumResponse;
import se.vgregion.portal.innovationsslussen.domain.IdeaContentType;
import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstance;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstances;
import se.vgregion.service.innovationsslussen.idea.IdeaServiceImpl;


/**
 * @author Patrik Bergström
 * @author Simon Göransson - simon.goransson@monator.com - vgrid: simgo3
 */
public class BariumServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BariumServiceTest.class);

    BariumRestClient bariumRestClient;
    BariumService bariumService;
    IdeaServiceImpl ideaService;

    String apiLocation = "http://localhost:8089";
    String apiKey = "apiKey";
    String username = "user";
    String password = "password1";
    String appId = "appId";

    String ideaId = "0fbe5dfe-12b2-462b-b585-5d629ca3c9e6";

    String ideaId2 = "4563a84a-b19c-4c75-90d8-1ef18c2bebcc";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Before
    public void setup() throws BariumException {

        bariumRestClient = new BariumRestClientImpl(apiLocation, apiKey, username, password, appId);
        bariumService = new BariumService(bariumRestClient);
        ideaService = new IdeaServiceImpl();



        // POSTs
        stubFor(post(urlEqualTo("/authenticate"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("yes")));

        stubFor(post(urlEqualTo("/Instances/" + ideaId + "/Objects"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"Items\":[{\"Id\":\"apa\"}]}")
                        ));

        stubFor(post(urlEqualTo("/Objects/apa/Objects"))
                .willReturn(aResponse()
                        .withStatus(200)));

        stubFor(post(urlEqualTo("/Objects/" + ideaId + "/Fields"))
                .willReturn(aResponse()
                        .withStatus(200)));

        stubFor(post(urlEqualTo("/apps/" + appId  ))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"InstanceId\":\"apa\",\"success\":\"true\"}")));


        //GETs
        stubFor(get(urlEqualTo("/Apps/" + appId + "/Instances"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"TotalCount\":1,\"Data\":[{\"Id\":\"" + ideaId + "\"," +
                                "\"ReferenceId\":null,\"Name\":\"Simon-Testar-local#18\",\"Description\":\"\"," +
                                "\"Status\":\"Active\",\"State\":null,\"StartDate\":\"2013-09-24T07:33:55Z\"," +
                                "\"StartedBy\":\"Innovationsslussen Barium (Innovationsslussen)\"," +
                                "\"StartedByUserId\":\"ba9c57f1-065c-46ed-b5f9-6fb50b0ab3a9\"," +
                                "\"CompletedDate\":null,\"ProcessId\":\"f1386159-6e74-4ddb-beba-09117ec79ce5\"," +
                                "\"DomainId\":\"14cb48ab-e10a-4a66-a992-e5201c3f1083\"}]}")));

        stubFor(get(urlEqualTo("/instances/" + ideaId + "/Objects/IDE/Fields"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("[{\"Id\":\"" +  ideaId + "\",\"Name\":\"Behov eller problem som din ide löser\"," +
                                "\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"problem\"," +
                                "\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999}]")));

        stubFor(get(urlEqualTo("/Instances/" + ideaId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"Id\":\"4563a84a-b19c-4c75-90d8-1ef18c2bebcc\",\"ReferenceId\":null,\"Name\":" +
                                "\"ClaesIde5\",\"Description\":\"\",\"Status\":\"Active\",\"State\":null,\"StartDate\":" +
                                "\"2013-08-22T09:14:49Z\",\"StartedBy\":\"Innovationsslussen Barium\",\"StartedByUserId\":" +
                                "\"ba9c57f1-065c-46ed-b5f9-6fb50b0ab3a9\",\"CompletedDate\":null,\"Priority\":100," +
                                "\"PlannedDate\":null,\"DeadlineDate\":null,\"ApplicationId\":" +
                                "\"a29b55e4-68d6-41dd-872e-877badb566cb\",\"ApplicationName\":\"INNOVATIONSSLUSSEN\"," +
                                "\"ParentInstanceName\":\"\",\"ParentInstanceId\":\"\",\"ProcessId\":" +
                                "\"f1386159-6e74-4ddb-beba-09117ec79ce5\"}")));

        stubFor(get(urlEqualTo("/instances/" + ideaId2 + "/Objects/IDE/Fields"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("[{\"Id\":\"behov\",\"Name\":\"Behov eller problem som din ide löser\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"problem\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"beslutsforslag\",\"Name\":\"Beslutsförslag\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"beslutsforslagsmotivering\",\"Name\":\"Motivering\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"bilaga\",\"Name\":\"Bifoga gärna en skiss eller andra dokument som du tror kan vara till nytta för att beskriva din ide\",\"DataType\":\"System.Binary\",\"FieldType\":\"fileuploadfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"fodelsear\",\"Name\":\"Födelseår\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"godkejprio1\",\"Name\":\"godkejprio1\",\"DataType\":\"System.Bool\",\"FieldType\":\"checkbox\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"godkejprio2\",\"Name\":\"godkejprio2\",\"DataType\":\"System.Bool\",\"FieldType\":\"checkbox\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"godkprio1\",\"Name\":\"godkprio1\",\"DataType\":\"System.Bool\",\"FieldType\":\"checkbox\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"godkprio2\",\"Name\":\"godkprio2\",\"DataType\":\"System.Bool\",\"FieldType\":\"checkbox\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"ide\",\"Name\":\"Beskriv din ide kortfattat\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"Besk 5\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"Idetranportorenskommentarintern\",\"Name\":\"Idétransportörens kommentarer\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"idetransportorenskonceptkommentar\",\"Name\":\"Idetransportörens kommentar\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"instance.name\",\"Name\":\"* Projekttitel\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"ClaesIde5\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":false,\"Index\":9999},{\"Id\":\"kommavidare\",\"Name\":\"Hur tror du att Innovationsslussen kan hjälpa dig att komma vidare med din ide\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"hjälp\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"komplnamn\",\"Name\":\"Är ni fler idégivare?\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"kon\",\"Name\":\"Kön\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"koordinatornskommentar\",\"Name\":\"Kommentar\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"koordinatornskommentarkoncept\",\"Name\":\"Koordinatorns kommentar\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"prio1kommentar\",\"Name\":\"Kommentar\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"prio2kommentar\",\"Name\":\"Kommentar\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"prioriteringsradsmote\",\"Name\":\"Prioriteringsrådsmöte\",\"DataType\":\"System.DateTime\",\"FieldType\":\"datefield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"publik\",\"Name\":\"publik\",\"DataType\":\"System.Bool\",\"FieldType\":\"checkbox\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"publikbeskrivning\",\"Name\":\"Publik beskrivning\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"publikintrotext\",\"Name\":\"Publik introtext\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"siteLank\",\"Name\":\"Länk till Liferay\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"http://localhost:9080/web/innovationsslussen/ide/-/idea/claeside5\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"testat\",\"Name\":\"Har du testat din ide?\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"test\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"VGR-ID\",\"Name\":\"VGR-ID\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"none\",\"FieldTypeNamespace\":\"KIV.user.properties\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"VGR-ID.email\",\"Name\":\"E-post\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"claes.lundahl@vgregion.se\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"VGR-ID.forvaltning\",\"Name\":\"Förvaltning\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"VGR-ID.fullname\",\"Name\":\"Namn\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"Claes Lundahl\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"VGR-ID.hsapublictelephonenumber\",\"Name\":\"Telefonnummer\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"VGR-ID.KIVenhet\",\"Name\":\"Enhet\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"VGR-ID.mobiletelephonenumber\",\"Name\":\"Mobiltelefonnummer\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"0727414600\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"VGR-ID.titel\",\"Name\":\"Yrkesroll\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999}]")));

        stubFor(get(urlEqualTo("/Instances/" + ideaId + "/Objects"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"TotalCount\":1,\"Data\":[{\"ObjectClass\":\"repository.folder\", \"Name\":\"apa\" }]}")));

        stubFor(get(urlEqualTo("/Instances/" + ideaId + "/Objects/IDE/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"Id\":\"" + ideaId + "\"}")));


        // URL url = getClass().getResource("/text.txt");
        // System.out.println("FFF " + url.getPath());

        stubFor(get(urlEqualTo("/Objects/" + "fileId" + "/File"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("Hello world")));



        // DELETE
        stubFor(delete(urlEqualTo("/Instances/" + ideaId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{success:true}")));



    }

    @Test
    public void testGetAllIdeas() {
        List<IdeaObjectFields> allIdeas = bariumService.getAllIdeas();
        Assert.assertEquals(1, allIdeas.size());
    }

    @Test
    public void  testDeleteBariumIdea(){
        BariumResponse bariumResponse = bariumService.deleteBariumIdea(ideaId);
        Assert.assertTrue(bariumResponse.getSuccess());

    }

    @Test
    public void testGetBariumIdea(){

        IdeaObjectFields bariumIdea = bariumService.getBariumIdea(ideaId);

        Idea idea = new Idea();

        ideaService.populateIdea(bariumIdea, idea);

        Assert.assertEquals(idea.getTitle(), "ClaesIde5");

    }

    @Test
    public void testUpLoadFile() throws BariumException, FileNotFoundException {

        Idea idea = new Idea();
        idea.setId(ideaId);
        InputStream inputStream = getClass().getResourceAsStream("/text.txt");
        bariumService.uploadFile(idea,"fileName.txt", inputStream);
        verify(postRequestedFor(urlEqualTo("/Instances/" + ideaId + "/Objects"))
                .withRequestBody(containing("fileName.txt")));

    }

    @Test
    public void testUpLoadFileInFolder() throws BariumException, FileNotFoundException {

        Idea idea = new Idea();
        idea.setId(ideaId);
        InputStream inputStream = getClass().getResourceAsStream("/text.txt");
        bariumService.uploadFile(idea,"fileName.txt", "folderName",inputStream);
        verify(postRequestedFor(urlEqualTo("/Instances/" + ideaId + "/Objects"))
                .withRequestBody(containing("fileName.txt")));

    }

    @Test
    public void testUpdateIdea() throws BariumException {

        bariumService.updateIdea(ideaId,"name","bepa");
        verify(postRequestedFor(urlEqualTo("/Objects/" + ideaId + "/Fields"))
                .withRequestBody(containing("name=bepa")));
    }

    @Test
    public void testDownloadFile() throws BariumException {
        InputStream inputStream = bariumService.downloadFile("fileId");

        BufferedInputStream bis = new BufferedInputStream(inputStream);

        String response = toString(bis);

        Assert.assertEquals("Hello world", response);

    }

    @Test
    public void testCreateIdea() {

        Idea idea = new Idea();

        idea.setId("123");
        idea.setTitle("My idea");


        IdeaContent ideaContentPrivate = new IdeaContent();
        IdeaContent ideaContentPublic = new IdeaContent();

        ideaContentPrivate.setSolvesProblem("apa");

        ideaContentPrivate.setType(IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE);
        ideaContentPublic.setType(IdeaContentType.IDEA_CONTENT_TYPE_PUBLIC);

        idea.addIdeaContent(ideaContentPrivate);
        idea.addIdeaContent(ideaContentPublic);

        IdeaPerson ideaPerson = new IdeaPerson();

        idea.addIdeaPerson(ideaPerson);



        BariumResponse bariumResponse = bariumService.createIdea(idea);
        Assert.assertTrue(bariumResponse.getSuccess());



    }




    // This test also tests the se.vgregion.service.barium.BariumRestClient.getIdeaObjectFields() method.
    @Test
    public void testGetAllIdeasOld() throws Exception {
        BariumRestClientImpl bariumRestClient = mock(BariumRestClientImpl.class);

        when(bariumRestClient.getIdeaObjectFields(any(ApplicationInstance.class))).thenCallRealMethod();

        ApplicationInstances instances = new ApplicationInstances();
        instances.setData(new ArrayList<ApplicationInstance>());
        ApplicationInstance instance = new ApplicationInstance();
        instance.setId("1234");
        instances.getData().add(instance);

        String json = "[{\"Id\":\"behov\",\"Name\":\"Behov eller problem som din ide löser\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"Ett stort behov\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"beslutsforslagsskommentar\",\"Name\":\"Kommentar\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"bilaga\",\"Name\":\"Bifoga gärna en skiss eller andra dokument som du tror kan vara till nytta för att beskriva din ide\",\"DataType\":\"System.Binary\",\"FieldType\":\"fileuploadfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"e-post\",\"Name\":\"E-post\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"mail@mailinator.com,mail@mailinator.com\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"fodelsear\",\"Name\":\"Födelseår\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"godkprio1\",\"Name\":\"godkprio1\",\"DataType\":\"System.Bool\",\"FieldType\":\"checkbox\",\"Value\":\"off\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"godkprio2\",\"Name\":\"godkprio2\",\"DataType\":\"System.Bool\",\"FieldType\":\"checkbox\",\"Value\":\"off\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"HSA-ID.KIVenhet\",\"Name\":\"Förvaltning\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"ide\",\"Name\":\"Beskriv din ide kortfattat\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"En lysande idé\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"Idetranportorenskommentar\",\"Name\":\"Kommentar\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"instance.name\",\"Name\":\"* Projekttitel\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"TestInstans\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":false,\"Index\":9999},{\"Id\":\"kommavidare\",\"Name\":\"Hur tror du att Innovationsslussen kan hjälpa dig att komma vidare med din ide\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"koordinatornsskommentar\",\"Name\":\"Kommentar\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"prio1kommentar\",\"Name\":\"Kommentar\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"prio2kommentar\",\"Name\":\"Kommentar\",\"DataType\":\"System.String\",\"FieldType\":\"textarea\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"prioriteringsradsmote\",\"Name\":\"Prioriteringsrådsmöte\",\"DataType\":\"System.DateTime\",\"FieldType\":\"datefield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"telefonnummer\",\"Name\":\"Telefonnummer\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"VGR-ID.fullname\",\"Name\":\"Namn\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"Kalle Johansson\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"VGR-ID.hsapostaladress\",\"Name\":\"Postadress\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999},{\"Id\":\"VGR-ID.titel\",\"Name\":\"Yrkesroll\",\"DataType\":\"System.String\",\"FieldType\":\"textfield\",\"Value\":\"Högsta hönset\",\"FieldTypeNamespace\":\"\",\"ReadOnly\":false,\"AllowBlank\":true,\"Index\":9999}]";
        doReturn(instances).when(bariumRestClient).getApplicationInstances();
        doReturn(json).when(bariumRestClient).doGet("/instances/" + instance.getId() + "/Objects/IDE/Fields");
        doReturn(true).when(bariumRestClient).connect();

        BariumService bariumService = new BariumService(bariumRestClient);

        List<IdeaObjectFields> allIdeas = bariumService.getAllIdeas();

        for (IdeaObjectFields idea : allIdeas) {
            LOGGER.info("idea: " + idea.getIde());
        }

        assertEquals(1, allIdeas.size());
        assertEquals("Ett stort behov", allIdeas.get(0).getBehov());
    }


    /**
     * To string.
     *
     * @param inputStream the input stream
     * @return the string
     * @throws BariumException the barium exception
     */
    private String toString(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        try {

            final int byteSize = 1024;

            byte[] buf = new byte[byteSize];
            int n;
            while ((n = inputStream.read(buf)) != -1) {
                sb.append(new String(buf, 0, n, "UTF-8"));
            }
        } catch (IOException e) {

        }
        return sb.toString();
    }

}
