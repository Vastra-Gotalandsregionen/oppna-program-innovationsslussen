package se.vgregion.portal.innovatinosslussen.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class ApplicationInstances {
	@JsonProperty("Data") private List<ApplicationInstance> Data;
	@JsonProperty("TotalCount") private Integer TotalCount;

    public List<ApplicationInstance> getData() {
        return Data;
    }

    public void setData(List<ApplicationInstance> data) {
        Data = data;
    }

    public Integer getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(Integer totalCount) {
        TotalCount = totalCount;
    }
}
