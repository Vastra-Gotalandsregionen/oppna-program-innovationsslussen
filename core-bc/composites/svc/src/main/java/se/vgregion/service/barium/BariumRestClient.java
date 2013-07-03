package se.vgregion.service.barium;

import java.util.List;

import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstance;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstances;
import se.vgregion.portal.innovationsslussen.domain.json.BariumInstance;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectField;
import se.vgregion.portal.innovationsslussen.domain.json.Objects;

public interface BariumRestClient {

	boolean connect() throws BariumException;

	ApplicationInstances getApplicationInstances()
			throws BariumException;
	
	BariumInstance getBariumInstance(String instanceId)
			throws BariumException;
	

	boolean updateInstance(String values, String objectId);

	Objects getInstanceObjects(ApplicationInstance instance)
			throws BariumException;

	String createIdeaInstance(IdeaObjectFields ideaObjectFields);

	boolean connect(String apiLocation, String apiKey, String username,
			String password, String applicationId) throws BariumException;

	List<ObjectField> getIdeaObjectFields(
			ApplicationInstance applicationInstance);

	List<ObjectField> getIdeaObjectFields(
			BariumInstance bariumInstance);
	
}