package se.vgregion.portal.innovationsslussen.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

public class ApplicationInstance {
	@JsonProperty("Name") private String Name;
	@JsonProperty("Status") private String Status;
	@JsonProperty("Description") private String Description;
	@JsonProperty("StartDate") private String StartDate;
	@JsonProperty("State") private Object State;
	@JsonProperty("StartedByUserId") private String StartedByUserId;
	@JsonProperty("DomainId") private Object DomainId;
	@JsonProperty("Id") private String Id;
	@JsonProperty("ProcessId") private Object ProcessId;
	@JsonProperty("CompletedDate") private Object CompletedDate;
	@JsonProperty("StartedBy") private String StartedBy;
	@JsonProperty("ReferenceId") private Object ReferenceId;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public Object getState() {
        return State;
    }

    public void setState(Object state) {
        State = state;
    }

    public String getStartedByUserId() {
        return StartedByUserId;
    }

    public void setStartedByUserId(String startedByUserId) {
        StartedByUserId = startedByUserId;
    }

    public Object getDomainId() {
        return DomainId;
    }

    public void setDomainId(Object domainId) {
        DomainId = domainId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Object getProcessId() {
        return ProcessId;
    }

    public void setProcessId(Object processId) {
        ProcessId = processId;
    }

    public Object getCompletedDate() {
        return CompletedDate;
    }

    public void setCompletedDate(Object completedDate) {
        CompletedDate = completedDate;
    }

    public String getStartedBy() {
        return StartedBy;
    }

    public void setStartedBy(String startedBy) {
        StartedBy = startedBy;
    }

    public Object getReferenceId() {
        return ReferenceId;
    }

    public void setReferenceId(Object referenceId) {
        ReferenceId = referenceId;
    }
}
