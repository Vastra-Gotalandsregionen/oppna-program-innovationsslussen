package se.vgregion.portal.innovationsslussen.domain;

public class BariumResponse {

    private boolean success;
    private String instanceId;
    private String jsonString;

    /**
     * Instantiates a new barium response.
     */
    public BariumResponse() {
    }

    /**
     * Instantiates a new barium response.
     *
     * @param success the success
     * @param instanceId the instance id
     * @param jsonString the json string
     */
    public BariumResponse(boolean success, String instanceId, String jsonString) {
        this.success = success;
        this.instanceId = instanceId;
        this.jsonString = jsonString;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

}
