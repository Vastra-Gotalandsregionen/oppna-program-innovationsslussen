package se.vgregion.service.innovationsslussen.idea;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import javax.annotation.PostConstruct;

import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.service.*;
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
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserFavorite;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserLike;
import se.vgregion.portal.innovationsslussen.domain.json.ObjectEntry;
import se.vgregion.portal.innovationsslussen.domain.vo.CommentItemVO;
import se.vgregion.service.barium.BariumException;
import se.vgregion.service.barium.BariumService;
import se.vgregion.service.innovationsslussen.exception.*;
import se.vgregion.service.innovationsslussen.idea.settings.IdeaSettingsService;
import se.vgregion.service.innovationsslussen.idea.settings.util.ExpandoConstants;
import se.vgregion.service.innovationsslussen.repository.idea.IdeaRepository;
import se.vgregion.service.innovationsslussen.repository.ideacontent.IdeaContentRepository;
import se.vgregion.service.innovationsslussen.repository.ideafile.IdeaFileRepository;
import se.vgregion.service.innovationsslussen.repository.ideaperson.IdeaPersonRepository;
import se.vgregion.service.innovationsslussen.repository.ideauserfavorite.IdeaUserFavoriteRepository;
import se.vgregion.service.innovationsslussen.repository.ideauserlike.IdeaUserLikeRepository;
import se.vgregion.service.innovationsslussen.util.FriendlyURLNormalizer;
import se.vgregion.service.innovationsslussen.util.IdeaServiceConstants;

