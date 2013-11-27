package se.vgregion.service.search.indexer.util;

import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Simon Görnsson
 * @company Monator Technologies AB
 */

public class Field extends com.liferay.portal.kernel.search.Field {

	//Date
    public static final String CREATED = "created";
	public static final String LAST_UPDATED = "lastUpdated";

    //Count
	public static final String FAVOURITES_COUNT = "favouritesCount";

    //Basic
    public static final String IDEA_ID = "ideaId‘";
    public static final String TITLE = "title";
    public static final String STATUS = "status";
    public static final String URL_TITLE = "urlTitle";

    //Idea
    public static final String PHASE = "phase";

    //Idea content
    public static final String PUBLIC_INTRO = "publicIntro";
    public static final String PUBLIC_DESCRIPTION = "publicDescription";
    public static final String PUBLIC_WANTS_HELP_WITH = "publicWantsHelpWith";
    public static final String PUBLIC_SOLVES_PROBLEM = "publicSolvesProblem";
    public static final String PUBLIC_IDEA_TESTED = "publicIdeaTested";
    public static final String PUBLIC_STATE = "publicState";
    public static final String PUBLIC_LAST_COMMENT_DATE = "publicLastCommentDate";
	public static final String PUBLIC_LIKES_COUNT = "publicLikesCount";

    public static final String PRIVATE_INTRO = "privateIntro";
    public static final String PRIVATE_DESCRIPTION = "privateDescription";
    public static final String PRIVATE_WANTS_HELP_WITH = "privateWantsHelpWith";
    public static final String PRIVATE_SOLVES_PROBLEM = "privateSolvesProblem";
    public static final String PRIVATE_IDEA_TESTED = "privateIdeaTested";
    public static final String PRIVATE_STATE = "privateState";
    public static final String PRIVATE_LAST_COMMENT_DATE = "privateLastCommentDate";
	public static final String PRIVATE_LIKES_COUNT = "privateLikesCount";

    //Idea person
    public static final String VGRID = "vgrId";
    public static final String USER_NAME = "userName";
    public static final String EMAIL = "email";

    // Comments
    public static final String PUBLIC_COMMENT_AUTHOR_IDS = "publicCommentAuthorIds";
    public static final String PUBLIC_COMMENT_AUTHOR_SCREEN_NAMES = "publicCommentAuthorScreenNames";
    public static final String PUBLIC_COMMENT_CREATE_DATES = "publicCommentCreateDates";
    public static final String PUBLIC_COMMENT_IDS = "publicCommentIds";
    public static final String PUBLIC_COMMENT_TEXTS = "publicCommentTexts";
    public static final String PUBLIC_COMMENT_COUNT = "publicCommentCount";

    public static final String PRIVATE_COMMENT_AUTHOR_IDS = "privateCommentAuthorIds";
    public static final String PRIVATE_COMMENT_AUTHOR_SCREEN_NAMES = "privateCommentAuthorScreenNames";
    public static final String PRIVATE_COMMENT_CREATE_DATES = "privateCommentCreateDates";
    public static final String PRIVATE_COMMENT_IDS = "privateCommentIds";
    public static final String PRIVATE_COMMENT_TEXTS = "privateCommentTexts";
    public static final String PRIVATE_COMMENT_COUNT = "privateCommentCount";


    public Field(String name, Map<Locale, String> localizedValues) {
        super(name, localizedValues);
    }
}