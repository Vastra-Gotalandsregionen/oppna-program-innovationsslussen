package se.vgregion.service.barium;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstance;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstances;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Patrik Bergström
 */
public class BariumServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BariumServiceTest.class);

    // This test also tests the se.vgregion.service.barium.BariumRestClient.getIdeaObjectFields() method.
    @Test
    public void testGetAllIdeas() throws Exception {
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
        doReturn(true).when(bariumRestClient).connect(anyString(), anyString(), anyString(), anyString(), anyString());

        BariumService bariumService = new BariumService(bariumRestClient);

        List<IdeaObjectFields> allIdeas = bariumService.getAllIdeas();

        for (IdeaObjectFields idea : allIdeas) {
            LOGGER.info("idea: " + idea.getIde());
        }

        assertEquals(1, allIdeas.size());
        assertEquals("Ett stort behov", allIdeas.get(0).getBehov());
    }
}
