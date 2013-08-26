package se.vgregion.service.barium;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
import se.vgregion.portal.innovationsslussen.domain.json.*;

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

    private ExecutorService executor = Executors.newCachedThreadPool();

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
    
    public BariumResponse deleteBariumIdea(String bariumId) {

    	BariumResponse bariumResponse = new BariumResponse();
    	
//    	System.out.println("BariumService - deleteBariumIdea - bariumId is: " + bariumId + " and currently bariumResponse.getSuccess is: " + bariumResponse.getSuccess());
    	
        try {
        	String replyJson = bariumRestClient.deleteBariumInstance(bariumId);
        	
        	
            try {
    			JSONObject jsonObject = new JSONObject(replyJson);
    			
    			System.out.println("BariumService - deleteBariumIdea - jsonObject: " + jsonObject.toString());
    			
    			boolean success = jsonObject.getBoolean("success");
    			
    			bariumResponse.setSuccess(success);
    			bariumResponse.setJsonString(replyJson);
    			
    		} catch (JSONException e) {
    			LOGGER.error(e.getMessage(), e);
    		}

        } catch (BariumException e) {
            throw new RuntimeException(e);
        }
    	
//    	System.out.println("BariumService - deleteBariumIdea - bariumResponse.getSuccess before return is: " + bariumResponse.getSuccess());

        return bariumResponse;
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
    
    public IdeaObjectFields getBariumIdea(String bariumId) {

        IdeaObjectFields bariumIdea = null;
        try {
            BariumInstance bariumInstance = bariumRestClient.getBariumInstance(bariumId);

            List<ObjectField> ideaObjectFieldsList = null;

            if (bariumInstance != null) {
                ideaObjectFieldsList = bariumRestClient.getIdeaObjectFields(bariumInstance.getId());
            }

            if (ideaObjectFieldsList != null) {
                bariumIdea = new IdeaObjectFields();
                bariumIdea.populate(ideaObjectFieldsList);
            }

        } catch (BariumException e) {
            throw new RuntimeException(e);
        }

        return bariumIdea;
    }

    public BariumResponse createIdea(Idea idea) {
    	BariumResponse bariumResponse = new BariumResponse();
    	
        IdeaObjectFields ideaObjectFields = new IdeaObjectFields();
        
        IdeaContent ideaContentPublic = idea.getIdeaContentPublic();
        IdeaContent ideaContentPrivate = idea.getIdeaContentPrivate();
        IdeaPerson ideaPerson = idea.getIdeaPerson();
        
        String ideaSiteLink = idea.getIdeaSiteLink();
        
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

    public void uploadFile(Idea idea, String folderName, String fileName, InputStream inputStream) throws BariumException {
        bariumRestClient.uploadFile(idea.getId(), folderName, fileName, inputStream);
    }

    public void uploadFile(Idea idea, String fileName, InputStream inputStream) throws BariumException {
        bariumRestClient.uploadFile(idea.getId(), fileName, inputStream);
    }

    private List<ObjectEntry> getIdeaFiles(String objectId) throws BariumException {
        Objects instanceObjects = bariumRestClient.getObjectObjects(objectId);

        SortedSet<ObjectEntry > objectEntries = new TreeSet<ObjectEntry>(new Comparator<ObjectEntry>() {
            @Override
            public int compare(ObjectEntry o1, ObjectEntry o2) {
                try {
                    if (!o1.getName().equals(o2.getName())) {
                        return o1.getName().compareTo(o2.getName());
                    } else {
                        return o1.getId().compareTo(o2.getId());
                    }
                } catch (RuntimeException e) {
                    return o1.hashCode() > o2.hashCode() ? 1 : -1;
                }
            }
        });

        for (ObjectEntry instanceObject : instanceObjects.getData()) {
            if (instanceObject.getType() != null && instanceObject.getType().equals("file")) {
                objectEntries.add(instanceObject);
            }
        }

        return new ArrayList<ObjectEntry>(objectEntries);
    }

    public Future<IdeaObjectFields> asyncGetIdeaObjectFields(final String ideaId) {
        return executor.submit(new Callable<IdeaObjectFields>() {
            @Override
            public IdeaObjectFields call() throws Exception {
                return getBariumIdea(ideaId);
            }
        });
    }

    public Future<String> asyncGetIdeaPhaseFuture(final String ideaId) {
        return executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getIdeaState(ideaId);
            }
        });
    }

    public String getIdeaState(String ideaId) {
        return bariumRestClient.getIdeaState(ideaId);
    }

    public List<ObjectEntry> getIdeaFiles(Idea idea, String folderName) throws BariumException {
        String folderId = bariumRestClient.findFolder(idea.getId(), folderName);

        return getIdeaFiles(folderId);
    }

    public ObjectEntry getObject(String id) throws BariumException {
        return bariumRestClient.getObject(id);
    }

    public InputStream downloadFile(String id) throws BariumException {
        return bariumRestClient.doGetFileStream(id);
    }

    public void updateIdea(String id, String field, String value) throws BariumException {
        bariumRestClient.updateField(id, field, value);
    }
}
