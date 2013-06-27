package se.vgregion.service.innovationsslussen.idea.permission;

public class IdeaPermissionChecker {

	private boolean hasPermissionViewIdeaPublic;
	private boolean hasPermissionViewIdeaPrivate;
	
	public IdeaPermissionChecker() {
	}

	public boolean getHasPermissionViewIdeaPublic() {
		return hasPermissionViewIdeaPublic;
	}

	public void setHasPermissionViewIdeaPublic(boolean hasPermissionViewIdeaPublic) {
		this.hasPermissionViewIdeaPublic = hasPermissionViewIdeaPublic;
	}

	public boolean getHasPermissionViewIdeaPrivate() {
		return hasPermissionViewIdeaPrivate;
	}

	public void setHasPermissionViewIdeaPrivate(boolean hasPermissionViewIdeaPrivate) {
		this.hasPermissionViewIdeaPrivate = hasPermissionViewIdeaPrivate;
	}

	


}
