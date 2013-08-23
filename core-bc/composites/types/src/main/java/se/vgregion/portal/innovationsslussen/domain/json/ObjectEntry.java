package se.vgregion.portal.innovationsslussen.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

public class ObjectEntry {
	@JsonProperty("ReadOnly") private Boolean ReadOnly;
	@JsonProperty("Description") private String Description;
	@JsonProperty("Type") private String Type;
	@JsonProperty("SortIndex") private Object SortIndex;
	@JsonProperty("DataId") private String DataId;
	@JsonProperty("TemplateId") private String TemplateId;
	@JsonProperty("Name") private String Name;
	@JsonProperty("State") private String State;
	@JsonProperty("UpdatedDate") private String UpdatedDate;
	@JsonProperty("FileType") private String FileType;
	@JsonProperty("CreatedDate") private String CreatedDate;
	@JsonProperty("ObjectClass") private String ObjectClass;
	@JsonProperty("Id") private String Id;
	@JsonProperty("TypeNamespace") private Object TypeNamespace;
	@JsonProperty("ReferenceId") private Object ReferenceId;
	@JsonProperty("Container") private Boolean Container;
	@JsonProperty("ParentId") private String ParentId;

    public Boolean getReadOnly() {
        return ReadOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        ReadOnly = readOnly;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Object getSortIndex() {
        return SortIndex;
    }

    public void setSortIndex(Object sortIndex) {
        SortIndex = sortIndex;
    }

    public String getDataId() {
        return DataId;
    }

    public void setDataId(String dataId) {
        DataId = dataId;
    }

    public String getTemplateId() {
        return TemplateId;
    }

    public void setTemplateId(String templateId) {
        TemplateId = templateId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getFileType() {
        return FileType;
    }

    public void setFileType(String fileType) {
        FileType = fileType;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getObjectClass() {
        return ObjectClass;
    }

    public void setObjectClass(String objectClass) {
        ObjectClass = objectClass;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Object getTypeNamespace() {
        return TypeNamespace;
    }

    public void setTypeNamespace(Object typeNamespace) {
        TypeNamespace = typeNamespace;
    }

    public Object getReferenceId() {
        return ReferenceId;
    }

    public void setReferenceId(Object referenceId) {
        ReferenceId = referenceId;
    }

    public Boolean getContainer() {
        return Container;
    }

    public void setContainer(Boolean container) {
        Container = container;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        ParentId = parentId;
    }
}
