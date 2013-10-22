/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.portal.innovationsslussen.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

public class BariumInstance {

    @JsonProperty("Id") private String id;
    @JsonProperty("ReferenceId") private Object referenceId;
    @JsonProperty("Name") private String name;
    @JsonProperty("Description") private String description;
    @JsonProperty("Status") private String status;
    @JsonProperty("State") private Object state;
    @JsonProperty("StartDate") private String startDate;
    @JsonProperty("StartedBy") private String startedBy;
    @JsonProperty("StartedByUserId") private String startedByUserId;
    @JsonProperty("CompletedDate") private Object completedDate;
    @JsonProperty("Priority") private int priority;
    @JsonProperty("PlannedDate") private Object plannedDate;
    @JsonProperty("DeadlineDate") private Object deadlineDate;
    @JsonProperty("ApplicationId") private String applicationId;
    @JsonProperty("ApplicationName") private String applicationName;
    @JsonProperty("ParentInstanceName") private String parentInstanceName;
    @JsonProperty("ParentInstanceId") private String parentInstanceId;
    @JsonProperty("ProcessId") private String processId;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Object getReferenceId() {
        return referenceId;
    }
    public void setReferenceId(Object referenceId) {
        this.referenceId = referenceId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Object getState() {
        return state;
    }
    public void setState(Object state) {
        this.state = state;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getStartedBy() {
        return startedBy;
    }
    public void setStartedBy(String startedBy) {
        this.startedBy = startedBy;
    }
    public String getStartedByUserId() {
        return this.startedByUserId;
    }
    public void setStartedByUserId(String startedByUserId) {
        this.startedByUserId = startedByUserId;
    }
    public Object getCompletedDate() {
        return completedDate;
    }
    public void setCompletedDate(Object completedDate) {
        this.completedDate = completedDate;
    }
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public Object getPlannedDate() {
        return plannedDate;
    }
    public void setPlannedDate(Object plannedDate) {
        this.plannedDate = plannedDate;
    }
    public Object getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(Object deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    public String getApplicationId() {
        return applicationId;
    }
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    public String getApplicationName() {
        return applicationName;
    }
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
    public String getParentInstanceName() {
        return parentInstanceName;
    }
    public void setParentInstanceName(String parentInstanceName) {
        this.parentInstanceName = parentInstanceName;
    }
    public String getParentInstanceId() {
        return parentInstanceId;
    }
    public void setParentInstanceId(String parentInstanceId) {
        this.parentInstanceId = parentInstanceId;
    }
    public String getProcessId() {
        return processId;
    }
    public void setProcessId(String processId) {
        this.processId = processId;
    }

}
