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

package se.vgregion.service.barium;

import java.io.InputStream;
import java.util.List;

import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstance;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstances;
import se.vgregion.portal.innovationsslussen.domain.json.BariumInstance;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectEntry;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectField;
import se.vgregion.portal.innovationsslussen.domain.json.Objects;

/**
 * The Interface BariumRestClient.
 */
public interface BariumRestClient {

    /**
     * Connect.
     *
     * @return true, if successful
     * @throws BariumException the barium exception
     */
    boolean connect() throws BariumException;

    /**
     * Delete barium instance.
     *
     * @param instanceId the instance id
     * @return the string
     * @throws BariumException the barium exception
     */
    String deleteBariumInstance(String instanceId)
            throws BariumException;

    /**
     * Gets the application instances.
     *
     * @return the application instances
     * @throws BariumException the barium exception
     */
    ApplicationInstances getApplicationInstances()
            throws BariumException;

    /**
     * Gets the barium instance.
     *
     * @param instanceId the instance id
     * @return the barium instance
     * @throws BariumException the barium exception
     */
    BariumInstance getBariumInstance(String instanceId)
            throws BariumException;


    /**
     * Update instance.
     *
     * @param values the values
     * @param objectId the object id
     * @return true, if successful
     */
    boolean updateInstance(String values, String objectId);

    /**
     * Gets the instance objects.
     *
     * @param instanceId the instance id
     * @return the instance objects
     * @throws BariumException the barium exception
     */
    Objects getInstanceObjects(String instanceId) throws BariumException;

    /**
     * Gets the object objects.
     *
     * @param objectId the object id
     * @return the object objects
     * @throws BariumException the barium exception
     */
    Objects getObjectObjects(String objectId) throws BariumException;

    /**
     * Creates the idea instance.
     *
     * @param ideaObjectFields the idea object fields
     * @return the string
     */
    String createIdeaInstance(IdeaObjectFields ideaObjectFields);

    /**
     * Gets the idea object fields.
     *
     * @param applicationInstance the application instance
     * @return the idea object fields
     */
    List<ObjectField> getIdeaObjectFields(
            ApplicationInstance applicationInstance);

    /**
     * Gets the idea object fields.
     *
     * @param instanceId the instance id
     * @return the idea object fields
     */
    List<ObjectField> getIdeaObjectFields(String instanceId);

    /**
     * Upload file.
     *
     * @param instanceId the instance id
     * @param fileName the file name
     * @param inputStream the input stream
     * @throws BariumException the barium exception
     */
    void uploadFile(String instanceId, String fileName, InputStream inputStream) throws BariumException;

    /**
     * Gets the object.
     *
     * @param id the id
     * @return the object
     * @throws BariumException the barium exception
     */
    ObjectEntry getObject(String id) throws BariumException;

    /**
     * Do get file stream.
     *
     * @param objectId the object id
     * @return the input stream
     * @throws BariumException the barium exception
     */
    InputStream doGetFileStream(String objectId) throws BariumException;

    /**
     * Update field.
     *
     * @param instanceId the instance id
     * @param field the field
     * @param value the value
     * @return the string
     * @throws BariumException the barium exception
     */
    String updateField(String instanceId, String field, String value) throws BariumException;

    /**
     * Find folder.
     *
     * @param instanceId the instance id
     * @param folderName the folder name
     * @return the string
     * @throws BariumException the barium exception
     */
    String findFolder(String instanceId, String folderName) throws BariumException;

    /**
     * Upload file.
     *
     *
     * @param instanceId the instance id
     * @param folderName the folder name
     * @param fileName the file name
     * @param inputStream the input stream
     * @throws BariumException the barium exception
     */
    String uploadFile(String instanceId, String folderName, String fileName, InputStream inputStream)
            throws BariumException;

    /**
     * Gets the idea state.
     *
     * @param instanceId the instance id
     * @return the idea state
     */
    String getIdeaState(String instanceId);

    /**
     * Shutdown the threadpool.
     */
    void shutdown();
}