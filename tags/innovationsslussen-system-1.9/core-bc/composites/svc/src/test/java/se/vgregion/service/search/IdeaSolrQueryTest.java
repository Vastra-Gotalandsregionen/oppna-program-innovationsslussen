package se.vgregion.service.search;

import org.junit.Test;

import static org.junit.Assert.*;

public class IdeaSolrQueryTest {

    @Test
    public void testBuildPhaseOrString() throws Exception {
        String phaseOrString = IdeaSolrQuery.buildPhaseOrString(0);
        assertEquals("phase:0", phaseOrString);

        phaseOrString = IdeaSolrQuery.buildPhaseOrString(0, 1);
        assertEquals("phase:0 OR phase:1", phaseOrString);

        phaseOrString = IdeaSolrQuery.buildPhaseOrString(0, 1, 2);
        assertEquals("phase:0 OR phase:1 OR phase:2", phaseOrString);
    }
}