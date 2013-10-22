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

import org.apache.commons.collections.BeanMap;
import org.junit.Ignore;
import org.junit.Test;
import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: portaldev
 * Date: 2013-08-22
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
 */

@Ignore
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

}
