package se.vgregion.portal.innovationsslussen.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class ApplicationInstances {
    @JsonProperty("Data") private List<ApplicationInstance> data;
    @JsonProperty("TotalCount") private Integer totalCount;

    public List<ApplicationInstance> getData() {
        return data;
    }

    public void setData(List<ApplicationInstance> data) {
        this.data = data;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
