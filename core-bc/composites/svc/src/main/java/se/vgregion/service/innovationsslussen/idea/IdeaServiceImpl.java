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

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.message.boards.model.MBMessage;
import com.liferay.message.boards.model.MBMessageDisplay;
import com.liferay.message.boards.model.MBThread;
import com.liferay.message.boards.service.MBMessageLocalService;
import com.liferay.message.boards.service.MBMessageLocalServiceUtil;
import com.liferay.message.boards.util.comparator.MessageCreateDateComparator;
import com.liferay.portal.kernel.exception.NoSuchUserException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.model.ClassName;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.ResourcePermission;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.ContactLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupRoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.collections.BeanMap;
import org.hibernate.JDBCException;
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
import se.vgregion.service.innovationsslussen.exception.RemoveIdeaException;
import se.vgregion.service.innovationsslussen.exception.UpdateIdeaException;
import se.vgregion.service.innovationsslussen.idea.settings.IdeaSettingsService;
import se.vgregion.service.innovationsslussen.idea.settings.util.ExpandoConstants;
import se.vgregion.service.innovationsslussen.repository.idea.IdeaRepository;
import se.vgregion.service.innovationsslussen.repository.ideafile.IdeaFileRepository;
import se.vgregion.service.innovationsslussen.repository.ideauserfavorite.IdeaUserFavoriteRepository;
import se.vgregion.service.innovationsslussen.repository.ideauserlike.IdeaUserLikeRepository;
import se.vgregion.service.innovationsslussen.util.FriendlyURLNormalizer;
import se.vgregion.service.innovationsslussen.util.IdeaServiceConstants;
import se.vgregion.util.Util;

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

    @Value("${node.is.sync.responsible}")
    private Boolean isSyncResponsible;

    private IdeaRepository ideaRepository;
    private IdeaFileRepository ideaFileRepository;
    private IdeaUserLikeRepository ideaUserLikeRepository;
    private IdeaUserFavoriteRepository ideaUserFavoriteRepository;
    private BariumService bariumService;
    private IdeaSettingsService ideaSettingsService;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1, new DaemonThreadFactory());

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
     * @param ideaRepository             the idea repository
     * @param ideaFileRepository         the idea file repository
     * @param ideaUserLikeRepository     the idea user like repository
     * @param ideaUserFavoriteRepository the idea user favorite repository
     * @param bariumService              the barium service
     * @param ideaSettingsService        the idea settings service
     * @param mbMessageLocalService      the mb message local service
     * @param userLocalService           the user local service
     * @param userGroupRoleLocalService  the user group role local service
     * @param resourceLocalService       the resource local service
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
        this.contactLocalService = contactLocalService;
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
        executor.shutdownNow();
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
    public Idea addLike(long companyId, long groupId, long userId, String urlTitle) {

        TransactionStatus transaction =
                transactionManager.getTransaction(new DefaultTransactionAttribute(
                        TransactionDefinition.PROPAGATION_REQUIRED));


        boolean isIdeaUserLiked = getIsIdeaUserLiked(companyId, groupId, userId, urlTitle);
        Idea idea = findIdeaByUrlTitle(urlTitle);

        if (isIdeaUserLiked) {
            // User already likes the idea
            return idea;
        }

        IdeaUserLike ideaUserLike = new IdeaUserLike(companyId, groupId, userId);
        ideaUserLike.setIdea(idea);

        ideaUserLikeRepository.merge(ideaUserLike);
        ideaUserLikeRepository.flush();

        transactionManager.commit(transaction);

        return ideaRepository.find(idea.getId());
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


    void logAllErros(Exception e) {
        if (e instanceof JDBCException){
            JDBCException jdbce = (JDBCException) e;
            e.printStackTrace();
            jdbce.getSQLException().getNextException().printStackTrace();
        }
    }



    private Idea checkIfIdeaIsForAnotherPerson(Idea idea) throws SystemException, PortalException {

        String ideaUserScreenName = userLocalService.getUser(idea.getUserId()).getScreenName();
        String ideaPersonVgrId = idea.getIdeaPerson().getVgrId();

        if (!ideaUserScreenName.equals(ideaPersonVgrId)) {

            idea.getUserId();

            User ideaOriginatorUser;

            try {
                ideaOriginatorUser = userLocalService.getUserByScreenName(idea.getCompanyId(), ideaPersonVgrId);
            } catch (NoSuchUserException e) {
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

    private User createUser(Idea idea, String ideaPersonVgrId) throws SystemException, PortalException {

        long companyId = idea.getCompanyId();
        long groupId = idea.getGroupId();
        String password1 = "qUqP5cyxm6YcTAhz05Hph5gvu9M="; //test
        String domainName = null;
        long facebookId = 0;
        String openId = null;
        String firstName = idea.getIdeaPerson().getName();
        String lastName = "";
        int prefixId = 0;
        int suffixId = 0;
        String jobTitle = "";

        String emailAddress = idea.getIdeaPerson().getEmail();

        String greeting = "Welcome " + ideaPersonVgrId;

        long idContact = counterLocalService.increment();
        Contact contact = contactLocalService.createContact(idContact);

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
        user.setCreateDate(new Date());
        user.setContactId(idContact);
        user.setCreateDate(new Date());
        user.setModifiedDate(new Date());
        user.setLanguageId("sv_SE");
        user.setTimeZoneId("Europe/Paris");
        user.setAgreedToTermsOfUse(true);

        userLocalService.addUser(user);

        userLocalService.addDefaultGroups(user.getUserId());
        userLocalService.addDefaultRoles(user.getUserId());
        //Insert Contact for a user

        contact.setCompanyId(companyId);
        contact.setCreateDate(new Date());
        contact.setUserName(ideaPersonVgrId);
        contact.setUserId(user.getUserId());
        contact.setModifiedDate(new Date());
        contact.setFirstName("contact-" + contact.getContactId());
        contact.setLastName("contact-" + contact.getContactId());
        contact.setMiddleName("contact-" + contact.getContactId());
        contact.setPrefixId(prefixId);
        contact.setBirthday(new Date());
        contact.setSuffixId(suffixId);
        contact.setJobTitle(jobTitle + contact.getContactId());

        contactLocalService.addContact(contact);

        //Associate a role with user
        long userid = user.getUserId();
        long userIds[] = {userid};
        Role role = roleLocalService.getRole(companyId, "User");
        userLocalService.addRoleUsers(role.getRoleId(), userIds);
        long roleids[] = {role.getRoleId()};
        userGroupRoleLocalService.addUserGroupRoles(user.getUserId(), groupId, roleids);
        ClassName clsNameUser = classNameLocalService.getClassName("com.liferay.portal.model.User");
        long classNameId = clsNameUser.getClassNameId();

        //Insert Group for a user
        long gpId = counterLocalService.increment();
        Group userGrp = groupLocalService.createGroup(gpId);
        userGrp.setClassNameId(classNameId);
        userGrp.setClassPK(userid);
        userGrp.setCompanyId(companyId);
        userGrp.setName("group" + String.valueOf(userid));
        userGrp.setFriendlyURL("/group" + gpId);
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
        layoutSetLocalService.addLayoutSet(layoutSetPub);

        long layoutSetIdPriv = counterLocalService.increment();
        LayoutSet layoutSetPriv = layoutSetLocalService.createLayoutSet(layoutSetIdPriv);
        layoutSetPriv.setCompanyId(companyId);
        layoutSetPriv.setPrivateLayout(true);
        layoutSetPriv.setThemeId("classic");
        layoutSetPriv.setGroupId(userGrp.getGroupId());
        layoutSetLocalService.addLayoutSet(layoutSetPriv);

        return user;
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
     * .IdeaService#findVisibleIdeaCountByGroupId(long, long, se.vgregion.portal.innovationsslussen.domain.IdeaStatus)
     */
    @Override
    public int findVisibleIdeaCountByGroupId(long companyId, long groupId, IdeaStatus status) {
        return ideaRepository.findVisibleIdeaCountByGroupId(companyId, groupId, status);
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
     * .IdeaService#findVisibleIdeasByGroupId(long, long, se.vgregion.portal.innovationsslussen.domain.IdeaStatus, int, int)
     */
    @Override
    public List<Idea> findVisibleIdeasByGroupId(long companyId, long groupId, IdeaStatus status, int start,
                                                int offset) {
        return ideaRepository.findVisibleIdeasByGroupId(companyId, groupId, status, start, offset);
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
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findVisibleUserFavoritedIdeasCount(long, long, long)
     */
    @Override
    public int findVisibleUserFavoritedIdeasCount(long companyId, long groupId, long userId) {
        return ideaRepository.findVisibleUserFavoritedIdeasCount(companyId, groupId, userId);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findUserFavoritedIdeas(long, long, long)
     */
    @Override
    public List<Idea> findUserFavoritedIdeas(long companyId, long groupId, long userId) {
        return ideaRepository.findUserFavoritedIdeas(companyId, groupId, userId);
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#findVisibleUserFavoritedIdeas(long, long, long, int, int)
     */
    @Override
    public List<Idea> findVisibleUserFavoritedIdeas(long companyId, long groupId, long userId, int start, int offset) {
        return ideaRepository.findVisibleUserFavoritedIdeas(companyId, groupId, userId, start, offset);
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
    //  @Override
    //  @Transactional
    //  public void remove(String ideaId) throws RemoveIdeaException {
    //      Idea idea = ideaRepository.find(ideaId);
    //
    //       remove(idea);
    //  }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#remove(se.vgregion.portal
     * .innovationsslussen.domain.jpa.Idea)
     */
    @Override
    @Transactional()
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
    public Idea removeLike(long companyId, long groupId, long userId, String urlTitle) {

        Idea idea = ideaRepository.findIdeaByUrlTitle(urlTitle);

        if (idea != null) {
            ideaUserLikeRepository.removeByUserAndIdea(companyId, groupId, userId, idea.getId());
        }

        for (IdeaUserLike ideaUserLike : idea.getLikes()) {
            if (ideaUserLike.getUserId() == userId) {
                idea.getLikes().remove(ideaUserLike);
                break;
            }
        }

        return idea;
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#updateFromBarium(java.lang.String)
     */
    @Override
    //@Transactional
    public Idea updateFromBarium(String ideaId) throws UpdateIdeaException {

        Idea idea = ideaRepository.find(ideaId);

        if (idea == null) {
            LOGGER.warn("Idea with id=" + ideaId + " was not found. Can't update.");
            return null;
        }

        idea = updateFromBarium(idea).getNewIdea();

        return idea;
    }

    @Override
    public boolean ideaOpenPartContainsFile(Idea idea, String fileId) {
        try {
            List<ObjectEntry> bariumFiles = bariumService.getIdeaFiles(idea, LIFERAY_OPEN_DOCUMENTS);
            return anyMatch(fileId, bariumFiles);
        } catch (BariumException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean ideaClosedPartContainsFile(Idea idea, String fileId) {
        try {
            List<ObjectEntry> bariumFiles = bariumService.getIdeaFiles(idea, LIFERAY_CLOSED_DOCUMENTS);
            return anyMatch(fileId, bariumFiles);
        } catch (BariumException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean anyMatch(String fileId, List<ObjectEntry> bariumFiles) {
        for (ObjectEntry bariumFile : bariumFiles) {
            if (fileId.equals(bariumFile.getId())) {
                return true;
            }
        }

        // No match
        return false;
    }

    /* (non-Javadoc)
     * @see se.vgregion.service.innovationsslussen.idea.IdeaService#updateFromBarium(se.vgregion.portal
     * .innovationsslussen.domain.jpa.Idea)
     */
    @Override
    //@Transactional(rollbackFor = BariumException.class)
    public UpdateFromBariumResult updateFromBarium(Idea idea) throws UpdateIdeaException {

        // Do the transaction manually since we may run this in a separate thread.
        TransactionStatus transaction =
                transactionManager.getTransaction(new DefaultTransactionAttribute(
                        TransactionDefinition.PROPAGATION_REQUIRES_NEW));

        try {

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
                if (bariumIdeaPhase.get() == null) {
                    throw new BariumException("bariumIdeaPhase is null");
                }
                bariumPhase = Integer.parseInt(bariumIdeaPhase.get());

                populateIdea(ideaObjectFieldsFuture.get(), idea);

                //Sync files
                if (idea.getIdeaContentPrivate() != null) {
                    populateFile(idea, idea.getIdeaContentPrivate(), LIFERAY_CLOSED_DOCUMENTS);
                }
                if (idea.getIdeaContentPublic() != null) {
                    populateFile(idea, idea.getIdeaContentPublic(), LIFERAY_OPEN_DOCUMENTS);
                }

            } catch (InterruptedException e) {
                throw new UpdateIdeaException(e);
            } catch (ExecutionException e) {
                throw new UpdateIdeaException(e);
            }

            result.setChanged(!isIdeasTheSame(idea, result.getOldIdea()));

            final String finalUrlTitle;
            if (!oldTitle.equals(idea.getTitle())) {
                finalUrlTitle = generateNewUrlTitle(idea.getTitle());
                idea.setUrlTitle(finalUrlTitle);
            } else {
                idea.setUrlTitle(result.getOldIdea().getUrlTitle());
                finalUrlTitle = null;
            }

            if (idea.getOriginalUserId() == null) {
                idea.setOriginalUserId(0L);
            }

            idea = ideaRepository.merge(idea);
            result.setNewIdea(idea);

            if (finalUrlTitle != null) {
                final Idea finalIdea = idea;
                // We may just as well do this asynchronously since we don't throw anything and don't return anything
                // from
                // here.
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        // We need to update the ideaSiteLink in Barium.
                        String ideaSiteLink = generateIdeaSiteLink(schemeServerNameUrl, finalUrlTitle);
                        try {
                            bariumService.updateIdea(finalIdea.getId(), "siteLank", ideaSiteLink);
                        } catch (BariumException e) {
                            LOGGER.error("Failed to update idea " + finalIdea.getId() + " with new site link: "
                                    + ideaSiteLink, e);
                        }
                    }

                });
            }

            if (currentPhase != bariumPhase) {
                idea.setPhase("" + (bariumPhase));
                result.setChanged(true);
            }

            // Add auto-comments to the idea.
            idea = generateAutoComments(idea, oldStatus, currentPhase, bariumPhase);

            // Sync comments count
            idea.setCommentsCount(getPrivateCommentsCount(idea));
            idea = ideaRepository.merge(idea);

            // Sync last comment date
            List<CommentItemVO> privateComments = getComments(idea.getIdeaContentPrivate());
            if (privateComments.size() > 0) {
                Collections.sort(privateComments, new Comparator<CommentItemVO>() {
                    @Override
                    public int compare(CommentItemVO o1, CommentItemVO o2) {
                        return -o1.getCreateDate().compareTo(o2.getCreateDate());
                    }
                });
                idea.setLastPrivateCommentDate(privateComments.get(0).getCreateDate());
            }

            if (transaction.isNewTransaction()) {
                transactionManager.commit(transaction);
            }

            return result;
        } catch (BariumException be) {
            transactionManager.rollback(transaction);
            LOGGER.warn(be.getMessage());
        } finally {
            if (!transaction.isCompleted()) {
                //If this happens, a runtimeexception has likley occurred.
                transactionManager.rollback(transaction);
                LOGGER.warn("Rolledback transaction because of likely RunTimeException");
            }
        }
        return null;
    }

    private Idea generateAutoComments(Idea idea, IdeaStatus oldStatus, int currentPhase, int bariumPhase) {
        if (currentPhase != bariumPhase && bariumPhase != 6 && !(currentPhase == 5 && bariumPhase == 6)) {
            addAutoComment(idea, idea.getIdeaContentPrivate().getId(), autoCommentDefaultMessageNewPhase + " " + getIdeaPhaseString(bariumPhase));
            sendEmailNotification(idea, false);
        }

        if (oldStatus.equals(IdeaStatus.PRIVATE_IDEA) && idea.getStatus().equals(IdeaStatus.PUBLIC_IDEA)) {
            addAutoComment(idea, idea.getIdeaContentPrivate().getId(), autoCommentDefaultMessageBecomePublic);
            addAutoComment(idea, idea.getIdeaContentPublic().getId(), autoCommentDefaultMessageBecomePublicPublic);
            sendEmailNotification(idea, false);
        }

        if (oldStatus.equals(IdeaStatus.PUBLIC_IDEA) && idea.getStatus().equals(IdeaStatus.PRIVATE_IDEA)) {
            addAutoComment(idea, idea.getIdeaContentPrivate().getId(), autoCommentDefaultMessageBecomePrivate);
            int commentsCount = idea.getCommentsCount();
        }
        return idea;
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

    private void populateFile(Idea idea, IdeaContent ideaContent, String folderName) {

        try {
            Set<IdeaFile> ideaFiles = ideaContent.getIdeaFiles();

            List<ObjectEntry> bariumFiles = bariumService.getIdeaFiles(idea, folderName);

            for (IdeaFile ideaFile : new HashSet<IdeaFile>(ideaFiles)) {
                boolean matched = false;
                for (ObjectEntry bariumFile : bariumFiles) {
                    if (bariumFile.getId().equals(ideaFile.getBariumId())) {
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    ideaFiles.remove(ideaFile);
                }
            }

            for (ObjectEntry bariumFile : bariumFiles) {
                IdeaFile ideaFile = null;

                for (IdeaFile file : ideaFiles) {
                    if (file.getBariumId().equals(bariumFile.getId())) {
                        ideaFile = file;
                    }
                }

                if (ideaFile == null) {
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
        // step 5 and 6 in barium are the same (?)
        list.add("kommersialisering");

        return list.get(i);

    }


    private void addAutoComment(Idea idea, long classPK, String message) {
        try {

            MBMessageDisplay messageDisplay = mbMessageLocalService.getDiscussionMessageDisplay(
                    idea.getUserId(), idea.getGroupId(), IdeaContent.class.getName(),
                    classPK, WorkflowConstants.STATUS_ANY);
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

    public Idea addMBMessage(Idea idea, ServiceContext serviceContext, long groupId, long userId, String comment, long ideaCommentClassPK) throws PortalException, SystemException {

        User user = UserLocalServiceUtil.getUser(userId);

        MBMessageDisplay messageDisplay = MBMessageLocalServiceUtil.getDiscussionMessageDisplay(
                userId, groupId, IdeaContent.class.getName(), ideaCommentClassPK,
                WorkflowConstants.STATUS_ANY);

        MBThread thread = messageDisplay.getThread();
        long threadId = thread.getThreadId();
        long rootThreadId = thread.getRootMessageId();

        String commentContentCleaned = comment;
        final int maxLenghtCommentSubject = 50;
        String commentSubject = comment;
        commentSubject = StringUtil.shorten(commentSubject, maxLenghtCommentSubject);
        commentSubject += "...";

        // TODO - validate comment and preserve line breaks
        MBMessageLocalServiceUtil.addDiscussionMessage(
                userId, user.getScreenName(), groupId,
                IdeaContent.class.getName(), ideaCommentClassPK, threadId,
                rootThreadId, commentSubject, commentContentCleaned,
                serviceContext);

        return idea;
    }

    /**
     * Replace last part.
     *
     * @param ideaSiteLink the idea site link
     * @param newUrlTitle  the new url title
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
    @Scheduled(cron = "0 15 6 * * ?")
    @Transactional
    public void updateAllIdeasFromBarium() {
        LOGGER.info("Updating all ideas from Barium...");

        if (!shouldRunUpdate()) {
            LOGGER.info("Skipping update...");
            return;
        }

        try {
            Collection<Idea> allIdeas = findAll();

            for (Idea idea : allIdeas) {
                idea = upgradeIfOldVersion(idea);

                try {
                    updateFromBarium(idea);
                    LOGGER.info("Updated idea with id=" + idea.getId());
                } catch (RuntimeException e) {
                    LOGGER.error("Failed to update idea: " + idea.toString(), e);
                    logIfNextExceptionExists(e);
                } catch (UpdateIdeaException e) {
                    LOGGER.error("Failed to update idea: " + idea.toString(), e);
                }
            }

            LOGGER.info("Finished updating all ideas from Barium.");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            logIfNextExceptionExists(e);
        }
    }

    private Idea upgradeIfOldVersion(Idea idea) {
        // Upgrade potentially old ideas without the hidden attribute set
        if (idea.getHidden() == null) {
            idea.setHidden(false);
            TransactionStatus transaction = null;
            try {
                // Do the transaction manually since we may run this in a separate thread.
                transaction = transactionManager.getTransaction(new DefaultTransactionAttribute(
                        TransactionDefinition.PROPAGATION_REQUIRED));
                // Merge here so we don't get unexpected consequences later when we will compare to decide whether
                // the idea has changed. This should not cause a change.
                ideaRepository.merge(idea);
                transactionManager.commit(transaction);
            } catch (RuntimeException e) {
                transactionManager.rollback(transaction);
            }
        }
        return idea;
    }

    @Override
    @Transactional
    public void updateIdeasFromBarium(List<String> ideas) {
        for (String idea : ideas) {
            try {
                updateFromBarium(idea);
            } catch (UpdateIdeaException e) {
                LOGGER.error("Failed to update idea: " + idea.toString(), e);
            }
        }
    }


    /**
     * Asynchronous update of all ideas in barium.
     */
    public void asyncUpdateAllIdeasFromBarium() {
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                updateAllIdeasFromBarium();
            }
        }, 5, TimeUnit.MINUTES);
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
            ideaContentPrivate.setWantsHelpWith(ideaObjectFields.getKommavidare());
            ideaContentPrivate.setIdeTansportor(ideaObjectFields.getIdetransportor());

            ideaContentPrivate.setIdeaTransporterComment(headString(ideaObjectFields.getIdetranportorensKommentar()));

            ideaContentPrivate.setPrioritizationCouncilMeetingTime(toDate(ideaObjectFields.getPrioriteringsradsmote()));
        }


    }

    private static String headString(String ideaTransporterComment) {
        if (ideaTransporterComment != null && ideaTransporterComment.length() > 250) {
            ideaTransporterComment = ideaTransporterComment.substring(0, 250);
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

        if (length > 0) {
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

                    boolean isUserIdeaCreator = isUserIdeaCreator(curCommentUserId, idea);
                    boolean isUserPrioCouncilMember = isUserPrioCouncilMember(curCommentUserId, scopeGroupId);
                    boolean isUserInnovationsslussenEmployee = isUserInnovationsslussenEmployee(curCommentUserId, scopeGroupId);
                    boolean isUserIdeaTransporter = isUserIdeaTransporter(curCommentUserId, scopeGroupId);

                    CommentItemVO commentItem = new CommentItemVO();
                    commentItem.setCommentText(curCommentText);
                    commentItem.setCreateDate(createDate);
                    commentItem.setId(commentId);
                    commentItem.setUserCreator(isUserIdeaCreator);
                    commentItem.setUserIdeaTransporter(isUserIdeaTransporter);
                    commentItem.setUserPrioCouncilMember(isUserPrioCouncilMember);
                    commentItem.setUserInnovationsslussenEmployee(isUserInnovationsslussenEmployee);
                    commentItem.setUserId(curCommentUserId);
                    commentItem.setMessageId(mbMessage.getMessageId());

                    try {
                        User curCommentUser = userLocalService.getUser(curCommentUserId);
                        String curCommentUserFullName = curCommentUser.getFullName();
                        commentItem.setName(curCommentUserFullName);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }

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
    public LinkedList<String> getInternalIdeaUsersToEmail(Idea idea) {


        LinkedList<String> toEmail = new LinkedList<String>();

        // toEmail.add(idea.getIdeaPerson().getEmail());

        try {
            toEmail = getUserGroupRoleByRoleAndGroup(idea, toEmail, IdeaServiceConstants.ROLE_NAME_COMMUNITY_INNOVATIONSSLUSSEN);
            toEmail = getUserGroupRoleByRoleAndGroup(idea, toEmail, IdeaServiceConstants.ROLE_NAME_COMMUNITY_IDEA_TRANSPORTER);
        } catch (PortalException e) {
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
        }

        return toEmail;
    }

    private LinkedList<String> getUserGroupRoleByRoleAndGroup(Idea idea, LinkedList<String> toEmail, String roleName) throws PortalException, SystemException {
        Role roleInnovationsslussen = roleLocalService.getRole(idea.getCompanyId(), roleName);
        List<UserGroupRole> roleUsersInnovationsslussen = userGroupRoleLocalService.getUserGroupRolesByGroupAndRole(idea.getGroupId(), roleInnovationsslussen.getRoleId());

        for (UserGroupRole userGroupRole : roleUsersInnovationsslussen) {
            String email = userGroupRole.getUser().getEmailAddress();
            if (!toEmail.contains(email)) {
                toEmail.add(email);
            }
        }
        return toEmail;
    }

    public void setTransactionManager(JpaTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public String generateIdeaSiteLink(String schemeServerNamePort, String urlTitle) {
        return schemeServerNamePort + urlTitle;
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

    public void sendEmailNotification(Idea idea, boolean publicbody) {

        if (ideaSettingsService.getSettingBoolean(ExpandoConstants.NOTIFICATION_EMAIL_ACTIVE,
                idea.getCompanyId(), idea.getGroupId())) {

            //Creat json messages.
            JSONObject mailToIdeaOwner = JSONFactoryUtil.createJSONObject();
            JSONObject mailToInternalIdeaUsers = JSONFactoryUtil.createJSONObject();

            //Add company ids.
            mailToIdeaOwner.put("companyId", idea.getCompanyId());
            mailToInternalIdeaUsers.put("companyId", idea.getCompanyId());

            //To
            //Add owner to email.
            JSONArray ownerToEmailArray = JSONFactoryUtil.createJSONArray();
            ownerToEmailArray.put(idea.getIdeaPerson().getEmail());
            mailToIdeaOwner.put("emailTo", ownerToEmailArray);

            //Add others to email.
            LinkedList<String> internalIdeaUsersToEmail = getInternalIdeaUsersToEmail(idea);
            JSONArray internalIdeaUsersToEmailArray = JSONFactoryUtil.createJSONArray();

            for (String email : internalIdeaUsersToEmail) {
                internalIdeaUsersToEmailArray.put(email);
            }
            mailToInternalIdeaUsers.put("emailTo", internalIdeaUsersToEmailArray);

            //From
            mailToIdeaOwner.put("emailFrom", ideaSettingsService.getSetting(ExpandoConstants.NOTIFICATION_EMAIL_FROM,
                    idea.getCompanyId(), idea.getGroupId()));
            mailToInternalIdeaUsers.put("emailFrom", ideaSettingsService.getSetting(ExpandoConstants.NOTIFICATION_EMAIL_FROM,
                    idea.getCompanyId(), idea.getGroupId()));

            //Subject
            mailToIdeaOwner.put("subject",
                    replaceTokens(ideaSettingsService.getSetting(ExpandoConstants.NOTIFICATION_EMAIL_SUBJECT,
                            idea.getCompanyId(), idea.getGroupId()), idea, false));

            mailToInternalIdeaUsers.put("subject",
                    replaceTokens(ideaSettingsService.getSetting(ExpandoConstants.NOTIFICATION_EMAIL_SUBJECT,
                            idea.getCompanyId(), idea.getGroupId()), idea, false));

            //Body
            //Owner body
            if (publicbody) {
                mailToIdeaOwner.put("body",
                        replaceTokens(ideaSettingsService.getSetting(ExpandoConstants.NOTIFICATION_EMAIL_PUBLIC_BODY,
                                idea.getCompanyId(), idea.getGroupId()), idea, false));
            } else {
                mailToIdeaOwner.put("body",
                        replaceTokens(ideaSettingsService.getSetting(ExpandoConstants.NOTIFICATION_EMAIL_PRIVATE_BODY,
                                idea.getCompanyId(), idea.getGroupId()), idea, false));
            }

            //Body for other
            if (publicbody) {
                //Remove the owner name for other receivers
                mailToInternalIdeaUsers.put("body",
                        replaceTokens(ideaSettingsService.getSetting(ExpandoConstants.NOTIFICATION_EMAIL_PUBLIC_BODY,
                                idea.getCompanyId(), idea.getGroupId()), idea, true));
            } else {
                //Remove the owner name for other receivers
                mailToInternalIdeaUsers.put("body",
                        replaceTokens(ideaSettingsService.getSetting(ExpandoConstants.NOTIFICATION_EMAIL_PRIVATE_BODY,
                                idea.getCompanyId(), idea.getGroupId()), idea, true));
            }

            //Idea owner
            Message messageToIdeaOwner = new Message();
            messageToIdeaOwner.setPayload(mailToIdeaOwner);
            MessageBusUtil.sendMessage("vgr/email/notification", messageToIdeaOwner);

            //Internal idea users (role Inovationsslussen and Idea transporter).
            Message messageToInternalIdeaUsers = new Message();
            messageToInternalIdeaUsers.setPayload(mailToInternalIdeaUsers);
            MessageBusUtil.sendMessage("vgr/email/notification", messageToInternalIdeaUsers);
        }
    }

    @Override
    @Transactional
    public Idea hide(String ideaId) {
        Idea idea = ideaRepository.getReference(ideaId);
        idea.setHidden(true);
        idea = ideaRepository.merge(idea);
        return idea;
    }

    @Override
    @Transactional
    public Idea unhide(String ideaId) {
        Idea idea = ideaRepository.getReference(ideaId);
        idea.setHidden(false);
        idea = ideaRepository.merge(idea);
        return idea;
    }

    @Override
    @Transactional
    public Idea save(Idea idea) {
        return ideaRepository.merge(idea);
    }

    /**
     * This method replaces tokens in the message text to the real value from an idea.
     *
     * @param in   the message to replace tokens in.
     * @param idea the idea to get values from.
     * @return the text with the token replaced by the values.
     */
    private String replaceTokens(String in, Idea idea, boolean hidePersonName) {
        String out = "";

        String serverNameUrl = ideaSettingsService.getSetting(ExpandoConstants.SERVER_NAME_URL,
                idea.getCompanyId(), idea.getGroupId());

        in = in.replaceAll("\\[\\$PERSON_NAME\\$\\]", hidePersonName ? "" : idea.getIdeaPerson().getName());

        String link = "<a href=\"" + serverNameUrl + idea.getUrlTitle() + "\">" + idea.getTitle() + "</a>";
        in = in.replaceAll("\\[\\$IDEA_NAME_AND_LINK\\$\\]", link);

        String urlLink = "<a href=\"" + serverNameUrl + idea.getUrlTitle() + "\">" + serverNameUrl + idea.getUrlTitle() + "</a>";
        in = in.replaceAll("\\[\\$IDEA_URL\\$\\]", urlLink);
        out = in.replaceAll("\\[\\$IDEA_NAME\\$\\]", idea.getTitle());

        return out;
    }

    private boolean shouldRunUpdate() {
        return isSyncResponsible;
    }

}
