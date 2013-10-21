package se.vgregion.portal.innovationsslussen.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class FileEntry {
    @JsonProperty("Success") private Boolean success;
    @JsonProperty("Name") private String name;
    @JsonProperty("Id") private String id;
    @JsonProperty("ReferenceId") private String referenceId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSuccess() {

        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
