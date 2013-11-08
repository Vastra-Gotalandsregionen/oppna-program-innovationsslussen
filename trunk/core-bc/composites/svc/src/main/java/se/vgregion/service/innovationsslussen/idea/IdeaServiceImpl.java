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

package se.vgregion.service.innovationsslussen.idea;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.collections.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import se.vgregion.portal.innovationsslussen.domain.BariumResponse;
import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.IdeaStatus;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaFile;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserFavorite;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserLike;
import se.vgregion.portal.innovationsslussen.domain.json.FileEntry;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectEntry;
import se.vgregion.portal.innovationsslussen.domain.vo.CommentItemVO;
import se.vgregion.service.barium.BariumException;
import se.vgregion.service.barium.BariumService;
import se.vgregion.service.innovationsslussen.exception.CreateIdeaException;
import se.vgregion.service.innovationsslussen.exception.FavoriteException;
import se.vgregion.service.innovationsslussen.exception.FileUploadException;
import se.vgregion.service.innovationsslussen.exception.LikeException;
import se.vgregion.service.innovationsslussen.exception.RemoveIdeaException;
import se.vgregion.service.innovationsslussen.idea.settings.IdeaSettingsService;
import se.vgregion.service.innovationsslussen.idea.settings.util.ExpandoConstants;
import se.vgregion.service.innovationsslussen.repository.idea.IdeaRepository;
import se.vgregion.service.innovationsslussen.repository.ideafile.IdeaFileRepository;
import se.vgregion.service.innovationsslussen.repository.ideauserfavorite.IdeaUserFavoriteRepository;
import se.vgregion.service.innovationsslussen.repository.ideauserlike.IdeaUserLikeRepository;
import se.vgregion.service.innovationsslussen.util.FriendlyURLNormalizer;
import se.vgregion.service.innovationsslussen.util.IdeaServiceConstants;
import se.vgregion.util.Util;


import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.model.ClassName;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.ResourcePermission;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.service.LayoutSetLocalService;
import com.liferay.portal.model.Role;
import com.liferay.portal.service.ClassNameLocalService;
import com.liferay.portal.service.ContactLocalService;
import com.liferay.portal.service.GroupLocalService;
import com.liferay.portal.service.ResourcePermissionLocalService;
import com.liferay.portal.service.RoleLocalService;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.service.AssetEntryLocalService;
import com.liferay.counter.service.CounterLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ResourceLocalService;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserGroupRoleLocalService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBMessageDisplay;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.service.MBMessageLocalService;
import com.liferay.portlet.messageboards.util.comparator.MessageCreateDateComparator;

/**
 * Implementation of {@link IdeaService}.
 *
 * @author Erik Andersson
 * @author Simon Göransson
 * @company Monator Technologies AB
 */
