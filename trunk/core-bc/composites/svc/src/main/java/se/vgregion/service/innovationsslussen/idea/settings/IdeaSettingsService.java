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
