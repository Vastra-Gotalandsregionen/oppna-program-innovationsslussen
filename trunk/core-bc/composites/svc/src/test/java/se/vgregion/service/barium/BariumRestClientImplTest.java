package se.vgregion.service.barium;

import junit.framework.Assert;
import org.junit.Test;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectEntry;
import se.vgregion.portal.innovationsslussen.domain.json.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-08-22
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
 */
public class BariumRestClientImplTest {

    String json = "{\"TotalCount\":4,\"Data\":[{\"Id\":\"092fe5af-c168-4419-8e00-80f93ead573b\",\"ReferenceId\":null," +
            "\"Name\":\"Liferay stängda dokument\",\"Description\":\"\",\"Type\":\"folder\",\"ObjectClass\":\"repository.folder\"," +
            "\"Container\":true,\"ReadOnly\":false,\"TypeNamespace\":null,\"TemplateId\":null,\"FileType\":\"\",\"SortIndex\":null," +
            "\"State\":null,\"CreatedDate\":\"2013-08-22T09:14:56Z\",\"UpdatedDate\":null,\"DataId\":null}," +
            "{\"Id\":\"81e14c45-d205-49d5-a83b-854a8e88f65f\",\"ReferenceId\":null,\"Name\":\"Liferay öppna dokument\"," +
            "\"Description\":\"\",\"Type\":\"folder\",\"ObjectClass\":\"repository.folder\",\"Container\":true," +
            "\"ReadOnly\":false,\"TypeNamespace\":null,\"TemplateId\":null,\"FileType\":\"\",\"SortIndex\":null," +
            "\"State\":null,\"CreatedDate\":\"2013-08-22T09:14:56Z\",\"UpdatedDate\":null,\"DataId\":null}," +
            "{\"Id\":\"ddd54fdb-c50a-4504-ac86-b5b177b8ee3a\",\"ReferenceId\":null,\"Name\":\"IDE\"," +
            "\"Description\":\"\",\"Type\":\"dataform\",\"ObjectClass\":\"dataforms.dataform\",\"Container\":false," +
            "\"ReadOnly\":false,\"TypeNamespace\":null,\"TemplateId\":\"565d4c81-4baa-451b-aacc-5f7ae295bfaf\"," +
            "\"FileType\":\"\",\"SortIndex\":null,\"State\":\"1\",\"CreatedDate\":\"2013-08-22T09:14:50Z\"," +
            "\"UpdatedDate\":\"2013-08-22T09:14:54Z\",\"DataId\":\"IDE\"},{\"Id\":\"fec968af-66cb-410b-9934-19a8cf32e0e9\"," +
            "\"ReferenceId\":null,\"Name\":\"Bariumdokument\",\"Description\":\"\",\"Type\":\"folder\"," +
            "\"ObjectClass\":\"repository.folder\",\"Container\":true,\"ReadOnly\":false,\"TypeNamespace\":null," +
            "\"TemplateId\":null,\"FileType\":\"\",\"SortIndex\":null,\"State\":null,\"CreatedDate\":\"2013-08-22T09:14:57Z\"," +
            "\"UpdatedDate\":null,\"DataId\":null}]}\n";

    @Test
    public void toUrl() {

    }
}
