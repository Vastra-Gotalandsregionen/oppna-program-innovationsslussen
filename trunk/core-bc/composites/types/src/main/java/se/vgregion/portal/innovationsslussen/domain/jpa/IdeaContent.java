package se.vgregion.portal.innovationsslussen.domain.jpa;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;
import se.vgregion.portal.innovationsslussen.domain.IdeaContentType;

/**
 * JPA entity class representing a IdeaContent for Innovationsslussen.
 * 
 * @author Erik Andersson
 * @author Simon Göransson - simon.goransson@monator.com - vgrid: simgo3
 * @company Monator Technologies AB
 */
@Entity
@Table(name = "vgr_innovationsslussen_idea_content")
public class IdeaContent extends AbstractEntity<Long> {

    // Primary Key

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Liferay Related
    @Column(name = "company_id")
    private long companyId;

    @Column(name = "group_id")
    private long groupId;

    @Column(name = "user_id")
    private long userId;

    // IdeaContent Related

    @Column(name = "intro")
    private String intro;

    @Lob
    // Todo Varför har vi denna annoteringen?
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "wants_help_with")
    private String wantsHelpWith;

    @Lob
    @Column(name = "solves_problem")
    private String solvesProblem;

    @Lob
    @Column(name = "idea_tested")
    private String ideaTested;

    @Column(name = "type")
    @Enumerated
    private IdeaContentType type;

    @Column(name = "state")
    private String state;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id", name = "idea_conten")
    private final Set<IdeaFile> ideaFiles = new HashSet<IdeaFile>();

    // Foreign
    @ManyToOne
    private Idea idea;

    @Lob
    @Column(name = "idea_transporter_comment")
    private String ideaTransporterComment;

    @Column(name = "prioritization_council_meeting")
    private String prioritizationCouncilMeeting;

    @Column(name = "prioritization_council_meeting_time")
    private Date prioritizationCouncilMeetingTime;

    @Column(name = "additional_idea_originators")
    private String additionalIdeaOriginators;

    /**
     * Constructor.
     */
    public IdeaContent() {
    }

    /**
     * Instantiates a new idea content.
     *
     * @param companyId the company id
     * @param groupId the group id
     * @param userId the user id
     */
    public IdeaContent(long companyId, long groupId, long userId) {
        this.companyId = companyId;
        this.groupId = groupId;
        this.userId = userId;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWantsHelpWith() {
        return wantsHelpWith;
    }

    public void setWantsHelpWith(String wantsHelpWith) {
        this.wantsHelpWith = wantsHelpWith;
    }

    public String getSolvesProblem() {
        return solvesProblem;
    }

    public void setSolvesProblem(String solvesProblem) {
        this.solvesProblem = solvesProblem;
    }

    public String getIdeaTested() {
        return ideaTested;
    }

    public void setIdeaTested(String ideaTested) {
        this.ideaTested = ideaTested;
    }

    public IdeaContentType getType() {
        return type;
    }

    public void setType(IdeaContentType type) {
        this.type = type;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }

    public Idea getIdea() {
        return idea;
    }

    public void setIdeaTransporterComment(String ideaTransporterComment) {
        this.ideaTransporterComment = ideaTransporterComment;
    }

    public String getIdeaTransporterComment() {
        return ideaTransporterComment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Set<IdeaFile> getIdeaFiles() {
        return ideaFiles;
    }

    /**
     * Adds the idea file.
     *
     * @param ideaFile the idea file
     */
    public void addIdeaFile(IdeaFile ideaFile) {
        // ideaFile.setIdeaContent(this);
        this.ideaFiles.add(ideaFile);
    }


    public void setPrioritizationCouncilMeeting(String prioritizationCouncilMeeting) {
        this.prioritizationCouncilMeeting = prioritizationCouncilMeeting;
    }

    public String getPrioritizationCouncilMeeting() {
        return prioritizationCouncilMeeting;
    }

    public void setAdditionalIdeaOriginators(String additionalIdeaOriginators) {
        this.additionalIdeaOriginators = additionalIdeaOriginators;
    }

    public String getAdditionalIdeaOriginators() {
        return additionalIdeaOriginators;
    }

    public Date getPrioritizationCouncilMeetingTime() {
        return prioritizationCouncilMeetingTime;
    }

    public void setPrioritizationCouncilMeetingTime(Date prioritizationCouncilMeetingTime) {
        this.prioritizationCouncilMeetingTime = prioritizationCouncilMeetingTime;
    }
}