import com.liferay.counter.service.CounterLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.User;
import com.liferay.portlet.messageboards.service.MBMessageLocalService;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBMessageDisplay;
import com.liferay.portlet.messageboards.model.MBThread;
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


    @Value("${auto.comment.default.user.id}")
    private String autoCommentDefaultUserId;

    @Value("${auto.comment.default.message.newphase}")
    private String autoCommentDefaultMessageNewPhase;

    @Value("${auto.comment.default.message.become.public}")
    private String autoCommentDefaultMessageBecomePublic;

    @Value("${auto.comment.default.subject}")
    private String autoCommentDefaultSubject;


    private IdeaRepository ideaRepository;
    private IdeaContentRepository ideaContentRepository;
    private IdeaFileRepository ideaFileRepository;
    private IdeaPersonRepository ideaPersonRepository;
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

    public IdeaServiceImpl() {
    }

    /**
     * Constructor.
     *
     * @param ideaRepository        the {@link IdeaRepository}
     * @param ideaContentRepository the {@link IdeaContentRepository}
     * @param ideaPersonRepository  the {@link IdeaPersonRepository}
     * @param bariumService         the {@link BariumService}
     */
    @Autowired
    public IdeaServiceImpl(IdeaRepository ideaRepository, IdeaContentRepository ideaContentRepository,
                           IdeaFileRepository ideaFileRepository, IdeaPersonRepository ideaPersonRepository,
                           IdeaUserLikeRepository ideaUserLikeRepository, IdeaUserFavoriteRepository ideaUserFavoriteRepository,
                           BariumService bariumService, IdeaSettingsService ideaSettingsService,
                           MBMessageLocalService mbMessageLocalService, UserLocalService userLocalService,
                           UserGroupRoleLocalService userGroupRoleLocalService, ResourceLocalService resourceLocalService) {
        this.ideaRepository = ideaRepository;
        this.ideaContentRepository = ideaContentRepository;
        this.ideaFileRepository = ideaFileRepository;
        this.ideaPersonRepository = ideaPersonRepository;
        this.ideaUserLikeRepository = ideaUserLikeRepository;
        this.ideaUserFavoriteRepository = ideaUserFavoriteRepository;
        this.bariumService = bariumService;
        this.ideaSettingsService = ideaSettingsService;
        this.mbMessageLocalService = mbMessageLocalService;
        this.userLocalService = userLocalService;
        this.userGroupRoleLocalService = userGroupRoleLocalService;
        this.resourceLocalService = resourceLocalService;

    }

    @PostConstruct
    public void init() {
        try {
            asyncUpdateAllIdeasFromBarium();
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

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

        ideaUserFavorite = ideaUserFavoriteRepository.merge(ideaUserFavorite);
    }

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

        ideaUserLike = ideaUserLikeRepository.merge(ideaUserLike);
    }

    @Override
    @Transactional(rollbackFor = CreateIdeaException.class)
    public Idea addIdea(Idea idea, String schemeServerNamePort) throws CreateIdeaException {

        // Do the urlTitle and ideaSiteLink before sending to Barium
        if (idea.getUrlTitle() == null) {
            idea.setUrlTitle(generateNewUrlTitle(idea.getTitle()));
        }
        idea.setIdeaSiteLink(generateIdeaSiteLink(schemeServerNamePort, idea.getUrlTitle()));

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

                // Get references to persisted IdeaPerson
                IdeaPerson ideaPerson = idea.getIdeaPerson();

                boolean addCommunityPermissions = true;
                boolean addGuestPermissions = true;

                // Add resource for Idea
                resourceLocalService.addResources(idea.getCompanyId(), idea.getGroupId(),
                        idea.getUserId(), Idea.class.getName(), idea.getId(), false, addCommunityPermissions,
                        addGuestPermissions);

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

    @Override
    public Idea find(String ideaId) {
        return ideaRepository.find(ideaId);
    }

    @Override
    public Collection<Idea> findAll() {
        return ideaRepository.findAll();
    }

    @Override
    public int findIdeasCountByCompanyId(long companyId) {
        return ideaRepository.findIdeasCountByCompanyId(companyId);
    }

    @Override
    public List<Idea> findIdeasByCompanyId(long companyId) {
        return ideaRepository.findIdeasByCompanyId(companyId);
    }

    @Override
    public List<Idea> findIdeasByCompanyId(long companyId, int start, int offset) {
        return ideaRepository.findIdeasByCompanyId(companyId, start, offset);
    }

    @Override
    public int findIdeaCountByGroupId(long companyId, long groupId) {
        return ideaRepository.findIdeaCountByGroupId(companyId, groupId);
    }

    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId) {
        return ideaRepository.findIdeasByGroupId(companyId, groupId);
    }

    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId, int start, int offset) {
        return ideaRepository.findIdeasByGroupId(companyId, groupId, start, offset);
    }

    @Override
    public int findIdeaCountByGroupId(long companyId, long groupId, IdeaStatus status) {
        return ideaRepository.findIdeaCountByGroupId(companyId, groupId, status);
    }

    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId, IdeaStatus status) {
        return ideaRepository.findIdeasByGroupId(companyId, groupId, status);
    }

    @Override
    public List<Idea> findIdeasByGroupId(long companyId, long groupId, IdeaStatus status, int start,
                                         int offset) {
        return ideaRepository.findIdeasByGroupId(companyId, groupId, status, start, offset);
    }

    @Override
    public int findIdeasCountByGroupIdAndUserId(long companyId, long groupId, long userId) {
        return ideaRepository.findIdeasCountByGroupIdAndUserId(companyId, groupId, userId);
    }

    @Override
    public List<Idea> findIdeasByGroupIdAndUserId(long companyId, long groupId, long userId) {
        return ideaRepository.findIdeasByGroupIdAndUserId(companyId, groupId, userId);
    }

    @Override
    public List<Idea> findIdeasByGroupIdAndUserId(long companyId, long groupId, long userId, int start,
                                                  int offset) {
        return ideaRepository.findIdeasByGroupIdAndUserId(companyId, groupId, userId, start, offset);
    }

    @Override
    public int findUserFavoritedIdeasCount(long companyId, long groupId, long userId) {
        return ideaRepository.findUserFavoritedIdeasCount(companyId, groupId, userId);
    }

    @Override
    public List<Idea> findUserFavoritedIdeas(long companyId, long groupId, long userId) {
        return ideaRepository.findUserFavoritedIdeas(companyId, groupId, userId);
    }

    @Override
    public List<Idea> findUserFavoritedIdeas(long companyId, long groupId, long userId, int start, int offset) {
        return ideaRepository.findUserFavoritedIdeas(companyId, groupId, userId, start, offset);
    }

    @Override
    public Idea findIdeaByUrlTitle(String urlTitle) {

        Idea idea = ideaRepository.findIdeaByUrlTitle(urlTitle);

        if (idea != null) {
            idea.setBariumUrl(getBariumUrl(idea));
        }

        return idea;
    }

    @Override
    public Idea findIdeaByUrlTitle(String urlTitle, boolean getBariumUrl) {

        Idea idea = ideaRepository.findIdeaByUrlTitle(urlTitle);

        if (getBariumUrl) {
            idea.setBariumUrl(getBariumUrl(idea));
        }

        return idea;
    }

    @Override
    public List<CommentItemVO> getPublicComments(Idea idea) {
        return getComments(idea.getIdeaContentPublic());
    }

    @Override
    public List<CommentItemVO> getPrivateComments(Idea idea) {
        return getComments(idea.getIdeaContentPrivate());
    }

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

    @Override
    @Transactional
    public void remove(String ideaId) throws RemoveIdeaException {
        Idea idea = ideaRepository.find(ideaId);

        remove(idea);
    }

    @Override
    @Transactional
    public void remove(Idea idea) throws RemoveIdeaException {

        System.out.println("IdeaServiceImpl - remove");

        if (idea != null) {
            try {
                IdeaObjectFields bariumIdea = bariumService.getBariumIdea(idea.getId());
            } catch (RuntimeException e){
                removeFromLiferay(idea);
                throw new RemoveIdeaException("Can not find idea with id " + idea.getId() + " in Barium. Have deleted idea in Liferay. ");
            }

            BariumResponse bariumResponse = bariumService.deleteBariumIdea(idea.getId());

            if (bariumResponse.getSuccess()) {
                removeFromLiferay(idea);
            } else {
                throw new RemoveIdeaException("Removing the idea " + idea.getTitle() + " from Barium failed.");
            }

        } else {
            throw new RemoveIdeaException("Can not find an idea with id " + idea.getId());
        }

    }

    @Override
    @Transactional
    public void removeFromLiferay(String ideaId) {
        Idea idea = ideaRepository.find(ideaId);

        removeFromLiferay(idea);
    }

    @Override
    @Transactional
    public void removeFromLiferay(Idea idea) {

        System.out.println("IdeaServiceImpl - remove");

        try {

            IdeaContent ideaContentPublic = idea.getIdeaContentPublic();
            IdeaContent ideaContentPrivate = idea.getIdeaContentPrivate();
            IdeaPerson ideaPerson = idea.getIdeaPerson();

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

    @Override
    @Transactional
    public void removeAll() {
        // Collection<Idea> all = ideaRepository.findAll();
        // for (Idea idea : all) {
        // ideaRepository.remove(idea);
        // }
    }

    @Override
    @Transactional
    public void removeFavorite(long companyId, long groupId, long userId, String urlTitle) {

        Idea idea = ideaRepository.findIdeaByUrlTitle(urlTitle);

        if (idea != null) {
            // Todo companyId och groupId behövs ej?
            ideaUserFavoriteRepository.removeByUserAndIdea(companyId, groupId, userId, idea.getId());
        }
    }

    @Override
    @Transactional
    public void removeLike(long companyId, long groupId, long userId, String urlTitle) {

        Idea idea = ideaRepository.findIdeaByUrlTitle(urlTitle);

        if (idea != null) {
            ideaUserLikeRepository.removeByUserAndIdea(companyId, groupId, userId, idea.getId());
        }
    }

    @Override
    @Transactional
    public Idea updateFromBarium(String ideaId) {

        Idea idea = ideaRepository.find(ideaId);

        idea = updateFromBarium(idea);

        return idea;
    }

    @Override
    @Transactional
    public Idea updateFromBarium(Idea idea) {

        LOGGER.info(" Update from Barium, idea: " + idea.getTitle());

        // Idea idea = findIdeaByBariumId(id);
        final String ideaId = idea.getId();

        String oldTitle = idea.getTitle(); // To know whether it has changed, in case we need to update in Barium

        // Make two calls asynchronously and simultaneously to speed up.
        Future<IdeaObjectFields> ideaObjectFieldsFuture = bariumService.asyncGetIdeaObjectFields(ideaId);
        Future<String> bariumIdeaPhase = bariumService.asyncGetIdeaPhaseFuture(ideaId);


        try {

            IdeaStatus oldStatus = idea.getStatus();
            populateIdea(ideaObjectFieldsFuture.get(), idea);

            int currentPhase = Integer.parseInt(idea.getPhase());
            int bariumPhase = Integer.parseInt(bariumIdeaPhase.get());

            for (int i = currentPhase; i < bariumPhase; i++) {
                addAutoComment(idea, autoCommentDefaultMessageNewPhase + " " + getIdeaPhaseString(i));
                idea.setPhase("" + (i + 1));
            }

            if (oldStatus.equals(IdeaStatus.PRIVATE_IDEA) && idea.getStatus().equals(IdeaStatus.PUBLIC_IDEA)) {
                addAutoComment(idea, autoCommentDefaultMessageBecomePublic);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        idea = ideaRepository.merge(idea);

        if (!oldTitle.equals(idea.getTitle())) {
            final Idea finalIdea = idea;

            // We may just as well do this asynchronously since we don't throw anything and don't return anything
            // from
            // here.
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    // We need to update the ideaSiteLink in Barium.
                    String ideaSiteLink = finalIdea.getIdeaSiteLink();
                    String newUrlTitle = generateNewUrlTitle(finalIdea.getTitle());
                    String newIdeaSiteLink = replaceLastPart(ideaSiteLink, newUrlTitle);
                    try {
                        bariumService.updateIdea(finalIdea.getId(), "siteLank", newIdeaSiteLink);
                    } catch (BariumException e) {
                        LOGGER.error("Failed to update idea " + finalIdea.getId() + " with new site link: "
                                + newIdeaSiteLink, e);
                    }
                }
            });
        }

        return idea;
    }

    private String getIdeaPhaseString(int i) {

        List<String> list = new ArrayList<String>();

        list.add("Gemensam utveckling");
        list.add("Idé");
        list.add("Värdering/Utredning");
        list.add("Koncept");
        list.add("Prövning");
        list.add("Kommersialisering");

        return list.get(i);

    }


    private void addAutoComment(Idea idea, String message) {
        try {
            String threadView = PropsKeys.DISCUSSION_THREAD_VIEW;
            MBMessageDisplay messageDisplay = mbMessageLocalService.getDiscussionMessageDisplay(
                    idea.getUserId(), idea.getGroupId(), IdeaContent.class.getName(), idea.getIdeaContentPrivate().getId(),
                    WorkflowConstants.STATUS_ANY, threadView);
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

    private boolean hasBeenSetToPublic(Idea idea, Future<String> bariulmIsPublicFtr) throws ExecutionException, InterruptedException {

        String bariulmIsPublic = bariulmIsPublicFtr.get();
        if (!bariulmIsPublic.equals(idea.getIsPublic())) {
            idea.setPhase(bariulmIsPublic);
            return true;
        }

        return false;
    }

    String replaceLastPart(String ideaSiteLink, String newUrlTitle) {
        return ideaSiteLink.substring(0, ideaSiteLink.lastIndexOf("/") + 1) + newUrlTitle;
    }

    private Idea findIdeaByBariumId(String bariumId) {
        return ideaRepository.find(bariumId);
    }

    // 06.15 every morning
    @Override
    @Scheduled(cron = "* 15 6 * * ?")
    public void updateAllIdeasFromBarium() {
        LOGGER.info("Updating all ideas from Barium...");

        // Do the transaction manually since we may run this in a separate thread.
        TransactionStatus transaction =
                transactionManager.getTransaction(new DefaultTransactionAttribute(
                        TransactionDefinition.PROPAGATION_REQUIRED));

        Collection<Idea> allIdeas = findAll();

        for (Idea idea : allIdeas) {
            try {
                updateFromBarium(idea);
            } catch (RuntimeException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        transactionManager.commit(transaction);

        LOGGER.info("Finished updating all ideas from Barium.");
    }

    public void asyncUpdateAllIdeasFromBarium() {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                updateAllIdeasFromBarium();
            }
        });
    }

    private void populateIdea(IdeaObjectFields ideaObjectFields, Idea idea) {

        idea.setTitle(ideaObjectFields.getInstanceName());
        idea.setUrlTitle(titleToUrlTitle(ideaObjectFields.getInstanceName()));

        IdeaContent ideaContentPublic = idea.getIdeaContentPublic();
        IdeaContent ideaContentPrivate = idea.getIdeaContentPrivate(); // TODO Vet vi att det ska vara private?

        // Default set idea status to private, then change if conditions are met
        String publik = ideaObjectFields.getPublik();
        if (publik != null && publik.equals("on")) {
            idea.setStatus(IdeaStatus.PUBLIC_IDEA);
        } else {
            idea.setStatus(IdeaStatus.PRIVATE_IDEA);
        }

        if (ideaContentPublic != null) {
            ideaContentPublic.setIntro(ideaObjectFields.getPublikintrotext());
            ideaContentPublic.setDescription(ideaObjectFields.getPublikbeskrivning());
        }

        if (ideaContentPrivate != null) {
            ideaContentPrivate.setSolvesProblem(ideaObjectFields.getBehov());
            ideaContentPrivate.setDescription(ideaObjectFields.getIde());
            ideaContentPrivate.setIdeaTested(ideaObjectFields.getTestat());
            ideaContentPrivate.setWantsHelpWith(ideaObjectFields.getKommavidare());

            ideaContentPrivate.setIdeaTransporterComment(ideaObjectFields.getIdetranportorensKommentar());
        }
    }

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

    @Override
    @Transactional
    public IdeaFile uploadFile(Idea idea, boolean publicIdea, String fileName, String contentType, InputStream inputStream)
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
            bariumService.uploadFile(idea, folderName, fileName, inputStream);
            IdeaFile ideaFile = new IdeaFile(idea.getCompanyId(), idea.getCompanyId(), idea.getUserId(), fileName, contentType);

            ideaFile.setIdeaContent(ideaContent);
            ideaFileRepository.merge(ideaFile);

            return ideaFile;

        } catch (BariumException e) {
            throw new FileUploadException(e);
        }
    }

    private List<ObjectEntry> getIdeaFiles(Idea idea, String folderName) throws BariumException {
        return bariumService.getIdeaFiles(idea, folderName);
    }

    @Override
    public ObjectEntry getObject(String id) throws BariumException {
        return bariumService.getObject(id);
    }

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

    private String titleToUrlTitle(String title) {
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

            MessageCreateDateComparator messageComparator = new MessageCreateDateComparator(false);

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
                    boolean isUserInnovationsslussenEmployee =
                            isUserInnovationsslussenEmployee(curCommentUserId, scopeGroupId);

                    CommentItemVO commentItem = new CommentItemVO();
                    commentItem.setCommentText(curCommentText);
                    commentItem.setCreateDate(createDate);
                    commentItem.setId(commentId);
                    commentItem.setName(curCommentUserFullName);
                    commentItem.setUserCreator(isUserIdeaCreator);
                    commentItem.setUserPrioCouncilMember(isUserPrioCouncilMember);
                    commentItem.setUserInnovationsslussenEmployee(isUserInnovationsslussenEmployee);

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

    protected boolean isUserInnovationsslussenEmployee(long userId, long groupId) {

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

    protected boolean isUserPrioCouncilMember(long userId, long groupId) {

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

    public void setTransactionManager(JpaTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    private String generateIdeaSiteLink(String schemeServerNamePort, String urlTitle) {
        return schemeServerNamePort + "/web/innovationsslussen/ide/-/idea/" + urlTitle;
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
