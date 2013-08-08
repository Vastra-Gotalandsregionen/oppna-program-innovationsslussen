package se.vgregion.service.barium;

import java.io.InputStream;
import java.util.List;

import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.json.*;

public interface BariumRestClient {

	boolean connect() throws BariumException;

	ApplicationInstances getApplicationInstances()
			throws BariumException;
	
	BariumInstance getBariumInstance(String instanceId)
			throws BariumException;
	

	boolean updateInstance(String values, String objectId);

	Objects getInstanceObjects(String instanceId)
			throws BariumException;

	String createIdeaInstance(IdeaObjectFields ideaObjectFields);

	boolean connect(String apiLocation, String apiKey, String username,
			String password, String applicationId) throws BariumException;

	List<ObjectField> getIdeaObjectFields(
			ApplicationInstance applicationInstance);

	List<ObjectField> getIdeaObjectFields(String instanceId);

    void uploadFile(String instanceId, String fileName, InputStream inputStream) throws BariumException;

    ObjectEntry getObject(String id);

    InputStream doGetFileStream(String objectId) throws BariumException;

    String updateField(String instanceId, String field, String value) throws BariumException;
}