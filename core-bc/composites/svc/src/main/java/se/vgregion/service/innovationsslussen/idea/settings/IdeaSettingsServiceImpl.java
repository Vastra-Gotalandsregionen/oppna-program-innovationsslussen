package se.vgregion.service.innovationsslussen.idea.settings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.service.ExpandoColumnLocalService;
import com.liferay.portlet.expando.service.ExpandoTableLocalService;
import com.liferay.portlet.expando.service.ExpandoValueLocalService;

/**
 * Implementation of {@link IdeaSettingsService}.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class IdeaSettingsServiceImpl implements IdeaSettingsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaSettingsServiceImpl.class);

    private static final String GROUP_CLASSNAME = Group.class.getName();


    @Autowired
    private ExpandoColumnLocalService expandoColumnService;

    @Autowired
    private ExpandoTableLocalService expandoTableService;

    @Autowired
    private ExpandoValueLocalService expandoValueService;

    @Override
    public void setSetting(String data, String columnName, long companyId, long groupId) {
        try {
            expandoValueService.addValue(companyId, GROUP_CLASSNAME, ExpandoTableConstants.DEFAULT_TABLE_NAME,
                    columnName, groupId, data);
        } catch (PortalException e) {
            if (e instanceof com.liferay.portlet.expando.NoSuchTableException) {
                // If table don't exists we try to create it.

                try {
                    expandoTableService.addDefaultTable(companyId, GROUP_CLASSNAME);
                    setSetting(data, columnName, companyId, groupId);
                } catch (PortalException e1) {
                    e1.printStackTrace();
                } catch (SystemException e1) {
                    e1.printStackTrace();
                }
            } else if (e instanceof com.liferay.portlet.expando.NoSuchColumnException) {
                // If column don't exists we try to create it.

                try {
                    long tableId = expandoTableService.getDefaultTable(companyId, GROUP_CLASSNAME).getTableId();
                    expandoColumnService.addColumn(tableId, columnName, ExpandoColumnConstants.STRING);
                    setSetting(data, columnName, companyId, groupId);
                } catch (PortalException e2) {
                    e2.printStackTrace();
                } catch (SystemException e2) {
                    e2.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSettingBoolean(boolean data, String columnName, long companyId, long groupId) {

        try {
            expandoValueService.addValue(companyId, GROUP_CLASSNAME, ExpandoTableConstants.DEFAULT_TABLE_NAME,
                    columnName, groupId, data);

        } catch (PortalException e) {
            if (e instanceof com.liferay.portlet.expando.NoSuchTableException) {
                // If table don't exists we try to create it

                try {
                    expandoTableService.addDefaultTable(companyId, GROUP_CLASSNAME);
                    setSettingBoolean(data, columnName, companyId, groupId);
                } catch (PortalException e1) {
                    e1.printStackTrace();
                } catch (SystemException e1) {
                    e1.printStackTrace();
                }

            } else if (e instanceof com.liferay.portlet.expando.NoSuchColumnException) {
                // If column don't exists we try to create it

                try {
                    long tableId = expandoTableService.getDefaultTable(companyId, GROUP_CLASSNAME).getTableId();
                    expandoColumnService.addColumn(tableId, columnName, ExpandoColumnConstants.BOOLEAN);
                    setSettingBoolean(data, columnName, companyId, groupId);
                } catch (PortalException e2) {
                    e2.printStackTrace();
                } catch (SystemException e2) {
                    e2.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        } catch (SystemException e) {
            e.printStackTrace();
        }

    }


    @Override
    public String getSetting(String columnName, long companyId, long groupId) {

        String value = "";
        try {

            value = expandoValueService.getData(companyId, GROUP_CLASSNAME,
                    ExpandoTableConstants.DEFAULT_TABLE_NAME, columnName, groupId, "");
        } catch (PortalException e) {
            if (e instanceof com.liferay.portlet.expando.NoSuchTableException) { // If table don't exists we try to
                try {
                    expandoTableService.addDefaultTable(companyId, GROUP_CLASSNAME);
                    getSetting(columnName, companyId, groupId);
                } catch (PortalException e1) {
                    e1.printStackTrace();
                } catch (SystemException e1) {
                    e1.printStackTrace();
                }
            } else if (e instanceof com.liferay.portlet.expando.NoSuchColumnException) { // If column don't exists
                // we try to create it.
                try {

                    long tableId = expandoTableService.getDefaultTable(companyId, GROUP_CLASSNAME).getTableId();
                    expandoColumnService.addColumn(tableId, columnName, ExpandoColumnConstants.STRING);
                    getSetting(columnName, companyId, groupId);
                } catch (PortalException e2) {
                    e2.printStackTrace();
                } catch (SystemException e2) {
                    e2.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        } catch (SystemException e) {
            e.printStackTrace();
        }

        return String.valueOf(value);
    }

    @Override
    public boolean getSettingBoolean(String columnName, long companyId, long groupId) {

        boolean value = false;

        try {

            value = expandoValueService.getData(companyId, GROUP_CLASSNAME,
                    ExpandoTableConstants.DEFAULT_TABLE_NAME, columnName, groupId, false);
        } catch (PortalException e) {
            if (e instanceof com.liferay.portlet.expando.NoSuchTableException) {
                // If table don't exists we try to create it

                try {
                    expandoTableService.addDefaultTable(companyId, GROUP_CLASSNAME);
                    getSettingBoolean(columnName, companyId, groupId);
                } catch (PortalException e1) {
                    e1.printStackTrace();
                } catch (SystemException e1) {
                    e1.printStackTrace();
                }

            } else if (e instanceof com.liferay.portlet.expando.NoSuchColumnException) {
                // If column don't exist we try to create it.

                try {

                    long tableId = expandoTableService.getDefaultTable(companyId, GROUP_CLASSNAME).getTableId();
                    expandoColumnService.addColumn(tableId, columnName, ExpandoColumnConstants.BOOLEAN);
                    getSettingBoolean(columnName, companyId, groupId);
                } catch (PortalException e2) {
                    e2.printStackTrace();
                } catch (SystemException e2) {
                    e2.printStackTrace();
                }

            } else {
                e.printStackTrace();
            }
        } catch (SystemException e) {
            e.printStackTrace();
        }

        return value;
    }




}
