package se.vgregion.portal.innovationsslussen.domain.xml;

import javax.xml.bind.annotation.*;

/**
 * JAXB entity being part of {@link BariumNotification}.
 */
@XmlType(name = "object")
@XmlAccessorType(XmlAccessType.FIELD)
public class BariumNotificationObject {

    @XmlAttribute
    private String objectId;
    @XmlAttribute
    private String objectClassNamespace;
    @XmlAttribute
    private String instanceId;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectClassNamespace() {
        return objectClassNamespace;
    }

    public void setObjectClassNamespace(String objectClassNamespace) {
        this.objectClassNamespace = objectClassNamespace;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
