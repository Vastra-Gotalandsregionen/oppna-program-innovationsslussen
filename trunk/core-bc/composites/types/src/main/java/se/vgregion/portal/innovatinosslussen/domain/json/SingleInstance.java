package se.vgregion.portal.innovatinosslussen.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

public class SingleInstance {
	@JsonProperty("ApplicationName") private String ApplicationName;
	@JsonProperty("PlannedDate") private String PlannedDate;
	@JsonProperty("Description") private String Description;
	@JsonProperty("ParentInstanceId") private String ParentInstanceId;
	@JsonProperty("ProcessId") private String ProcessId;
	@JsonProperty("StartedBy") private String StartedBy;
	@JsonProperty("ApplicationId") private String ApplicationId;
	@JsonProperty("Priority") private Integer Priority;
	@JsonProperty("Name") private String Name;
	@JsonProperty("Status") private String Status;
	@JsonProperty("DeadlineDate") private String DeadlineDate;
	@JsonProperty("State") private String State;
	@JsonProperty("StartDate") private String StartDate;
	@JsonProperty("ParentInstanceName") private String ParentInstanceName;
	@JsonProperty("StartedByUserId") private String StartedByUserId;
	@JsonProperty("Id") private String Id;
	@JsonProperty("CompletedDate") private String CompletedDate;
	@JsonProperty("ReferenceId") private String ReferenceId;

    public String getApplicationName() {
        return ApplicationName;
    }

    public void setApplicationName(String applicationName) {
        ApplicationName = applicationName;
    }

    public String getPlannedDate() {
        return PlannedDate;
    }

    public void setPlannedDate(String plannedDate) {
        PlannedDate = plannedDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getParentInstanceId() {
        return ParentInstanceId;
    }

    public void setParentInstanceId(String parentInstanceId) {
        ParentInstanceId = parentInstanceId;
    }

    public String getProcessId() {
        return ProcessId;
    }

    public void setProcessId(String processId) {
        ProcessId = processId;
    }

    public String getStartedBy() {
        return StartedBy;
    }

    public void setStartedBy(String startedBy) {
        StartedBy = startedBy;
    }

    public String getApplicationId() {
        return ApplicationId;
    }

    public void setApplicationId(String applicationId) {
        ApplicationId = applicationId;
    }

    public Integer getPriority() {
        return Priority;
    }

    public void setPriority(Integer priority) {
        Priority = priority;
    }

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

    public String getDeadlineDate() {
        return DeadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        DeadlineDate = deadlineDate;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getParentInstanceName() {
        return ParentInstanceName;
    }

    public void setParentInstanceName(String parentInstanceName) {
        ParentInstanceName = parentInstanceName;
    }

    public String getStartedByUserId() {
        return StartedByUserId;
    }

    public void setStartedByUserId(String startedByUserId) {
        StartedByUserId = startedByUserId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCompletedDate() {
        return CompletedDate;
    }

    public void setCompletedDate(String completedDate) {
        CompletedDate = completedDate;
    }

    public String getReferenceId() {
        return ReferenceId;
    }

    public void setReferenceId(String referenceId) {
        ReferenceId = referenceId;
    }
}
