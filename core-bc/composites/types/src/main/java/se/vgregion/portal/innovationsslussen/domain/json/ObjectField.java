package se.vgregion.portal.innovationsslussen.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

public class ObjectField {
    @JsonProperty("Name") private String name;
    @JsonProperty("ReadOnly") private Boolean readOnly;
    @JsonProperty("AllowBlank") private Boolean allowBlank;
    @JsonProperty("Value") private String value;
    @JsonProperty("DataType") private String dataType;
    @JsonProperty("Index") private Integer index;
    @JsonProperty("FieldType") private String fieldType;
    @JsonProperty("Id") private String id;
    @JsonProperty("FieldTypeNamespace") private String fieldTypeNamespace;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Boolean getAllowBlank() {
        return allowBlank;
    }

    public void setAllowBlank(Boolean allowBlank) {
        this.allowBlank = allowBlank;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFieldTypeNamespace() {
        return fieldTypeNamespace;
    }

    public void setFieldTypeNamespace(String fieldTypeNamespace) {
        this.fieldTypeNamespace = fieldTypeNamespace;
    }
}
