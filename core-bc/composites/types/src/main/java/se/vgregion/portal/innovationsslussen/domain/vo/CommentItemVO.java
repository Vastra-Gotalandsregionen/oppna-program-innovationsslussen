package se.vgregion.portal.innovationsslussen.domain.vo;

import java.util.Date;

public class CommentItemVO {

    /**
     * Instantiates a new comment item vo.
     */
    public CommentItemVO() {
    }

    /**
     * Instantiates a new comment item vo.
     *
     * @param id the id
     * @param commentText the comment text
     * @param createDate the create date
     * @param name the name
     */
    public CommentItemVO(long id, String commentText, Date createDate, String name) {
        this.id = id;
        this.commentText = commentText;
        this.createDate = createDate;
        this.name = name;
    }

    private long id;
    private String commentText;
    private Date createDate;
    private boolean userCreator;
    private boolean userInnovationsslussenEmployee;
    private boolean userPrioCouncilMember;
    private String name;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getCommentText() {
        return commentText;
    }
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isUserCreator() {
        return userCreator;
    }

    public void setUserCreator(boolean userCreator) {
        this.userCreator = userCreator;
    }

    public boolean isUserPrioCouncilMember() {
        return userPrioCouncilMember;
    }

    public void setUserPrioCouncilMember(boolean userPrioCouncilMember) {
        this.userPrioCouncilMember = userPrioCouncilMember;
    }

    public boolean isUserInnovationsslussenEmployee() {
        return userInnovationsslussenEmployee;
    }

    public void setUserInnovationsslussenEmployee( boolean userInnovationsslussenEmployee) {
        this.userInnovationsslussenEmployee = userInnovationsslussenEmployee;
    }
}