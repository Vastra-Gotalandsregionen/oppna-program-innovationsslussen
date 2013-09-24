package se.vgregion.portal.innovationsslussen.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

public class ObjectEntry {
    @JsonProperty("ReadOnly") private Boolean readOnly;
    @JsonProperty("Description") private String description;
    @JsonProperty("Type") private String type;
    @JsonProperty("SortIndex") private Object sortIndex;
    @JsonProperty("DataId") private String dataId;
    @JsonProperty("TemplateId") private String templateId;
    @JsonProperty("Name") private String name;
    @JsonProperty("State") private String state;
    @JsonProperty("UpdatedDate") private String updatedDate;
    @JsonProperty("FileType") private String fileType;
    @JsonProperty("CreatedDate") private String createdDate;
    @JsonProperty("ObjectClass") private String objectClass;
    @JsonProperty("Id") private String id;
    @JsonProperty("TypeNamespace") private Object typeNamespace;
    @JsonProperty("ReferenceId") private Object referenceId;
    @JsonProperty("Container") private Boolean container;
    @JsonProperty("ParentId") private String parentId;

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Object sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getDataId() {
        return this.dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String objectClass) {
        this.objectClass = objectClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getTypeNamespace() {
        return typeNamespace;
    }

    public void setTypeNamespace(Object typeNamespace) {
        this.typeNamespace = typeNamespace;
    }

    public Object getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Object referenceId) {
        this.referenceId = referenceId;
    }

    public Boolean getContainer() {
        return container;
    }

    public void setContainer(Boolean container) {
        this.container = container;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
