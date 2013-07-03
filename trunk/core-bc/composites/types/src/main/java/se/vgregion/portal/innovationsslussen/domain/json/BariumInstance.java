package se.vgregion.portal.innovationsslussen.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

public class BariumInstance {
	
	@JsonProperty("Id") private String Id;
	@JsonProperty("ReferenceId") private Object ReferenceId;
	@JsonProperty("Name") private String Name;
	@JsonProperty("Description") private String Description;
	@JsonProperty("Status") private String Status;
	@JsonProperty("State") private Object State;
	@JsonProperty("StartDate") private String StartDate;
	@JsonProperty("StartedBy") private String StartedBy;
	@JsonProperty("StartedByUserId") private String StartedByUserId;
	@JsonProperty("CompletedDate") private Object CompletedDate;
	@JsonProperty("Priority") private int Priority;
	@JsonProperty("PlannedDate") private Object PlannedDate;
	@JsonProperty("DeadlineDate") private Object DeadlineDate;
	@JsonProperty("ApplicationId") private String ApplicationId;
	@JsonProperty("ApplicationName") private String ApplicationName;
	@JsonProperty("ParentInstanceName") private String ParentInstanceName;
	@JsonProperty("ParentInstanceId") private String ParentInstanceId;
	@JsonProperty("ProcessId") private String ProcessId;
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public Object getReferenceId() {
		return ReferenceId;
	}
	public void setReferenceId(Object referenceId) {
		ReferenceId = referenceId;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public Object getState() {
		return State;
	}
	public void setState(Object state) {
		State = state;
	}
	public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public String getStartedBy() {
		return StartedBy;
	}
	public void setStartedBy(String startedBy) {
		StartedBy = startedBy;
	}
	public String getStartedByUserId() {
		return StartedByUserId;
	}
	public void setStartedByUserId(String startedByUserId) {
		StartedByUserId = startedByUserId;
	}
	public Object getCompletedDate() {
		return CompletedDate;
	}
	public void setCompletedDate(Object completedDate) {
		CompletedDate = completedDate;
	}
	public int getPriority() {
		return Priority;
	}
	public void setPriority(int priority) {
		Priority = priority;
	}
	public Object getPlannedDate() {
		return PlannedDate;
	}
	public void setPlannedDate(Object plannedDate) {
		PlannedDate = plannedDate;
	}
	public Object getDeadlineDate() {
		return DeadlineDate;
	}
	public void setDeadlineDate(Object deadlineDate) {
		DeadlineDate = deadlineDate;
	}
	public String getApplicationId() {
		return ApplicationId;
	}
	public void setApplicationId(String applicationId) {
		ApplicationId = applicationId;
	}
	public String getApplicationName() {
		return ApplicationName;
	}
	public void setApplicationName(String applicationName) {
		ApplicationName = applicationName;
	}
	public String getParentInstanceName() {
		return ParentInstanceName;
	}
	public void setParentInstanceName(String parentInstanceName) {
		ParentInstanceName = parentInstanceName;
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

}
