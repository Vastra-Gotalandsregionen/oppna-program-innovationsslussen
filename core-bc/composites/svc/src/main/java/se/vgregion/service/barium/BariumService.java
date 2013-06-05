package se.vgregion.service.barium;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstance;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstances;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectField;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrik Bergström
 */
@Service
public class BariumService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BariumService.class.getName());

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
    }

    public BariumService(BariumRestClient bariumRestClient) {
        this.bariumRestClient = bariumRestClient;
    }

    @PostConstruct
    public void init() {
        try {
        	
        	System.out.println("BariumService - init - apiLocation has value: " + apiLocation);
        	
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
                
                if(ideaObjectFieldsList != null) {
                    IdeaObjectFields ideaObjectFields = new IdeaObjectFields();
                    ideaObjectFields.populate(ideaObjectFieldsList);

                    ideas.add(ideaObjectFields);
                }

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
    
    public String createIdea(Idea idea) {
    	String bariumId = "";
    	
        IdeaObjectFields ideaObjectFields = new IdeaObjectFields();
        
        ideaObjectFields.setBehov(idea.getSolvesProblem());     
        ideaObjectFields.setEpost(idea.getEmail());
        ideaObjectFields.setIde(idea.getDescription());
        ideaObjectFields.setInstanceName(idea.getTitle());
        ideaObjectFields.setKommavidare(idea.getWantsHelpWith());       
        ideaObjectFields.setTelefonnummer(idea.getPhone());
        
        ideaObjectFields.setVgrId(idea.getVgrId());
        ideaObjectFields.setVgrIdFullname(idea.getName());
        ideaObjectFields.setVgrIdTitel(idea.getJobPosition());
        
        String replyJson = bariumRestClient.createIdeaInstance(ideaObjectFields);
        
        try {
			JSONObject jsonObject = new JSONObject(replyJson);
			
			bariumId = jsonObject.getString("InstanceId");
			
			System.out.println("BariumService - createIdea - InstanceId: " + bariumId);
			
		} catch (JSONException e) {
			LOGGER.error(e.getMessage(), e);
		}

        return bariumId;
    }
    
    
    public String createIdea(IdeaObjectFields ideaObjectFields) {
        bariumRestClient.createIdeaInstance(ideaObjectFields);
        return "";
    }    
    
    
}
