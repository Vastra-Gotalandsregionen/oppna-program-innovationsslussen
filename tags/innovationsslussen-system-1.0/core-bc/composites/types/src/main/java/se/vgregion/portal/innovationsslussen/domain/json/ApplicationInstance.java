package se.vgregion.portal.innovationsslussen.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

public class ApplicationInstance {
    @JsonProperty("Name") private String name;
    @JsonProperty("Status") private String status;
    @JsonProperty("Description") private String description;
    @JsonProperty("StartDate") private String startDate;
    @JsonProperty("State") private Object state;
    @JsonProperty("StartedByUserId") private String startedByUserId;
    @JsonProperty("DomainId") private Object domainId;
    @JsonProperty("Id") private String id;
    @JsonProperty("ProcessId") private Object processId;
    @JsonProperty("CompletedDate") private Object completedDate;
    @JsonProperty("StartedBy") private String startedBy;
    @JsonProperty("ReferenceId") private Object referenceId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public String getStartedByUserId() {
        return startedByUserId;
    }

    public void setStartedByUserId(String startedByUserId) {
        this.startedByUserId = startedByUserId;
    }

    public Object getDomainId() {
        return domainId;
    }

    public void setDomainId(Object domainId) {
        this.domainId = domainId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getProcessId() {
        return processId;
    }

    public void setProcessId(Object processId) {
        this.processId = processId;
    }

    public Object getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Object completedDate) {
        this.completedDate = completedDate;
    }

    public String getStartedBy() {
        return startedBy;
    }

    public void setStartedBy(String startedBy) {
        this.startedBy = startedBy;
    }

    public Object getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Object referenceId) {
        this.referenceId = referenceId;
    }
}
