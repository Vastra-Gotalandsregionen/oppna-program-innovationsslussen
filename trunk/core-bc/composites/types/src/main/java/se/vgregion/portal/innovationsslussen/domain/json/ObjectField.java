package se.vgregion.portal.innovationsslussen.domain.json;

import org.codehaus.jackson.annotate.JsonProperty;

public class ObjectField {
	@JsonProperty("Name") private String Name;
	@JsonProperty("ReadOnly") private Boolean ReadOnly;
	@JsonProperty("AllowBlank") private Boolean AllowBlank;
	@JsonProperty("Value") private String Value;
	@JsonProperty("DataType") private String DataType;
	@JsonProperty("Index") private Integer Index;
	@JsonProperty("FieldType") private String FieldType;
	@JsonProperty("Id") private String Id;
	@JsonProperty("FieldTypeNamespace") private String FieldTypeNamespace;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Boolean getReadOnly() {
        return ReadOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        ReadOnly = readOnly;
    }

    public Boolean getAllowBlank() {
        return AllowBlank;
    }

    public void setAllowBlank(Boolean allowBlank) {
        AllowBlank = allowBlank;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getDataType() {
        return DataType;
    }

    public void setDataType(String dataType) {
        DataType = dataType;
    }

    public Integer getIndex() {
        return Index;
    }

    public void setIndex(Integer index) {
        Index = index;
    }

    public String getFieldType() {
        return FieldType;
    }

    public void setFieldType(String fieldType) {
        FieldType = fieldType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFieldTypeNamespace() {
        return FieldTypeNamespace;
    }

    public void setFieldTypeNamespace(String fieldTypeNamespace) {
        FieldTypeNamespace = fieldTypeNamespace;
    }
}
