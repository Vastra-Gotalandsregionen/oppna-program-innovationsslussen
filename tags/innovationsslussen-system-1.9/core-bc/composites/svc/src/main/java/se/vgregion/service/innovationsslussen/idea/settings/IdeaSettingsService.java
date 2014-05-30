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

package se.vgregion.service.innovationsslussen.idea.settings;


/**
 * Service interface for managing {@link se.vgregion.portal.innovationsslussen.domain.jpa.Idea}s.
 *
 * @author Erik Andersson
 * @company Monator Technologies ABŒ
 */
public interface IdeaSettingsService {

    /**
     * Add an {@link IdeaUserFavorite}.
     *
     */
    //void addFavorite(long companyId, long groupId, long userId, String urlTitle);


    /**
     * Sets the setting.
     * 
     * @param data
     *            the data
     * @param columnName
     *            the column name
     * @param companyId
     *            the company id
     * @param groupId
     *            the group id
     */
    void setSetting(String data, String columnName, long companyId, long groupId);

    /**
     * Gets the setting.
     * 
     * @param columnName
     *            the column name
     * @param companyId
     *            the company id
     * @param groupId
     *            the group id
     * @return the setting
     */
    String getSetting(String columnName, long companyId, long groupId);

    /**
     * Sets the setting.
     * 
     * @param data
     *            the data
     * @param columnName
     *            the column name
     * @param companyId
     *            the company id
     * @param groupId
     *            the group id
     */
    void setSettingBoolean(boolean data, String columnName, long companyId, long groupId);

    /**
     * Gets the setting.
     * 
     * @param columnName
     *            the column name
     * @param companyId
     *            the company id
     * @param groupId
     *            the group id
     * @return the setting
     */
    boolean getSettingBoolean(String columnName, long companyId, long groupId);




}
