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
