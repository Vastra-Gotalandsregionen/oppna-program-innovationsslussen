package se.vgregion.service.barium;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstance;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstances;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrik Bergström
 */
@Service
public class BariumService {

    @Value("${apiLocation}")
    private String apiLocation;
    @Value("${apiKey}")
    private String apiKey;
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;
    @Value("${applicationId}")
    private String applicationId;

    private BariumRestClient bariumRestClient;

    public BariumService() {
        bariumRestClient = new BariumRestClientImpl();
        try {
            bariumRestClient.connect(apiLocation, apiKey, username, password, applicationId);
        } catch (BariumException e) {
            throw new RuntimeException(e);
        }
    }

    public BariumService(BariumRestClientImpl bariumRestClient) {
        this.bariumRestClient = bariumRestClient;
        try {
            bariumRestClient.connect(apiLocation, apiKey, username, password, applicationId);
        } catch (BariumException e) {
            throw new RuntimeException(e);
        }
    }

    public List<IdeaObjectFields> getAllIdeas() {

        List<IdeaObjectFields> ideas = new ArrayList<IdeaObjectFields>();
        try {
            ApplicationInstances applicationInstances = bariumRestClient.getApplicationInstances();
            List<ApplicationInstance> data = applicationInstances.getData();

            for (ApplicationInstance applicationInstance : data) {
                List<ObjectField> ideaObjectFieldsList = bariumRestClient.getIdeaObjectFields(applicationInstance);

                IdeaObjectFields ideaObjectFields = new IdeaObjectFields();
                ideaObjectFields.populate(ideaObjectFieldsList);

                ideas.add(ideaObjectFields);
            }
        } catch (BariumException e) {
            throw new RuntimeException(e);
        }

        return ideas;
    }

    public String createIdea() {
        IdeaObjectFields ideaObjectFields = new IdeaObjectFields();
        ideaObjectFields.setIde("en lysande idé");
        bariumRestClient.createIdeaInstance(ideaObjectFields);

        return "";
    }
}
