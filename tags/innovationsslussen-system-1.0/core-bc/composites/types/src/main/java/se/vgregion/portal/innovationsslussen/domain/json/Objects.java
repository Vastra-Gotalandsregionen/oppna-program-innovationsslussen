package se.vgregion.portal.innovationsslussen.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class Objects {
    @JsonProperty("Data") private List<ObjectEntry> data;
    @JsonProperty("TotalCount") private Integer totalCount;

    public List<ObjectEntry> getData() {
        return data;
    }

    public void setData(List<ObjectEntry> data) {
        this.data = data;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