@Service
public class IdeaServiceImpl implements IdeaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaServiceImpl.class);
    private static final char[] URL_TITLE_REPLACE_CHARS = new char[]{'.', '/'};
    private static final String LIFERAY_OPEN_DOCUMENTS = "Liferay öppna dokument";
    private static final String LIFERAY_CLOSED_DOCUMENTS = "Liferay stängda dokument";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Value("${auto.comment.default.user.id}")
    private String autoCommentDefaultUserId;

    @Value("${auto.comment.default.message.newphase}")
    private String autoCommentDefaultMessageNewPhase;

    @Value("${auto.comment.default.message.become.public}")
    private String autoCommentDefaultMessageBecomePublic;

    @Value("${auto.comment.default.message.become.private}")
    private String autoCommentDefaultMessageBecomePrivate;

    @Value("${auto.comment.default.message.become.public.public}")
    private String autoCommentDefaultMessageBecomePublicPublic;

    @Value("${auto.comment.default.subject}")
    private String autoCommentDefaultSubject;

    @Value("${comment.page.size}")
    private String defaultCommentCount;

    @Value("${scheme.server.name.url}")
    private String schemeServerNameUrl;

    private IdeaRepository ideaRepository;
    private IdeaFileRepository ideaFileRepository;
    private IdeaUserLikeRepository ideaUserLikeRepository;
    private IdeaUserFavoriteRepository ideaUserFavoriteRepository;
    private BariumService bariumService;
    private IdeaSettingsService ideaSettingsService;

    private final ExecutorService executor = Executors.newCachedThreadPool(new DaemonThreadFactory());

    @Autowired
    private JpaTransactionManager transactionManager;

    @Autowired
    private MBMessageLocalService mbMessageLocalService;

    @Autowired
    private UserLocalService userLocalService;

    @Autowired
    private UserGroupRoleLocalService userGroupRoleLocalService;

    @Autowired
    private ResourceLocalService resourceLocalService;

    @Autowired
    private CounterLocalService counterLocalService;

    @Autowired
    private ContactLocalService contactLocalService;

    @Autowired
    private AssetEntryLocalService assetEntryLocalService;

    @Autowired
    private GroupLocalService groupLocalService;

    @Autowired
    private ClassNameLocalService classNameLocalService;

    @Autowired
    private ResourcePermissionLocalService resourcePermissionLocalService;

    @Autowired
    private RoleLocalService roleLocalService;

    @Autowired
    private LayoutSetLocalService layoutSetLocalService;


    private MessageCreateDateComparator messageComparator;



    /**
     * Instantiates a new idea service impl.
     */
    public IdeaServiceImpl() {
    }


    /**
     * Instantiates a new idea service impl.
     *
     * @param ideaRepository the idea repository
     * @param ideaFileRepository the idea file repository
     * @param ideaUserLikeRepository the idea user like repository
     * @param ideaUserFavoriteRepository the idea user favorite repository
     * @param bariumService the barium service
     * @param ideaSettingsService the idea settings service
     * @param mbMessageLocalService the mb message local service
     * @param userLocalService the user local service
     * @param userGroupRoleLocalService the user group role local service
     * @param resourceLocalService the resource local service
     */
    @Autowired
    public IdeaServiceImpl(IdeaRepository ideaRepository, IdeaFileRepository ideaFileRepository,
            IdeaUserLikeRepository ideaUserLikeRepository, IdeaUserFavoriteRepository ideaUserFavoriteRepository,
            BariumService bariumService, IdeaSettingsService ideaSettingsService,
            MBMessageLocalService mbMessageLocalService, UserLocalService userLocalService,
            UserGroupRoleLocalService userGroupRoleLocalService, ResourceLocalService resourceLocalService,
            CounterLocalService counterLocalService, ContactLocalService contactLocalService,
            AssetEntryLocalService assetEntryLocalService, GroupLocalService groupLocalService,
            ClassNameLocalService classNameLocalService, ResourcePermissionLocalService resourcePermissionLocalService,
            RoleLocalService roleLocalService, LayoutSetLocalService layoutSetLocalService) {
        this.ideaRepository = ideaRepository;
        this.ideaFileRepository = ideaFileRepository;
        this.ideaUserLikeRepository = ideaUserLikeRepository;
        this.ideaUserFavoriteRepository = ideaUserFavoriteRepository;
        this.bariumService = bariumService;
        this.ideaSettingsService = ideaSettingsService;
        this.mbMessageLocalService = mbMessageLocalService;
        this.userLocalService = userLocalService;
        this.userGroupRoleLocalService = userGroupRoleLocalService;
        this.resourceLocalService = resourceLocalService;
        this.counterLocalService = counterLocalService;
        this.contactLocalService= contactLocalService;
        this.assetEntryLocalService = assetEntryLocalService;
        this.groupLocalService = groupLocalService;
        this.classNameLocalService = classNameLocalService;
        this.resourcePermissionLocalService = resourcePermissionLocalService;
        this.roleLocalService = roleLocalService;
        this.layoutSetLocalService = layoutSetLocalService;

    }

    /**
     * Initiations methode.
     */
    @PostConstruct
    public void init() {
        try {
            asyncUpdateAllIdeasFromBarium();
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#addFavorite(long, long, long, java.lang.String)
     */
    @Override
    @Transactional(rollbackFor = FavoriteException.class)
    public void addFavorite(long companyId, long groupId, long userId, String urlTitle) {

        boolean isIdeaUserFavorite = getIsIdeaUserFavorite(companyId, groupId, userId, urlTitle);

        if (isIdeaUserFavorite) {
            // User already has the idea as a favorite
            return;
        }

        Idea idea = findIdeaByUrlTitle(urlTitle);

        IdeaUserFavorite ideaUserFavorite = new IdeaUserFavorite(companyId, groupId, userId);
        ideaUserFavorite.setIdea(idea);

        ideaUserFavoriteRepository.merge(ideaUserFavorite);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#addLike(long, long, long, java.lang.String)
     */
    @Override
    @Transactional(rollbackFor = LikeException.class)
    public void addLike(long companyId, long groupId, long userId, String urlTitle) {

        boolean isIdeaUserLiked = getIsIdeaUserLiked(companyId, groupId, userId, urlTitle);

        if (isIdeaUserLiked) {
            // User already likes the idea
            return;
        }

        Idea idea = findIdeaByUrlTitle(urlTitle);

        IdeaUserLike ideaUserLike = new IdeaUserLike(companyId, groupId, userId);
        ideaUserLike.setIdea(idea);

        ideaUserLikeRepository.merge(ideaUserLike);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#addIdea(se.vgregion.portal
     * .innovationsslussen.domain.jpa.Idea, java.lang.String)
     */
    @Override
    @Transactional(rollbackFor = CreateIdeaException.class)
    public Idea addIdea(Idea idea, String schemeServerNamePort) throws CreateIdeaException, PortalException, SystemException {

        // Do the urlTitle and ideaSiteLink before sending to Barium
        if (idea.getUrlTitle() == null) {
            idea.setUrlTitle(generateNewUrlTitle(idea.getTitle()));
        }
        // idea.setIdeaSiteLink(generateIdeaSiteLink(schemeServerNamePort, idea.getUrlTitle()));

        idea = checkIfIdeaIsForAnotherPerson(idea);

        // Create Barium Idea
        BariumResponse bariumResponse = bariumService.createIdea(idea);
        // BariumResponse bariumResponse = new BariumResponse(true, "MOCKAD-12345666", "");

        if (bariumResponse.getSuccess()) {

            String bariumId = bariumResponse.getInstanceId();

            String phase = bariumService.getIdeaState(bariumId);
            idea.setPhase(phase);

            try {
                idea.setId(bariumId);

                idea.setStatus(IdeaStatus.PRIVATE_IDEA);

                // Persist idea
                idea = ideaRepository.persist(idea); // Use persist to assure it doesn't exist already.

                // Get references to persisted IdeaContent (public and private)
                IdeaContent ideaContentPublic = idea.getIdeaContentPublic();
                IdeaContent ideaContentPrivate = idea.getIdeaContentPrivate();

                boolean addCommunityPermissions = true;
                boolean addGuestPermissions = true;

                // Add resource for Idea
                resourceLocalService.addResources(idea.getCompanyId(), idea.getGroupId(),
                        idea.getUserId(), Idea.class.getName(), idea.getId(), false, addCommunityPermissions,
                        addGuestPermissions);

                ideaContentPublic.getGroupId();
                ideaContentPublic.getId();

                // Add public discussion
                mbMessageLocalService.addDiscussionMessage(idea.getUserId(),
                        String.valueOf(ideaContentPublic.getUserId()), ideaContentPublic.getGroupId(),
                        IdeaContent.class.getName(), ideaContentPublic.getId(),
                        WorkflowConstants.ACTION_PUBLISH);

                // Add private discussion
                mbMessageLocalService.addDiscussionMessage(idea.getUserId(),
                        String.valueOf(ideaContentPrivate.getUserId()), ideaContentPrivate.getGroupId(),
                        IdeaContent.class.getName(), ideaContentPrivate.getId(),
                        WorkflowConstants.ACTION_PUBLISH);

            } catch (SystemException e) {
                LOGGER.error(e.getMessage(), e);

                // Delete idea from Barium
                // Delete message board threads
                // Set BariumId to blank for idea
                // Ska vi inte ta bort idén från db helt isf?

                throw new CreateIdeaException(e);
            } catch (PortalException e) {

                LOGGER.error(e.getMessage(), e);

                // Delete idea from Barium
                // Delete message board threads
                // Set BariumId to blank for idea
                // Ska vi inte ta bort idén från db helt isf?

                // idea.setBariumId("");

                throw new CreateIdeaException(e);
            }

        } else {
            throw new CreateIdeaException("Failed to create idea in Barium, bariumRestClient returned: "
                    + bariumResponse.getJsonString());
        }

        return idea;
    }

    private Idea checkIfIdeaIsForAnotherPerson(Idea idea) throws SystemException, PortalException {

            String ideaUserScreenName = userLocalService.getUser(idea.getUserId()).getScreenName();
            String ideaPersonVgrId = idea.getIdeaPerson().getVgrId();

            if (!ideaUserScreenName.equals(ideaPersonVgrId)){

                idea.getUserId();

                User ideaOriginatorUser;

                try{
                    ideaOriginatorUser = userLocalService.getUserByScreenName(idea.getCompanyId(),ideaPersonVgrId);
                }catch (NoSuchUserException e){
                    ideaOriginatorUser = createUser(idea, ideaPersonVgrId);
                }

                Long ideaOriginatorUserId = ideaOriginatorUser.getUserId();

                idea.setUserId(ideaOriginatorUserId);
                idea.getIdeaPerson().setUserId(ideaOriginatorUserId);
                idea.getIdeaContentPrivate().setUserId(ideaOriginatorUserId);
                idea.getIdeaContentPublic().setUserId(ideaOriginatorUserId);
            }

        return idea;

    }

    private User createUser(Idea idea , String ideaPersonVgrId) {

        try {
            long companyId = idea.getCompanyId();
            long groupId= idea.getGroupId();
            String password1= "qUqP5cyxm6YcTAhz05Hph5gvu9M="; //test
            String domainName = null;
            long facebookId=0;
            String openId=null;
            String firstName= idea.getIdeaPerson().getName();
            String lastName="";
            int prefixId=0;
            int suffixId=0;
            String jobTitle="";

            String emailAddress = idea.getIdeaPerson().getEmail();

            String greeting="Welcome "+ ideaPersonVgrId;

            long idContact = counterLocalService.increment();

            User user = userLocalService.createUser(counterLocalService.increment());
            user.setCompanyId(companyId);
            user.setPassword(password1);
            user.setPasswordEncrypted(true);
            user.setPasswordReset(false);
            user.setPasswordModifiedDate(new Date());
            user.setScreenName(ideaPersonVgrId);
            user.setGreeting(greeting);
            user.setEmailAddress(emailAddress);
            user.setFacebookId(facebookId);
            user.setOpenId(openId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setJobTitle(jobTitle);
            user.setCreateDate(new Date ());
            user.setContactId(idContact);
            user.setCreateDate(new Date());
            user.setModifiedDate(new Date());
            user.setLanguageId("sv_SE");
            user.setTimeZoneId("Europe/Paris");
            user.setActive(true);
            user.setAgreedToTermsOfUse(true);
            userLocalService.addUser(user);

            userLocalService.addDefaultGroups(user.getUserId());
            userLocalService.addDefaultRoles(user.getUserId());
            //Insert Contact for a user

            Contact contact = contactLocalService.createContact(idContact);
            contact.setCompanyId(companyId);
            contact.setCreateDate(new Date());
            contact.setUserName(ideaPersonVgrId);
            contact.setUserId(user.getUserId());
            contact.setModifiedDate(new Date());
            contact.setFirstName("contact-"+contact.getContactId());
            contact.setLastName("contact-"+contact.getContactId());
            contact.setMiddleName("contact-"+contact.getContactId());
            contact.setPrefixId(prefixId);
            contact.setBirthday(new Date());
            contact.setSuffixId(suffixId);
            contact.setJobTitle(jobTitle+contact.getContactId());

            contactLocalService.addContact(contact);

            //Associate a role with user
            long userid = user.getUserId();
            long userIds[] = {userid};
            Role role = roleLocalService.getRole(companyId, "User");
            userLocalService.addRoleUsers(role.getRoleId(), userIds);
            long roleids[]= {role.getRoleId()};
            userGroupRoleLocalService.addUserGroupRoles(user.getUserId(), groupId, roleids);
            ClassName clsNameUser = classNameLocalService.getClassName("com.liferay.portal.model.User");
            long classNameId=clsNameUser.getClassNameId();

            //Insert Group for a user
            long gpId = counterLocalService.increment();
            Group userGrp = groupLocalService.createGroup(gpId);
            userGrp.setClassNameId(classNameId);
            userGrp.setClassPK(userid);
            userGrp.setCompanyId(companyId);
            userGrp.setName("group"+String.valueOf(userid));
            userGrp.setFriendlyURL("/group"+gpId);
            userGrp.setCreatorUserId(userid);
            userGrp.setActive(true);
            groupLocalService.addGroup(userGrp);

    //Create AssetEntry
            long assetEntryId = counterLocalService.increment();
            AssetEntry ae = assetEntryLocalService.createAssetEntry(assetEntryId);
            ae.setCompanyId(companyId);
            ae.setClassPK(user.getUserId());
            ae.setGroupId(userGrp.getGroupId());
            ae.setClassNameId(classNameId);
            assetEntryLocalService.addAssetEntry(ae);

            //Insert ResourcePermission for a User
            long resPermId = counterLocalService.increment();
            ResourcePermission rpEntry = resourcePermissionLocalService.createResourcePermission(resPermId);
            rpEntry.setCompanyId(companyId);
            rpEntry.setName("com.liferay.portal.model.User");
            rpEntry.setPrimaryKey(userid);
            rpEntry.setPrimKey("" + userid);
            rpEntry.setRoleId(role.getRoleId());
            rpEntry.setScope(4);
            resourcePermissionLocalService.addResourcePermission(rpEntry);



            //Insert Layoutset for public and private
            long layoutSetIdPub = counterLocalService.increment();
            LayoutSet layoutSetPub = layoutSetLocalService.createLayoutSet(layoutSetIdPub);
            layoutSetPub.setCompanyId(companyId);
            layoutSetPub.setPrivateLayout(false);
            layoutSetPub.setGroupId(userGrp.getGroupId());
            layoutSetPub.setThemeId("classic");
            try{
                layoutSetLocalService.addLayoutSet(layoutSetPub);
            }catch(SystemException se){

            }

            long layoutSetIdPriv= counterLocalService.increment();
            LayoutSet layoutSetPriv=layoutSetLocalService.createLayoutSet(layoutSetIdPriv);
            layoutSetPriv.setCompanyId(companyId);
            layoutSetPriv.setPrivateLayout(true);
            layoutSetPriv.setThemeId("classic");
            layoutSetPriv.setGroupId(userGrp.getGroupId());
            try{
                layoutSetLocalService.addLayoutSet(layoutSetPriv);
            }catch(SystemException se){
            }

            return user;

        } catch (SystemException e) {
            e.printStackTrace();
        } catch (PortalException e) {
            e.printStackTrace();
        }

        return null;

    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#find(java.lang.String)
     */
    @Override
    public Idea find(String ideaId) {
        return ideaRepository.find(ideaId);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findAll()
     */
    @Override
    public Collection<Idea> findAll() {
        return ideaRepository.findAll();
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findIdeasCountByCompanyId(long)
     */
    @Override
    public int findIdeasCountByCompanyId(long companyId) {
        return ideaRepository.findIdeasCountByCompanyId(companyId);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findIdeasByCompanyId(long)
     */
    @Override
    public List<Idea> findIdeasByCompanyId(long companyId) {
        return ideaRepository.findIdeasByCompanyId(companyId);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findIdeasByCompanyId(long, int, int)
     */
    @Override
    public List<Idea> findIdeasByCompanyId(long companyId, int start, int offset) {
        return ideaRepository.findIdeasByCompanyId(companyId, start, offset);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findIdeaCountByGroupId(long, long)
     */
    @Override
    public int findIdeaCountByGroupId(long companyId, long groupId) {
        return ideaRepository.findIdeaCountByGroupId(companyId, groupId);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findIdeasByGroupId(long, long)
     */
    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId) {
        return ideaRepository.findIdeasByGroupId(companyId, groupId);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findIdeasByGroupId(long, long, int, int)
     */
    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId, int start, int offset) {
        return ideaRepository.findIdeasByGroupId(companyId, groupId, start, offset);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea
     * .IdeaService#findIdeaCountByGroupId(long, long, se.vgregion.portal.innovationsslussen.domain.IdeaStatus)
     */
    @Override
    public int findIdeaCountByGroupId(long companyId, long groupId, IdeaStatus status) {
        return ideaRepository.findIdeaCountByGroupId(companyId, groupId, status);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea
     * .IdeaService#findIdeasByGroupId(long, long, se.vgregion.portal.innovationsslussen.domain.IdeaStatus)
     */
    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId, IdeaStatus status) {
        return ideaRepository.findIdeasByGroupId(companyId, groupId, status);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea
     * .IdeaService#findIdeasByGroupId(long, long, se.vgregion.portal.innovationsslussen.domain.IdeaStatus, int, int)
     */
    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId, IdeaStatus status, int start,
            int offset) {
        return ideaRepository.findIdeasByGroupId(companyId, groupId, status, start, offset);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findIdeasCountByGroupIdAndUserId(long, long, long)
     */
    @Override
    public int findIdeasCountByGroupIdAndUserId(long companyId, long groupId, long userId) {
        return ideaRepository.findIdeasCountByGroupIdAndUserId(companyId, groupId, userId);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findIdeasByGroupIdAndUserId(long, long, long)
     */
    @Override
    public List<Idea> findIdeasByGroupIdAndUserId(long companyId, long groupId, long userId) {
        return ideaRepository.findIdeasByGroupIdAndUserId(companyId, groupId, userId);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea
     * .IdeaService#findIdeasByGroupIdAndUserId(long, long, long, int, int)
     */
    @Override
    public List<Idea> findIdeasByGroupIdAndUserId(long companyId, long groupId, long userId, int start,
            int offset) {
        return ideaRepository.findIdeasByGroupIdAndUserId(companyId, groupId, userId, start, offset);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findUserFavoritedIdeasCount(long, long, long)
     */
    @Override
    public int findUserFavoritedIdeasCount(long companyId, long groupId, long userId) {
        return ideaRepository.findUserFavoritedIdeasCount(companyId, groupId, userId);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findUserFavoritedIdeas(long, long, long)
     */
    @Override
    public List<Idea> findUserFavoritedIdeas(long companyId, long groupId, long userId) {
        return ideaRepository.findUserFavoritedIdeas(companyId, groupId, userId);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findUserFavoritedIdeas(long, long, long, int, int)
     */
    @Override
    public List<Idea> findUserFavoritedIdeas(long companyId, long groupId, long userId, int start, int offset) {
        return ideaRepository.findUserFavoritedIdeas(companyId, groupId, userId, start, offset);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findIdeaByUrlTitle(java.lang.String)
     */
    @Override
    public Idea findIdeaByUrlTitle(String urlTitle) {

        Idea idea = ideaRepository.findIdeaByUrlTitle(urlTitle);

        if (idea != null) {
            idea.setBariumUrl(getBariumUrl(idea));
        }

        return idea;
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findIdeaByUrlTitle(java.lang.String, boolean)
     */
    @Override
    public Idea findIdeaByUrlTitle(String urlTitle, boolean getBariumUrl) {

        Idea idea = ideaRepository.findIdeaByUrlTitle(urlTitle);

        if (getBariumUrl) {
            idea.setBariumUrl(getBariumUrl(idea));
        }

        return idea;
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea
     * .IdeaService#getPublicComments(se.vgregion.portal.innovationsslussen.domain.jpa.Idea)
     */
    @Override
	public List<CommentItemVO> getPublicComments(Idea idea) {
		return getComments(idea.getIdeaContentPublic());
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea
     * .IdeaService#getPrivateComments(se.vgregion.portal.innovationsslussen.domain.jpa.Idea)
     */
    @Override
	public List<CommentItemVO> getPrivateComments(Idea idea) {
		return getComments(idea.getIdeaContentPrivate());
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.vgregion.service.innovationsslussen.idea
	 * .IdeaService#getPublicComments
	 * (se.vgregion.portal.innovationsslussen.domain.jpa.Idea)
	 */
	@Override
	public int getPublicCommentsCount(Idea idea) {
		return getCommentsCount(idea.getIdeaContentPublic());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.vgregion.service.innovationsslussen.idea
	 * .IdeaService#getPrivateComments
	 * (se.vgregion.portal.innovationsslussen.domain.jpa.Idea)
	 */
	@Override
	public int getPrivateCommentsCount(Idea idea) {
		return getCommentsCount(idea.getIdeaContentPrivate());
	}

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea
     * .IdeaService#getIsIdeaUserFavorite(long, long, long, java.lang.String)
     */
    @Override
    public boolean getIsIdeaUserFavorite(long companyId, long groupId, long userId, String urlTitle) {
        boolean isIdeaUserFavorite = false;

        Idea idea = findIdeaByUrlTitle(urlTitle);
        String ideaId = idea.getId();

        List<IdeaUserFavorite> ideaUserFavorites =
                ideaUserFavoriteRepository.findFavoritesByUserAndIdea(companyId, groupId, userId, ideaId);

        if (ideaUserFavorites.size() > 0) {
            isIdeaUserFavorite = true;
        }

        return isIdeaUserFavorite;
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea
     * .IdeaService#getIsIdeaUserLiked(long, long, long, java.lang.String)
     */
    @Override
    public boolean getIsIdeaUserLiked(long companyId, long groupId, long userId, String urlTitle) {
        boolean isIdeaUserLiked = false;

        Idea idea = findIdeaByUrlTitle(urlTitle);
        String ideaId = idea.getId();

        List<IdeaUserLike> ideaUserLikes =
                ideaUserLikeRepository.findLikesByUserAndIdea(companyId, groupId, userId, ideaId);

        if (ideaUserLikes.size() > 0) {
            isIdeaUserLiked = true;
        }

        return isIdeaUserLiked;
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#remove(java.lang.String)
     */
    @Override
    @Transactional
    public void remove(String ideaId) throws RemoveIdeaException {
        Idea idea = ideaRepository.find(ideaId);

        remove(idea);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#remove(se.vgregion.portal
     * .innovationsslussen.domain.jpa.Idea)
     */
    @Override
    @Transactional
    public void remove(Idea idea) throws RemoveIdeaException {

        if (idea != null) {
            try {
                bariumService.getBariumIdea(idea.getId());
            } catch (RuntimeException e) {
                removeFromLiferay(idea);
                throw new RemoveIdeaException("Can not find idea with id " + idea.getId()
                        + " in Barium. Have deleted idea in Liferay. ");
            }

            BariumResponse bariumResponse = bariumService.deleteBariumIdea(idea.getId());

            if (bariumResponse.getSuccess()) {
                removeFromLiferay(idea);
            } else {
                throw new RemoveIdeaException("Removing the idea " + idea.getTitle() + " from Barium failed.");
            }

        } else {
            throw new RemoveIdeaException("Can not find an idea");
        }

    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#removeFromLiferay(java.lang.String)
     */
    @Override
    @Transactional
    public void removeFromLiferay(String ideaId) {
        Idea idea = ideaRepository.find(ideaId);

        removeFromLiferay(idea);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#removeFromLiferay(se.vgregion.portal
     * .innovationsslussen.domain.jpa.Idea)
     */
    @Override
    @Transactional
    public void removeFromLiferay(Idea idea) {

        try {

            idea.getIdeaContentPublic();
            idea.getIdeaContentPrivate();
            idea.getIdeaPerson();

            // Delete resource for Idea
            resourceLocalService.deleteResource(idea.getCompanyId(), Idea.class.getName(),
                    ResourceConstants.SCOPE_INDIVIDUAL, idea.getId());

            // Delete message board entries
            mbMessageLocalService.deleteDiscussionMessages(IdeaContent.class.getName(), idea
                    .getIdeaContentPublic().getId());
            mbMessageLocalService.deleteDiscussionMessages(IdeaContent.class.getName(), idea
                    .getIdeaContentPrivate().getId());

            // Delete idea
            ideaRepository.remove(idea);

        } catch (PortalException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (SystemException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#removeAll()
     */
    @Override
    @Transactional
    public void removeAll() {
        // Collection<Idea> all = ideaRepository.findAll();
        // for (Idea idea : all) {
        // ideaRepository.remove(idea);
        // }
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#removeFavorite(long, long, long, java.lang.String)
     */
    @Override
    @Transactional
    public void removeFavorite(long companyId, long groupId, long userId, String urlTitle) {

        Idea idea = ideaRepository.findIdeaByUrlTitle(urlTitle);

        if (idea != null) {
            // Todo companyId och groupId behövs ej?
            ideaUserFavoriteRepository.removeByUserAndIdea(companyId, groupId, userId, idea.getId());
        }
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#removeLike(long, long, long, java.lang.String)
     */
    @Override
    @Transactional
    public void removeLike(long companyId, long groupId, long userId, String urlTitle) {

        Idea idea = ideaRepository.findIdeaByUrlTitle(urlTitle);

        if (idea != null) {
            ideaUserLikeRepository.removeByUserAndIdea(companyId, groupId, userId, idea.getId());
        }
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#updateFromBarium(java.lang.String)
     */
    @Override
    @Transactional
    public Idea updateFromBarium(String ideaId) {

        Idea idea = ideaRepository.find(ideaId);

        if (idea == null) {
            LOGGER.warn("Idea with id=" + ideaId + " was not found. Can't update.");
            return null;
        }

        idea = updateFromBarium(idea).getNewIdea();

        return idea;
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#updateFromBarium(se.vgregion.portal
     * .innovationsslussen.domain.jpa.Idea)
     */
    @Override
    public UpdateFromBariumResult updateFromBarium(Idea idea) {

        // Do the transaction manually since we may run this in a separate thread.
        TransactionStatus transaction =
                transactionManager.getTransaction(new DefaultTransactionAttribute(
                        TransactionDefinition.PROPAGATION_REQUIRED));

        UpdateFromBariumResult result = new UpdateFromBariumResult();
        result.setOldIdea(find(idea.getId()));
        result.getOldIdea().getIdeaContentPrivate();
        result.getOldIdea().getIdeaContentPublic();

        LOGGER.info(" Update from Barium, idea: " + idea.getTitle());

        // Idea idea = findIdeaByBariumId(id);
        final String ideaId = idea.getId();

        String oldTitle = idea.getTitle(); // To know whether it has changed, in case we need to update in Barium

        // Make two calls asynchronously and simultaneously to speed up.
        Future<IdeaObjectFields> ideaObjectFieldsFuture = bariumService.asyncGetIdeaObjectFields(ideaId);
        Future<String> bariumIdeaPhase = bariumService.asyncGetIdeaPhaseFuture(ideaId);

        int bariumPhase = 0;
        int currentPhase = Integer.parseInt(idea.getPhase() == null ? "0" : idea.getPhase());
        IdeaStatus oldStatus = idea.getStatus();

        try {
            bariumPhase = Integer.parseInt(bariumIdeaPhase.get());

            populateIdea(ideaObjectFieldsFuture.get(), idea);

            if (idea.getIdeaContentPrivate() != null){
                populateFile(idea ,idea.getIdeaContentPrivate(), LIFERAY_CLOSED_DOCUMENTS);
            }

            if (idea.getIdeaContentPublic() != null){
                populateFile(idea ,idea.getIdeaContentPublic(), LIFERAY_OPEN_DOCUMENTS);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        result.setChanged(!isIdeasTheSame(idea, result.getOldIdea()));

        String urlTitle = null;
        if (!oldTitle.equals(idea.getTitle())) {
            idea.setUrlTitle(urlTitle = generateIdeaSiteLink(schemeServerNameUrl, idea.getUrlTitle()));
        }

        idea = ideaRepository.merge(idea);
        result.setNewIdea(idea);

        if (urlTitle != null) {
            final Idea finalIdea = idea;
            final String finalUrlTitle =urlTitle;
            // We may just as well do this asynchronously since we don't throw anything and don't return anything
            // from
            // here.
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    // We need to update the ideaSiteLink in Barium.

                    String ideaSiteLink = generateIdeaSiteLink(schemeServerNameUrl, finalIdea.getUrlTitle());

                    String newIdeaSiteLink = replaceLastPart(ideaSiteLink, finalUrlTitle);
                    try {
                        bariumService.updateIdea(finalIdea.getId(), "siteLank", newIdeaSiteLink);
                    } catch (BariumException e) {
                        LOGGER.error("Failed to update idea " + finalIdea.getId() + " with new site link: "
                                + newIdeaSiteLink, e);
                    }
                }

            });
        }

        if (currentPhase != bariumPhase){
          idea.setPhase("" + (bariumPhase));
          result.setChanged(true);
        }

        transactionManager.commit(transaction);

        // Add auto-comments to the idea.
        generateAutoComments(idea, oldStatus, currentPhase, bariumPhase);


        return result;
    }

    private void generateAutoComments(Idea idea, IdeaStatus oldStatus, int currentPhase, int bariumPhase) {
        if (currentPhase != bariumPhase){
            addAutoComment(idea, idea.getIdeaContentPrivate().getId(), autoCommentDefaultMessageNewPhase + " " + getIdeaPhaseString(bariumPhase));
        }

        if (oldStatus.equals(IdeaStatus.PRIVATE_IDEA) && idea.getStatus().equals(IdeaStatus.PUBLIC_IDEA)) {
            addAutoComment(idea, idea.getIdeaContentPrivate().getId(), autoCommentDefaultMessageBecomePublic);
            addAutoComment(idea, idea.getIdeaContentPublic().getId(), autoCommentDefaultMessageBecomePublicPublic);
        }

        if (oldStatus.equals(IdeaStatus.PUBLIC_IDEA) && idea.getStatus().equals(IdeaStatus.PRIVATE_IDEA)) {
            addAutoComment(idea, idea.getIdeaContentPrivate().getId(), autoCommentDefaultMessageBecomePrivate);
        }
    }

    boolean isIdeasTheSame(Idea i1, Idea i2) {
        if (i1 == i2) {
            return true;
        }
        if (!same(i1, i2)) {
            return false;
        }
        if (!same(i1.getIdeaContentPrivate(), i2.getIdeaContentPrivate())) {
            return false;
        }
        if (!same(i1.getIdeaContentPublic(), i2.getIdeaContentPublic())) {
            return false;
        }
        return true;
    }


    boolean same(Object i1, Object i2) {
        if (i1 == i2) {
            return true;
        }
        if (i1 == null || i2 == null) {
            return false;
        }
        Map bm1 = new HashMap(new BeanMap(i1));
        Map bm2 = new HashMap(new BeanMap(i2));

        if (!bm1.equals(bm2)) {
            return false;
        }
        return true;
    }

    @Transactional
    private void populateFile(Idea idea, IdeaContent ideaContent, String folderName) {

        try {
            Set<IdeaFile> ideaFiles = ideaContent.getIdeaFiles();

            List<ObjectEntry> bariumFiles = bariumService.getIdeaFiles(idea, folderName);

            for (IdeaFile ideaFile : new HashSet<IdeaFile>(ideaFiles)) {
                boolean matched = false;
                for (ObjectEntry bariumFile : bariumFiles) {
                    if (bariumFile.getId().equals(ideaFile.getBariumId())){
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                     ideaFiles.remove(ideaFile);
                }
            }

            for(ObjectEntry bariumFile : bariumFiles){
                IdeaFile ideaFile = null;

                for (IdeaFile file: ideaFiles){
                    if (file.getBariumId().equals(bariumFile.getId())){
                        ideaFile = file;
                    }
                }

                if (ideaFile == null){
                    ideaFile = new IdeaFile();
                    ideaFiles.add(ideaFile);
                }

                ideaFile.setBariumId(bariumFile.getId());
                ideaFile.setCompanyId(idea.getCompanyId());
                ideaFile.setGroupId(idea.getGroupId());
                ideaFile.setName(bariumFile.getName());
                ideaFile.setUserId(idea.getUserId());

            }

        } catch (BariumException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



    }

    private String getIdeaPhaseString(int i) {

        List<String> list = new ArrayList<String>();

        list.add("gemensam utveckling");
        list.add("idé");
        list.add("värdering/utredning");
        list.add("koncept");
        list.add("prövning");
        list.add("kommersialisering");

        return list.get(i);

    }


    private void addAutoComment(Idea idea, long classPK, String message) {
        try {

            String threadView = PropsKeys.DISCUSSION_THREAD_VIEW;
            MBMessageDisplay messageDisplay = mbMessageLocalService.getDiscussionMessageDisplay(
                    idea.getUserId(), idea.getGroupId(), IdeaContent.class.getName(),
                    classPK, WorkflowConstants.STATUS_ANY, threadView);
            MBThread thread = messageDisplay.getThread();

            long threadId = thread.getThreadId();
            long rootThreadId = thread.getRootMessageId();
            ServiceContext serviceContext = new ServiceContext();

            long userId = Long.valueOf(autoCommentDefaultUserId);

            mbMessageLocalService.addDiscussionMessage(userId,
                    autoCommentDefaultUserId, idea.getGroupId(),
                    IdeaContent.class.getName(), idea.getIdeaContentPrivate().getId(),
                    threadId, rootThreadId, autoCommentDefaultSubject, message,
                    serviceContext);

        } catch (PortalException e) {
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    /**
     * Replace last part.
     *
     * @param ideaSiteLink the idea site link
     * @param newUrlTitle the new url title
     * @return the string
     */
    String replaceLastPart(String ideaSiteLink, String newUrlTitle) {
        return ideaSiteLink.substring(0, ideaSiteLink.lastIndexOf("/") + 1) + newUrlTitle;
    }


    // 06.15 every morning
    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#updateAllIdeasFromBarium()
     */
    @Override
    @Transactional
    @Scheduled(cron = "* 15 6 * * ?")
    public void updateAllIdeasFromBarium() {
        LOGGER.info("Updating all ideas from Barium...");

        try {
            Collection<Idea> allIdeas = findAll();

            for (Idea idea : allIdeas) {
                try {
                    // Do the transaction manually since we may run this in a separate thread.
                    TransactionStatus transaction =
                            transactionManager.getTransaction(new DefaultTransactionAttribute(
                                    TransactionDefinition.PROPAGATION_REQUIRED));
                    updateFromBarium(idea);
                    transactionManager.commit(transaction);
                    LOGGER.info("Updated idea with id=" + idea.getId());
                } catch (RuntimeException e) {
                    LOGGER.error("Failed to update idea: " + idea.toString(), e);
                    logIfNextExceptionExists(e);
                }
            }

            LOGGER.info("Finished updating all ideas from Barium.");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            logIfNextExceptionExists(e);
        }
    }

    @Override
    @Transactional
    public void updateIdeasFromBarium(List<String> ideas) {
        for (String idea : ideas) {
            updateFromBarium(idea);
        }
    }


    /**
     * Asynchronous update of all ideas in barium.
     */
    public void asyncUpdateAllIdeasFromBarium() {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                updateAllIdeasFromBarium();
            }
        });
    }

    public static void populateIdea(IdeaObjectFields ideaObjectFields, Idea idea) {

        idea.setTitle(ideaObjectFields.getInstanceName());
        idea.setUrlTitle(titleToUrlTitle(ideaObjectFields.getInstanceName()));

        IdeaContent ideaContentPublic = idea.getIdeaContentPublic();
        IdeaContent ideaContentPrivate = idea.getIdeaContentPrivate();

        // Default set idea status to private, then change if conditions are met
        String publik = ideaObjectFields.getPublik();
        if (publik != null && publik.equals("on")) {
            idea.setStatus(IdeaStatus.PUBLIC_IDEA);
        } else {
            idea.setStatus(IdeaStatus.PRIVATE_IDEA);
        }

        if (ideaContentPublic != null) {
            ideaContentPublic.setIntro(headString(ideaObjectFields.getPublikintrotext()));
            ideaContentPublic.setDescription(ideaObjectFields.getPublikbeskrivning());
        }

        if (ideaContentPrivate != null) {
            ideaContentPrivate.setSolvesProblem(ideaObjectFields.getBehov());
            ideaContentPrivate.setDescription(ideaObjectFields.getIde());
            ideaContentPrivate.setIdeaTested(ideaObjectFields.getTestat());
            ideaContentPrivate.setWantsHelpWith(ideaObjectFields.getKommavidare());

            ideaContentPrivate.setIdeaTransporterComment(headString(ideaObjectFields.getIdetranportorensKommentar()));

            ideaContentPrivate.setPrioritizationCouncilMeetingTime(toDate(ideaObjectFields.getPrioriteringsradsmote()));

            ideaContentPrivate.setAdditionalIdeaOriginators(headString(ideaObjectFields.getKomplnamn()));
        }
    }

    private static String headString(String ideaTransporterComment) {
        if (ideaTransporterComment != null && ideaTransporterComment.length() > 250){
            ideaTransporterComment = ideaTransporterComment.substring(0,250);
        }
        return ideaTransporterComment;
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#uploadFile(se.vgregion.portal
     * .innovationsslussen.domain.jpa.Idea, java.lang.String, java.io.InputStream)
     */
    @Override
    @Deprecated
    // Probably not needed?
    public void uploadFile(Idea idea, String fileName, InputStream inputStream) throws FileUploadException {
        try {
            bariumService.uploadFile(idea, fileName, inputStream);
        } catch (BariumException e) {
            throw new FileUploadException(e);
        }
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#uploadFile(se.vgregion.portal
     * .innovationsslussen.domain.jpa.Idea, boolean, java.lang.String, java.lang.String, java.io.InputStream)
     */
    @Override
    @Transactional
    public IdeaFile uploadFile(Idea idea, boolean publicIdea, String fileName, String contentType,
            InputStream inputStream)
                    throws FileUploadException {

        String folderName;
        IdeaContent ideaContent;

        if (publicIdea) {
            folderName = LIFERAY_OPEN_DOCUMENTS;
            ideaContent = idea.getIdeaContentPublic();
        } else {
            folderName = LIFERAY_CLOSED_DOCUMENTS;
            ideaContent = idea.getIdeaContentPrivate();
        }

        try {
            FileEntry fileEntry = bariumService.uploadFile(idea, folderName, fileName, inputStream);
            IdeaFile ideaFile = new IdeaFile(idea.getCompanyId(), idea.getCompanyId(), idea.getUserId(),
                    fileEntry.getName(), getContentType(fileEntry.getName()));

            ideaFile.setBariumId(fileEntry.getId());

            ideaContent.getIdeaFiles().add(ideaFile);
            ideaRepository.merge(idea);

            return ideaFile;

        } catch (BariumException e) {
            throw new FileUploadException(e);
        }
    }

    private String getContentType(String name) {

        String[] nameArray = name.split("\\.");

        int length = nameArray.length;

        if (length > 0){
            return nameArray[length - 1];
        }
        return "";
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#getObject(java.lang.String)
     */
    @Override
    public ObjectEntry getObject(String id) throws BariumException {
        return bariumService.getObject(id);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#downloadFile(java.lang.String)
     */
    @Override
    public InputStream downloadFile(String id) throws BariumException {
        return bariumService.downloadFile(id);
    }

    /**
     * When a new url title should be generated for a new idea.
     *
     * @param title the title
     * @return the generated url title
     */
    @Override
    public String generateNewUrlTitle(String title) {
        title = title.trim().toLowerCase();

        if (Validator.isNull(title) || Validator.isNumber(title) || title.equals("")) {
            /*
             * long generatedId = 0; try { generatedId = CounterLocalServiceUtil.increment(); } catch
             * (SystemException e) { throw new RuntimeException(e); } return String.valueOf(generatedId);
             */// Behöver kunna förutse url-titeln utifrån titeln så detta skulle ställa till det
            throw new IllegalArgumentException("The title must contain letters.");
        } else {
            String urlTitle = titleToUrlTitle(title);

            boolean isUnique = isUniqueUrlTitle(urlTitle);

            if (!isUnique) {
                String newUrlTitle = "";

                int i = 2;
                while (!isUnique) {
                    newUrlTitle = urlTitle + "-" + i;
                    isUnique = isUniqueUrlTitle(newUrlTitle);
                    i++;
                }
                urlTitle = newUrlTitle;
            }

            return urlTitle;
        }
    }

    private static String titleToUrlTitle(String title) {
        return FriendlyURLNormalizer.normalize(title, URL_TITLE_REPLACE_CHARS);
    }

    protected String getBariumUrl(Idea idea) {
        String bariumUrl = "";

        String bariumDetailsViewUrlPrefix =
                ideaSettingsService.getSetting(ExpandoConstants.BARIUM_DETAILS_VIEW_URL_PREFIX,
                        idea.getCompanyId(), idea.getGroupId());

        if (bariumDetailsViewUrlPrefix != null) {
            if (!bariumDetailsViewUrlPrefix.equals("")) {
                bariumUrl = bariumDetailsViewUrlPrefix + idea.getId();
            }
        }

        return bariumUrl;
    }

	protected List<CommentItemVO> getComments(IdeaContent ideaContent) {

        ArrayList<CommentItemVO> commentsList = new ArrayList<CommentItemVO>();

        try {

            MBMessageDisplay messageDisplay = null;

            try {
                messageDisplay =
                        mbMessageLocalService.getDiscussionMessageDisplay(ideaContent.getUserId(),
                                ideaContent.getGroupId(), IdeaContent.class.getName(), ideaContent.getId(),
                                WorkflowConstants.STATUS_ANY);
            } catch (NullPointerException e) {
                return commentsList;
            }

            Idea idea = ideaContent.getIdea();

            MBThread thread = messageDisplay.getThread();

            long threadId = thread.getThreadId();
            long rootMessageId = thread.getRootMessageId();

            messageComparator = new MessageCreateDateComparator(false);

            @SuppressWarnings("unchecked")
            List<MBMessage> mbMessages =
            mbMessageLocalService.getThreadMessages(threadId, WorkflowConstants.STATUS_ANY,
                    messageComparator);

            for (MBMessage mbMessage : mbMessages) {

                String curCommentText = mbMessage.getBody();
                Date createDate = mbMessage.getCreateDate();
                long commentId = mbMessage.getMessageId();

                if (commentId != rootMessageId) {
                    long curCommentUserId = mbMessage.getUserId();
                    long scopeGroupId = mbMessage.getGroupId();
                    User curCommentUser = userLocalService.getUser(curCommentUserId);
                    String curCommentUserFullName = curCommentUser.getFullName();

                    boolean isUserIdeaCreator = isUserIdeaCreator(curCommentUserId, idea);
                    boolean isUserPrioCouncilMember = isUserPrioCouncilMember(curCommentUserId, scopeGroupId);
                    boolean isUserInnovationsslussenEmployee = isUserInnovationsslussenEmployee(curCommentUserId, scopeGroupId);
                    boolean isUserIdeaTransporter = isUserIdeaTransporter(curCommentUserId, scopeGroupId);

                    CommentItemVO commentItem = new CommentItemVO();
                    commentItem.setCommentText(curCommentText);
                    commentItem.setCreateDate(createDate);
                    commentItem.setId(commentId);
                    commentItem.setName(curCommentUserFullName);
                    commentItem.setUserCreator(isUserIdeaCreator);
                    commentItem.setUserIdeaTransporter(isUserIdeaTransporter);
                    commentItem.setUserPrioCouncilMember(isUserPrioCouncilMember);
                    commentItem.setUserInnovationsslussenEmployee(isUserInnovationsslussenEmployee);
                    commentItem.setUserId(curCommentUserId);

                    commentsList.add(commentItem);
                }
            }

        } catch (PortalException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (SystemException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return commentsList;
    }

	protected int getCommentsCount(IdeaContent ideaContent) {

		int commentsCount = 0;

		try {

			MBMessageDisplay messageDisplay = null;

			try {
				messageDisplay = mbMessageLocalService
						.getDiscussionMessageDisplay(ideaContent.getUserId(),
                                ideaContent.getGroupId(),
                                IdeaContent.class.getName(),
                                ideaContent.getId(),
                                WorkflowConstants.STATUS_ANY);
			} catch (NullPointerException e) {
				return commentsCount;
			}

			MBThread thread = messageDisplay.getThread();

			long threadId = thread.getThreadId();
			long rootMessageId = thread.getRootMessageId();

			messageComparator = new MessageCreateDateComparator(false);

			@SuppressWarnings("unchecked")
			List<MBMessage> mbMessages = mbMessageLocalService
					.getThreadMessages(threadId, WorkflowConstants.STATUS_ANY,
							messageComparator);

			for (MBMessage mbMessage : mbMessages) {

				long commentId = mbMessage.getMessageId();

				if (commentId != rootMessageId) {
					commentsCount++;
				}
			}

		} catch (PortalException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (SystemException e) {
			LOGGER.error(e.getMessage(), e);
		}

		return commentsCount;
	}

    protected boolean isUniqueUrlTitle(String urlTitle) {
        boolean isUnique = false;

        Idea idea = findIdeaByUrlTitle(urlTitle, false);

        if (idea == null) {
            isUnique = true;
        }

        return isUnique;
    }

    protected boolean isUserIdeaCreator(long userId, Idea idea) {
        boolean isUserIdeaCreator = false;

        if (userId == idea.getUserId()) {
            isUserIdeaCreator = true;
        }

        return isUserIdeaCreator;
    }

    @Override
    public boolean isUserIdeaTransporter(long userId, long groupId) {

        boolean isUserIdeaTransporter = false;

        boolean checkInheritedRoles = true;

        try {
            isUserIdeaTransporter =
                    userGroupRoleLocalService.hasUserGroupRole(userId, groupId,
                            IdeaServiceConstants.ROLE_NAME_COMMUNITY_IDEA_TRANSPORTER, checkInheritedRoles);
        } catch (PortalException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (SystemException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return isUserIdeaTransporter;

    }
    
    @Override
    public boolean isUserInnovationsslussenEmployee(long userId, long groupId) {

        boolean isUserInnovationsslussenEmployee = false;

        boolean checkInheritedRoles = true;

        try {
            isUserInnovationsslussenEmployee =
                    userGroupRoleLocalService.hasUserGroupRole(userId, groupId,
                            IdeaServiceConstants.ROLE_NAME_COMMUNITY_INNOVATIONSSLUSSEN, checkInheritedRoles);
        } catch (PortalException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (SystemException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return isUserInnovationsslussenEmployee;

    }

    @Override
    public boolean isUserPrioCouncilMember(long userId, long groupId) {

        boolean isUserPrioCouncilMember = false;

        boolean checkInheritedRoles = true;

        try {
            isUserPrioCouncilMember =
                    userGroupRoleLocalService.hasUserGroupRole(userId, groupId,
                            IdeaServiceConstants.ROLE_NAME_COMMUNITY_PRIO_COUNCIL, checkInheritedRoles);
        } catch (PortalException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (SystemException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return isUserPrioCouncilMember;

    }

    @Override
    public LinkedList<String> getUsersToEmail(Idea idea) {


        LinkedList<String> toEmail = new LinkedList<String>();

        toEmail.add(idea.getIdeaPerson().getEmail());

        try {
            Role roleInnovationsslussen = roleLocalService.getRole(idea.getCompanyId(), IdeaServiceConstants.ROLE_NAME_COMMUNITY_INNOVATIONSSLUSSEN);
            List<User> roleUsersInnovationsslussen = userLocalService.getRoleUsers(roleInnovationsslussen.getRoleId());

            for (User user : roleUsersInnovationsslussen) {
                toEmail.add(user.getEmailAddress());
            }

            Role rolePrioCouncil = roleLocalService.getRole(idea.getCompanyId(), IdeaServiceConstants.ROLE_NAME_COMMUNITY_PRIO_COUNCIL);
            List<User> roleUsersPrioCouncil = userLocalService.getRoleUsers(rolePrioCouncil.getRoleId());

            for (User user : roleUsersPrioCouncil) {
                toEmail.add(user.getEmailAddress());
            }

            Role roleTransporter = roleLocalService.getRole(idea.getCompanyId(), IdeaServiceConstants.ROLE_NAME_COMMUNITY_IDEA_TRANSPORTER);
            List<User> roleUsersTransporter = userLocalService.getRoleUsers(roleTransporter.getRoleId());

            for (User user : roleUsersTransporter) {
                toEmail.add(user.getEmailAddress());
            }

        } catch (PortalException e) {
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
        }

        return toEmail;
    }

    public void setTransactionManager(JpaTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public String generateIdeaSiteLink(String schemeServerNamePort, String urlTitle) {
        return schemeServerNamePort + "/web/innovationsslussen/ide/-/idea/" + urlTitle;
    }

    @Override
    public String getDefaultCommentCount() {
        return defaultCommentCount;
    }

    @Override
    public void setDefaultCommentCount(String defaultCommentCount) {
        this.defaultCommentCount = defaultCommentCount;
    }

    private void logIfNextExceptionExists(Exception e) {
        SQLException nextException = Util.getNextExceptionFromLastCause(e);
        if (nextException != null) {
            LOGGER.error("Next exception is: " + nextException.getMessage(), nextException);
        }
    }

    private static Date toDate(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        try {
            return dateFormat.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

    private static class DaemonThreadFactory implements ThreadFactory {
        private final ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = defaultThreadFactory.newThread(r);
            thread.setName("IdeaServiceExecutor-" + thread.getName());
            thread.setDaemon(true); // We don't want these threads to block a shutdown of the JVM
            return thread;
        }
    }

}
