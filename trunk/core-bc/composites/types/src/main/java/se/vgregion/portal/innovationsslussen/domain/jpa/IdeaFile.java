/**
 * 
 */
package se.vgregion.portal.innovationsslussen.domain.jpa;

import javax.persistence.*;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * @author Simon Göransson - simon.goransson@monator.com - vgrid: simgo3
 * @company Monator Technologies AB
 * 
 */
@Entity
@Table(name = "vgr_innovationsslussen_idea_file")
public class IdeaFile extends AbstractEntity<Long> {

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

    @Column(name = "name")
    private String name;

    @Column(name = "file_type")
    private String fileType;

    @ManyToOne
    @JoinColumn(name = "idea_content")
    private IdeaContent ideaContent;

    /**
     * Constructor.
     */
    public IdeaFile() {
    }

    public IdeaFile(long companyId, long groupId, long userId, String name, String fileType) {
        super();
        this.companyId = companyId;
        this.groupId = groupId;
        this.userId = userId;
        this.name = name;
        this.fileType = fileType;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public Long getId() {
        return id;
    }

    public IdeaContent getIdeaContent() {
        return ideaContent;
    }

    public void setIdeaContent(IdeaContent ideaContent) {
        this.ideaContent = ideaContent;
    }
}
