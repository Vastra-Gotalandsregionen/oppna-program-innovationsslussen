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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import se.vgregion.portal.innovationsslussen.domain.BariumResponse;
import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;
import se.vgregion.portal.innovationsslussen.domain.json.*;

/**
 * A REST service for communicate with Barium.
 * 
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

    private final ExecutorService executor = Executors.newCachedThreadPool();

    private BariumRestClient bariumRestClient;


    @Value("${scheme.server.name.url}")
    private String schemeServerNameUrl;
    /**
     * Instantiates a new barium service.
     */
    public BariumService() {
        try {
            bariumRestClient = new BariumRestClientImpl(apiLocation, apiKey, username, password, applicationId);
        } catch (BariumException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Instantiates a new barium service.
     *
     * @param bariumRestClient the barium rest client
     */
    public BariumService(BariumRestClient bariumRestClient) {
        this.bariumRestClient = bariumRestClient;
    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        try {

            bariumRestClient.connect();
        } catch (BariumException e) {
            LOGGER.error("Innovationsplattformen - Could not connect to Barium.");
            //throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
        bariumRestClient.shutdown();
    }

    /**
     * Delete barium idea.
     *
     * @param bariumId the barium id
     * @return the barium response
     */
    public BariumResponse deleteBariumIdea(String bariumId) {

        BariumResponse bariumResponse = new BariumResponse();

        try {
            String replyJson = bariumRestClient.deleteBariumInstance(bariumId);


            try {
                JSONObject jsonObject = new JSONObject(replyJson);

                boolean success = jsonObject.getBoolean("success");

                bariumResponse.setSuccess(success);
                bariumResponse.setJsonString(replyJson);

            } catch (JSONException e) {
                LOGGER.error(e.getMessage(), e);
            }

        } catch (BariumException e) {
            throw new RuntimeException(e);
        }

        return bariumResponse;
    }

    /**
     * Gets the all ideas.
     *
     * @return the all ideas
     */
    public List<IdeaObjectFields> getAllIdeas() {

        List<IdeaObjectFields> ideas = new ArrayList<IdeaObjectFields>();
        try {
            ApplicationInstances applicationInstances = bariumRestClient.getApplicationInstances();
            List<ApplicationInstance> data = applicationInstances.getData();

            for (ApplicationInstance applicationInstance : data) {
                List<ObjectField> ideaObjectFieldsList = bariumRestClient.getIdeaObjectFields(applicationInstance);

                if (ideaObjectFieldsList != null) {
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

    /**
     * Gets the barium idea.
     *
     * @param bariumId the barium id
     * @return the barium idea
     */
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

    private String generateIdeaSiteLink(String urlTitle) {
        return schemeServerNameUrl + urlTitle;
    }

    /**
     * Creates the idea.
     *
     * @param idea the idea
     * @return the barium response
     */
    public BariumResponse createIdea(Idea idea) {
        BariumResponse bariumResponse = new BariumResponse();

        IdeaObjectFields ideaObjectFields = new IdeaObjectFields();

        IdeaContent ideaContentPrivate = idea.getIdeaContentPrivate();
        IdeaPerson ideaPerson = idea.getIdeaPerson();

        String ideaSiteLink = generateIdeaSiteLink(idea.getUrlTitle());

        String solvesProblem = ideaContentPrivate.getSolvesProblem();
        String email = ideaPerson.getEmail();
        String description = ideaContentPrivate.getDescription();
        String ideaTested = ideaContentPrivate.getIdeaTested();
        String title = idea.getTitle();
        String wantsHelpWith = ideaContentPrivate.getWantsHelpWith();

        String administrativeUnit = ideaPerson.getAdministrativeUnit();
        String phone = ideaPerson.getPhone();
        String phoneMobile = ideaPerson.getPhoneMobile();
        String vgrId = ideaPerson.getVgrId();
        String name = ideaPerson.getName();
        String jobPosition = ideaPerson.getJobPosition();
        String vgrStrukturPerson = ideaPerson.getVgrStrukturPerson();

        ideaObjectFields.setBehov(solvesProblem);
        ideaObjectFields.setEpost(email);
        ideaObjectFields.setFodelsear(ideaPerson.getBirthYear() + "");
        IdeaPerson.Gender gender = ideaPerson.getGender();
        if (gender != null && !gender.equals(IdeaPerson.Gender.UNKNOWN)) {
            ideaObjectFields.setKon((gender.equals(IdeaPerson.Gender.MALE) ? "Man" : "Kvinna"));
        }
        ideaObjectFields.setForvaltning(administrativeUnit);
        ideaObjectFields.setHsaIdKivEnhet(ideaPerson.getVgrStrukturPerson());
        ideaObjectFields.setVgrStrukturPerson(vgrStrukturPerson);

        ideaObjectFields.setIde(description);
        ideaObjectFields.setInstanceName(title);
        ideaObjectFields.setKommavidare(wantsHelpWith);
        ideaObjectFields.setMobiletelephonenumber(phoneMobile);
        ideaObjectFields.setSiteLank(ideaSiteLink);
        ideaObjectFields.setTelefonnummer(phone);

        ideaObjectFields.setVgrId(vgrId);
        ideaObjectFields.setVgrIdFullname(name);
        ideaObjectFields.setVgrIdTitel(jobPosition);

        String replyJson = bariumRestClient.createIdeaInstance(ideaObjectFields);

        try {
            JSONObject jsonObject = new JSONObject(replyJson);

            String instanceId = jsonObject.getString("InstanceId");
            boolean success = jsonObject.getBoolean("success");

            bariumResponse.setInstanceId(instanceId);
            bariumResponse.setSuccess(success);
            bariumResponse.setJsonString(replyJson);
        } catch (JSONException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return bariumResponse;
    }

    /**
     * Upload file.
     *
     * @param idea the idea
     * @param folderName the folder name
     * @param fileName the file name
     * @param inputStream the input stream
     * @throws BariumException the barium exception
     */
    public FileEntry uploadFile(Idea idea, String folderName, String fileName, InputStream inputStream)
            throws BariumException {

        FileEntry fileEntry = new FileEntry();

        String replyJson = bariumRestClient.uploadFile(idea.getId(), folderName, fileName, inputStream);

        //  {"success":true,"Items":[{"success":true,"Name":"index2.jpg",
        // "Id":"ada759d4-e49f-410e-a738-7c7baecedc10","ReferenceId":"ada759d4-e49f-410e-a738-7c7baecedc10"}]}

        try {
            JSONObject jsonObject = new JSONObject(replyJson);

            boolean success = jsonObject.getBoolean("success");

            if (success){
                JSONArray items = jsonObject.getJSONArray("Items");

                JSONObject fileEntryObject = items.getJSONObject(0);

                String id = fileEntryObject.getString("Id");
                String name = fileEntryObject.getString("Name");
                String referenceId = fileEntryObject.getString("ReferenceId");

                fileEntry.setId(id);
                fileEntry.setName(name);
                fileEntry.setReferenceId(referenceId);
            }

            fileEntry.setSuccess(success);

        } catch (JSONException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return fileEntry;
    }

    /**
     * Upload file.
     *
     * @param idea the idea
     * @param fileName the file name
     * @param inputStream the input stream
     * @throws BariumException the barium exception
     */
    public void uploadFile(Idea idea, String fileName, InputStream inputStream) throws BariumException {
        bariumRestClient.uploadFile(idea.getId(), fileName, inputStream);
    }

    private List<ObjectEntry> getIdeaFiles(String objectId) throws BariumException {
        Objects instanceObjects = bariumRestClient.getObjectObjects(objectId);

        SortedSet<ObjectEntry> objectEntries = new TreeSet<ObjectEntry>(new Comparator<ObjectEntry>() {
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

    /**
     * Async get idea object fields.
     *
     * @param ideaId the idea id
     * @return the future
     */
    public Future<IdeaObjectFields> asyncGetIdeaObjectFields(final String ideaId) {
        return executor.submit(new Callable<IdeaObjectFields>() {
            @Override
            public IdeaObjectFields call() throws Exception {
                return getBariumIdea(ideaId);
            }
        });
    }

    /**
     * Async get idea phase future.
     *
     * @param ideaId the idea id
     * @return the future
     */
    public Future<String> asyncGetIdeaPhaseFuture(final String ideaId) {
        return executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getIdeaState(ideaId);
            }
        });
    }

    /**
     * Gets the idea state.
     *
     * @param ideaId the idea id
     * @return the idea state
     */
    public String getIdeaState(String ideaId) {
        return bariumRestClient.getIdeaState(ideaId);
    }

    /**
     * Gets the idea files.
     *
     * @param idea the idea
     * @param folderName the folder name
     * @return the idea files
     * @throws BariumException the barium exception
     */
    public List<ObjectEntry> getIdeaFiles(Idea idea, String folderName) throws BariumException {
        String folderId = bariumRestClient.findFolder(idea.getId(), folderName);

        return getIdeaFiles(folderId);
    }

    /**
     * Gets the object.
     *
     * @param id the id
     * @return the object
     * @throws BariumException the barium exception
     */
    public ObjectEntry getObject(String id) throws BariumException {
        return bariumRestClient.getObject(id);
    }

    /**
     * Download file.
     *
     * @param id the id
     * @return the input stream
     * @throws BariumException the barium exception
     */
    public InputStream downloadFile(String id) throws BariumException {
        return bariumRestClient.doGetFileStream(id);
    }

    /**
     * Update idea.
     *
     * @param id the id
     * @param field the field
     * @param value the value
     * @throws BariumException the barium exception
     */
    public void updateIdea(String id, String field, String value) throws BariumException {
        bariumRestClient.updateField(id, field, value);
    }
}
