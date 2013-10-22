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