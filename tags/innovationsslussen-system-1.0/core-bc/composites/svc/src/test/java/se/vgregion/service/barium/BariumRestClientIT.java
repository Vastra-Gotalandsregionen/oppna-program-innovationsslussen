package se.vgregion.service.barium;

import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstance;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstances;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectEntry;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectField;
import se.vgregion.portal.innovationsslussen.domain.json.Objects;

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
    @Value("${applicationId}")
    private String applicationId;

    @Test
    public void testConnect() throws Exception {
        BariumRestClientImpl bariumRestClient = new BariumRestClientImpl(apiLocation, apiKey, username, password, applicationId);
        boolean connect = bariumRestClient.connect();

        assertTrue(connect);
    }



    @Test
    @Ignore
    public void testGetApplicationInstances() throws BariumException {
        BariumRestClientImpl bariumRestClient = createBariumRestClient();

        ApplicationInstances applicationInstances = bariumRestClient.getApplicationInstances();

        for (ApplicationInstance instance : applicationInstances.getData()) {
            System.out.println(instance.getId() + " " + instance.getName() + " " + instance.getReferenceId());
            Objects instanceObjects = bariumRestClient.getInstanceObjects(instance.getId());
            for (ObjectEntry objectEntry : instanceObjects.getData()) {
                System.out.println("ObjectEntry: " + objectEntry.getFileType() + " " + objectEntry.getName());
            }

            List<ObjectField> ideObjectFields = bariumRestClient.getIdeaObjectFields(instance);

            if (ideObjectFields != null) {
                for (ObjectField field : ideObjectFields) {
                    System.out.println("Field: " + field.getId() + " - " + field.getValue());
                }
                System.out.println();
            }
        }
    }

    @Test
    @Ignore
    public void testCreateFormInstance() throws Exception {
        BariumRestClient bariumRestClient = createBariumRestClient();

        IdeaObjectFields ideaObjectFields = new IdeaObjectFields();
        ideaObjectFields.setInstanceName("TestInstans");
        ideaObjectFields.setBehov("Ett stort behov");
        ideaObjectFields.setEpost("mail@mailinator.com");
        ideaObjectFields.setIde("En lysande idé");
        ideaObjectFields.setKommavidare("Jag behöver hjälp med att tänka.");
        ideaObjectFields.setVgrId("kaljo3");
        ideaObjectFields.setVgrIdFullname("Kalle Johansson");
        ideaObjectFields.setVgrIdTitel("Högsta hönset");

        //        ideaObjectFields.setGodkprio1(true);
        //        ideaObjectFields.setGodkprio2(true);

        bariumRestClient.createIdeaInstance(ideaObjectFields);
    }

    @Test
    //@Ignore
    public void testGetAllTaskInfo2() throws Exception {
        BariumRestClientImpl bariumRestClient = createBariumRestClient();

        //        String s = bariumRestClient.doGet("/DataFormTemplates/565d4c81-4baa-451b-aacc-5f7ae295bfaf/Fields"); // Får formulär-modell
        //        String s = bariumRestClient.doGet("Tasks");
        //        String s = bariumRestClient.doGet("/Apps/a29b55e4-68d6-41dd-872e-877badb566cb/Instances");
        //        String s = bariumRestClient.doPost("/Apps/a29b55e4-68d6-41dd-872e-877badb566cb?message=START&template=565d4c81-4baa-451b-aacc-5f7ae295bfaf", "name=patriktest");
        //        String s = bariumRestClient.createInstance("message=START&template=565d4c81-4baa-451b-aacc-5f7ae295bfaf&instance.name=patriktest3&behov=behov1");//&beslutsforslagsskommentar&"); // awesome
        //        String s = bariumRestClient.doPost("/Objects/f5b8cf5f-f214-4e35-9e13-fb04223568db/Fields", "behov=nyttbehov"); // Funkar
        //        String s = bariumRestClient.doPostMultipart("/Objects/f5b8cf5f-f214-4e35-9e13-fb04223568db/Objects", new byte[]{1, 2, 3, 4, 5, 6}); //

        //        String s = bariumRestClient.doPostMultipart("/Objects/5e2b60b2-a582-4521-ac6a-49e218734096/Objects", "blabla...".getBytes()); // Laddar upp fil till mappen
        //        String s = bariumRestClient.doPostMultipart("/Instances/9c9e2b6c-93ec-4334-acb0-dfabffdd5dc8/Objects", "tasdf332.txt", new ByteArrayInputStream("blabla...".getBytes())); // Laddar upp fil till instansen
        //String s = bariumRestClient.doPostMultipart("/Objects/5493ce84-077e-4554-a86e-c2fdf3011787/Objects", "tåhävning.txt", new ByteArrayInputStream("blabla...".getBytes())); // Laddar upp fil till instansen
        //        String s = bariumRestClient.doPost("/Objects/f5b8cf5f-f214-4e35-9e13-fb04223568db/Fields/fileuploadfield", new byte[]{1,2,3,4,5,6});
        //        String s = bariumRestClient.doGet("/startevents");

        //        String s = bariumRestClient.doGet("/Instances/9c9e2b6c-93ec-4334-acb0-dfabffdd5dc8/Objects");

        //String formData = bariumRestClient.doGet("/Instances/4c21203b-ec1f-412c-9c02-7d0545f3d3ad/Objects/IDE/Fields/");

        String allData = bariumRestClient.doGet("/Instances/4563a84a-b19c-4c75-90d8-1ef18c2bebcc/Objects/IDE/Fields");

        System.out.println(allData);

        //        String s = bariumRestClient.doGet("/Instances/2c75baaf-6a60-476e-ad2d-724f965f7817/Objects/IDE/Fields");
        //        String s = bariumRestClient.doGet("/Instances");
        //        String s = bariumRestClient.doGet("/Objects/89d4e894-bc31-4ecd-ab6d-d4fb993d9edd/File");
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
        //        String s = bariumRestClient.doGet("/Objects/0290a271-e592-4617-8fbb-10c08303b7e2"); // asdf-filen
        //System.out.println("BariumRestClientIT - testGetAllTaskInfo2 s: " + s);
        //System.out.println(s.length());
        //        System.out.println("s: " + s2);

        //        System.out.println(s.contains("INNOVATION"));
    }

    @Test
    @Ignore
    public void doGetFileStream() throws BariumException, IOException {
        BariumRestClientImpl bariumRestClient = createBariumRestClient();

        InputStream inputStream = bariumRestClient.doGetFileStream("89d4e894-bc31-4ecd-ab6d-d4fb993d9edd");

        BufferedInputStream bis = new BufferedInputStream(inputStream);

        int n;
        byte[] buf = new byte[1024];
        while ((n = bis.read(buf)) != -1) {
            System.out.println(new String(buf, 0, n));
        }
    }

    private BariumRestClientImpl createBariumRestClient() {
        BariumRestClientImpl bariumRestClient = null;
        try {
            bariumRestClient = new BariumRestClientImpl(apiLocation, apiKey, username, password, applicationId);
        } catch (BariumException e) {
            e.printStackTrace();
        }

        return bariumRestClient;
    }


    public static void main(String[] args) {

        try {
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL,
                    "LDAP://my.ldap.server:389"); //replace with your server URL/IP
            //only DIGEST-MD5 works with our Windows Active Directory
            env.put(Context.SECURITY_AUTHENTICATION,
                    "DIGEST-MD5"); //No other SALS worked with me
            env.put(Context.SECURITY_PRINCIPAL,
                    "user1"); // specify the username ONLY to let Microsoft Happy
            env.put(Context.SECURITY_CREDENTIALS, "secret1");   //the password

            DirContext ctx = new InitialDirContext(env);

            ctx.close();

        } catch(NamingException ne) {
            System.out.println("Error authenticating user:");
            System.out.println(ne.getMessage());
            return;
        }

        //if no exception, the user is already authenticated.
        System.out.println("OK, successfully authenticating user");
    }


}


