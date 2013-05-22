package se.vgregion.service.barium;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

/**
 * @author Patrik Bergström
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-test.xml"})
public class BariumRestClientIT {

    @Value("${apiLocation}")
    private String apiLocation;
    @Value("${apiKey}")
    private String apiKey;
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;
    @Value(("${applicationId}"))
    private String applicationId;

    @Test
    public void testConnect() throws Exception {
        BariumRestClient bariumRestClient = new BariumRestClient();
        boolean connect = bariumRestClient.connect(apiLocation, apiKey, username, password, applicationId);

        assertTrue(connect);
    }

    @Test
    public void testGetAllTaskInfo2() throws Exception {
        BariumRestClient bariumRestClient = new BariumRestClient();

        bariumRestClient.setApiLocation(apiLocation);
        bariumRestClient.setApiKey(apiKey);
        bariumRestClient.setUsername(username);
        bariumRestClient.setPassword(password);

//        String s = bariumRestClient.doGet("/DataFormTemplates/565d4c81-4baa-451b-aacc-5f7ae295bfaf/Fields"); // Får formulär-modell
//        String s = bariumRestClient.doGet("Tasks");
//        String s = bariumRestClient.doGet("/Apps/a29b55e4-68d6-41dd-872e-877badb566cb/Instances");
//        String s = bariumRestClient.doPost("/Apps/a29b55e4-68d6-41dd-872e-877badb566cb?message=START&template=565d4c81-4baa-451b-aacc-5f7ae295bfaf", "name=patriktest");
//        String s = bariumRestClient.createInstance("message=START&template=565d4c81-4baa-451b-aacc-5f7ae295bfaf&instance.name=patriktest3&behov=behov1");//&beslutsforslagsskommentar&"); // awesome
//        String s = bariumRestClient.doPost("/Objects/f5b8cf5f-f214-4e35-9e13-fb04223568db/Fields", "behov=nyttbehov"); // Funkar
//        String s = bariumRestClient.doPostMultipart("/Objects/f5b8cf5f-f214-4e35-9e13-fb04223568db/Objects", new byte[]{1, 2, 3, 4, 5, 6}); //

//        String s = bariumRestClient.doPostMultipart("/Objects/5e2b60b2-a582-4521-ac6a-49e218734096/Objects", "blabla...".getBytes()); // Laddar upp fil till mappen
//        String s = bariumRestClient.doPost("/Objects/f5b8cf5f-f214-4e35-9e13-fb04223568db/Fields/fileuploadfield", new byte[]{1,2,3,4,5,6});
//        String s = bariumRestClient.doGet("/startevents");
//        String s = bariumRestClient.doGet("/Instances/ef7ea30b-5089-428f-87e8-f10acd285cb6");
//        String s = bariumRestClient.doGet("/Objects");
//        String s = bariumRestClient.doGet("/Instances/ef7ea30b-5089-428f-87e8-f10acd285cb6/Objects/IDE");
//        String s = bariumRestClient.doGet("/Instances/ef7ea30b-5089-428f-87e8-f10acd285cb6/Objects/IDE/Fields");
//        String s = bariumRestClient.doGet("/Instances/ef7ea30b-5089-428f-87e8-f10acd285cb6/Objects");
//        String s = bariumRestClient.doGet("/Instances/ef7ea30b-5089-428f-87e8-f10acd285cb6/Fields");
//        String s = bariumRestClient.doGet("/Instances/f3d2aca1-acb0-4853-b9a5-9e743d77b6a0/Objects/IDE/Fields"); // the newly created with the api
//        String s = bariumRestClient.doGet("/Instances/7730f873-74b1-44fd-8d1f-8cc21f9c8b4e/Objects/IDE/Fields"); // the newly created with the api
//        String s = bariumRestClient.doGet("Processes"); // 400
//        String s = bariumRestClient.doGet("Types"); // 0 träffar
//        String s = bariumRestClient.doGet("/Objects/5e2b60b2-a582-4521-ac6a-49e218734096"); // Mappen
//        String s = bariumRestClient.doGet("/Objects/f5b8cf5f-f214-4e35-9e13-fb04223568db"); // Formuläret
        String s = bariumRestClient.doGet("/Objects/0290a271-e592-4617-8fbb-10c08303b7e2"); // asdf-filen
        System.out.println("s: " + s);
//        System.out.println("s: " + s2);

//        System.out.println(s.contains("INNOVATION"));
    }

}
