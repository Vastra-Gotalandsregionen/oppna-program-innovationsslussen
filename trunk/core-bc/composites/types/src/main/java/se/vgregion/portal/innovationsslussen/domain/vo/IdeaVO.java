package se.vgregion.portal.innovationsslussen.domain.vo;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

public class IdeaVO {

	private String description;
	private String descriptionShort;
	private int phase;
	private String solvesProblem;
	private String title;
	private String wantsHelpWith;
	
	private String vgrId;
	private String name;
	private String phone;
	private String administrativeUnit;
	private String jobPosition;

	private String bariumId;
	private long ideaId;
	private long userId;
	private long companyId;
	private long groupId;
	

	public IdeaVO() {
	}


	public String getDescriptionShort() {
		return descriptionShort;
	}


	public void setDescriptionShort(String descriptionShort) {
		this.descriptionShort = descriptionShort;
	}


	public int getPhase() {
		return phase;
	}


	public void setPhase(int phase) {
		this.phase = phase;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getSolvesProblem() {
		return solvesProblem;
	}


	public void setSolvesProblem(String solvesProblem) {
		this.solvesProblem = solvesProblem;
	}


	public String getWantsHelpWith() {
		return wantsHelpWith;
	}


	public void setWantsHelpWith(String wantsHelpWith) {
		this.wantsHelpWith = wantsHelpWith;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getAdministrativeUnit() {
		return administrativeUnit;
	}


	public void setAdministrativeUnit(String administrativeUnit) {
		this.administrativeUnit = administrativeUnit;
	}


	public String getJobPosition() {
		return jobPosition;
	}


	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}


	public String getVgrId() {
		return vgrId;
	}


	public void setVgrId(String vgrId) {
		this.vgrId = vgrId;
	}


	public String getBariumId() {
		return bariumId;
	}


	public void setBariumId(String bariumId) {
		this.bariumId = bariumId;
	}


	public long getUserId() {
		return userId;
	}


	public void setUserId(long userId) {
		this.userId = userId;
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


	public long getIdeaId() {
		return ideaId;
	}


	public void setIdeaId(long ideaId) {
		this.ideaId = ideaId;
	}

	
	
	
	
}
