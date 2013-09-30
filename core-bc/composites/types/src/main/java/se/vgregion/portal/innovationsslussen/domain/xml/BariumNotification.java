package se.vgregion.portal.innovationsslussen.domain.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * JAXB entity denoting a Barium notification containing object elements mapping to the ideas which have been updated
 * on the Barium side.
 *
 * @author Patrik Bergström
 */
@XmlRootElement(name = "objects")
@XmlAccessorType(XmlAccessType.FIELD)
public class BariumNotification {

    @XmlElement(name = "object")
    private List<BariumNotificationObject> objects;

    public List<BariumNotificationObject> getObjects() {
        return objects;
    }

    public void setObjects(List<BariumNotificationObject> objects) {
        this.objects = objects;
    }

}