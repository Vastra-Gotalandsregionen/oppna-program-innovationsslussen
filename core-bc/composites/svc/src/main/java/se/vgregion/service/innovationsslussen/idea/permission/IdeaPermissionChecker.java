package se.vgregion.service.innovationsslussen.idea.permission;

public class IdeaPermissionChecker {

    private boolean hasPermissionAddCommentPublic;
    private boolean hasPermissionAddCommentPrivate;
    private boolean hasPermissionAddLike;
    private boolean hasPermissionAddFavorite;

    private boolean hasPermissionDeleteCommentPublic;
    private boolean hasPermissionDeleteCommentPrivate;

    private boolean hasPermissionDeleteLike;
    private boolean hasPermissionDeleteFavorite;

    private boolean hasPermissionUpdateFromBarium;

    private boolean hasPermissionViewCommentPublic;
    private boolean hasPermissionViewCommentPrivate;

    private boolean hasPermissionViewIdeaPublic;
    private boolean hasPermissionViewIdeaPrivate;

    private boolean hasPermissionViewInBarium;

    private boolean isIdeaOwner;

    /**
     * Instantiates a new idea permission checker.
     */
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

    public boolean getHasPermissionAddLike() {
        return hasPermissionAddLike;
    }

    public void setHasPermissionAddLike(boolean hasPermissionAddLike) {
        this.hasPermissionAddLike = hasPermissionAddLike;
    }

    public boolean getHasPermissionAddFavorite() {
        return hasPermissionAddFavorite;
    }

    public void setHasPermissionAddFavorite(boolean hasPermissionAddFavorite) {
        this.hasPermissionAddFavorite = hasPermissionAddFavorite;
    }

    public boolean getHasPermissionDeleteLike() {
        return hasPermissionDeleteLike;
    }

    public void setHasPermissionDeleteLike(boolean hasPermissionDeleteLike) {
        this.hasPermissionDeleteLike = hasPermissionDeleteLike;
    }

    public boolean getHasPermissionDeleteFavorite() {
        return hasPermissionDeleteFavorite;
    }

    public void setHasPermissionDeleteFavorite(boolean hasPermissionDeleteFavorite) {
        this.hasPermissionDeleteFavorite = hasPermissionDeleteFavorite;
    }

    public boolean getHasPermissionAddCommentPublic() {
        return hasPermissionAddCommentPublic;
    }

    public void setHasPermissionAddCommentPublic(
            boolean hasPermissionAddCommentPublic) {
        this.hasPermissionAddCommentPublic = hasPermissionAddCommentPublic;
    }

    public boolean getHasPermissionAddCommentPrivate() {
        return hasPermissionAddCommentPrivate;
    }

    public void setHasPermissionAddCommentPrivate(
            boolean hasPermissionAddCommentPrivate) {
        this.hasPermissionAddCommentPrivate = hasPermissionAddCommentPrivate;
    }

    public boolean getHasPermissionViewCommentPublic() {
        return hasPermissionViewCommentPublic;
    }

    public void setHasPermissionViewCommentPublic(
            boolean hasPermissionViewCommentPublic) {
        this.hasPermissionViewCommentPublic = hasPermissionViewCommentPublic;
    }

    public boolean getHasPermissionViewCommentPrivate() {
        return hasPermissionViewCommentPrivate;
    }

    public void setHasPermissionViewCommentPrivate(
            boolean hasPermissionViewCommentPrivate) {
        this.hasPermissionViewCommentPrivate = hasPermissionViewCommentPrivate;
    }

    public boolean isHasPermissionViewInBarium() {
        return hasPermissionViewInBarium;
    }

    public void setHasPermissionViewInBarium(boolean hasPermissionViewInBarium) {
        this.hasPermissionViewInBarium = hasPermissionViewInBarium;
    }

    public boolean isHasPermissionDeleteCommentPublic() {
        return hasPermissionDeleteCommentPublic;
    }

    public void setHasPermissionDeleteCommentPublic(
            boolean hasPermissionDeleteCommentPublic) {
        this.hasPermissionDeleteCommentPublic = hasPermissionDeleteCommentPublic;
    }

    public boolean isHasPermissionDeleteCommentPrivate() {
        return hasPermissionDeleteCommentPrivate;
    }

    public void setHasPermissionDeleteCommentPrivate(
            boolean hasPermissionDeleteCommentPrivate) {
        this.hasPermissionDeleteCommentPrivate = hasPermissionDeleteCommentPrivate;
    }

    public boolean getHasPermissionUpdateFromBarium() {
        return hasPermissionUpdateFromBarium;
    }

    public void setHasPermissionUpdateFromBarium(
            boolean hasPermissionUpdateFromBarium) {
        this.hasPermissionUpdateFromBarium = hasPermissionUpdateFromBarium;
    }

    public boolean getIsIdeaOwner() {
        return isIdeaOwner;
    }

    public void setIsIdeaOwner(boolean isIdeaOwner) {
        this.isIdeaOwner = isIdeaOwner;
    }




}
