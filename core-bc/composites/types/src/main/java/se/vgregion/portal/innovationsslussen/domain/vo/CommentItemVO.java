package se.vgregion.portal.innovationsslussen.domain.vo;

import java.util.Date;

public class CommentItemVO {

	public CommentItemVO() {
	}

	public CommentItemVO(long id, String commentText, Date createDate, String name) {
		this.id = id;
		this.commentText = commentText;
		this.createDate = createDate;
		this.name = name;
	}
	
	private long id;
	private String commentText;
	private Date createDate;
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
}