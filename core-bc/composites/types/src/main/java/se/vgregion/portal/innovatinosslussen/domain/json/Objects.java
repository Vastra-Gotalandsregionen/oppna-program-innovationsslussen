package se.vgregion.portal.innovatinosslussen.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class Objects {
	@JsonProperty("Data") private List<ObjectEntry> Data;
	@JsonProperty("TotalCount") private Integer TotalCount;

    public List<ObjectEntry> getData() {
        return Data;
    }

    public void setData(List<ObjectEntry> data) {
        Data = data;
    }

    public Integer getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(Integer totalCount) {
        TotalCount = totalCount;
    }
}
