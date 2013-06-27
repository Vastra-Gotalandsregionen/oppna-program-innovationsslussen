package se.vgregion.service.barium;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import se.vgregion.portal.innovationsslussen.domain.BariumResponse;
import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstance;
import se.vgregion.portal.innovationsslussen.domain.json.ApplicationInstances;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectField;

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

    public BariumResponse createIdea(Idea idea) {
    	BariumResponse bariumResponse = new BariumResponse();
    	
        IdeaObjectFields ideaObjectFields = new IdeaObjectFields();
        
        IdeaContent ideaContentPublic = idea.getIdeaContentPublic();
        IdeaContent ideaContentPrivate = idea.getIdeaContentPrivate();
        IdeaPerson ideaPerson = idea.getIdeaPerson();
        
        // Todo - get real link to Liferay Page
        String ideaSiteLink = "";
        
        String solvesProblem = ideaContentPrivate.getSolvesProblem();
        String email = ideaPerson.getEmail();
        String description = ideaContentPrivate.getDescription();
        String ideaTested = ideaContentPrivate.getIdeaTested();
        String title = idea.getTitle();
        String wantsHelpWith = ideaContentPrivate.getWantsHelpWith();
        
        String additonalPersonsInfo = ideaPerson.getAdditionalPersonsInfo();
        String phone = ideaPerson.getPhone();
        String phoneMobile = ideaPerson.getPhoneMobile();
        String vgrId = ideaPerson.getVgrId();
        String name = ideaPerson.getName();
        String jobPosition = ideaPerson.getJobPosition();
        
        System.out.println("BariumService - createIdea - ideaTested has value: " + ideaTested);
        
        ideaObjectFields.setBehov(solvesProblem);     
        ideaObjectFields.setEpost(email);
        ideaObjectFields.setIde(description);
        ideaObjectFields.setInstanceName(title);
        ideaObjectFields.setKomplnamn(additonalPersonsInfo);
        ideaObjectFields.setKommavidare(wantsHelpWith);
        ideaObjectFields.setSiteLank(ideaSiteLink);
        ideaObjectFields.setTelefonnummer(phone);
        ideaObjectFields.setTelefonnummerMobil(phoneMobile);
        ideaObjectFields.setTestat(ideaTested);
        
        
        ideaObjectFields.setVgrId(vgrId);
        ideaObjectFields.setVgrIdFullname(name);
        ideaObjectFields.setVgrIdTitel(jobPosition);
        
        String replyJson = bariumRestClient.createIdeaInstance(ideaObjectFields);
        
        try {
			JSONObject jsonObject = new JSONObject(replyJson);
			
			System.out.println("BariumService - createIdea - jsonObject: " + jsonObject.toString());
			
			String instanceId = jsonObject.getString("InstanceId");
			boolean success = jsonObject.getBoolean("success");
			
			bariumResponse.setInstanceId(instanceId);
			bariumResponse.setSuccess(success);
			bariumResponse.setJsonString(replyJson);
			
			System.out.println("BariumService - createIdea - InstanceId: " + instanceId);
			
		} catch (JSONException e) {
			LOGGER.error(e.getMessage(), e);
		}

        return bariumResponse;
    }
    
    
    public String createIdea(IdeaObjectFields ideaObjectFields) {
        bariumRestClient.createIdeaInstance(ideaObjectFields);
        return "";
    }    
    
    
}