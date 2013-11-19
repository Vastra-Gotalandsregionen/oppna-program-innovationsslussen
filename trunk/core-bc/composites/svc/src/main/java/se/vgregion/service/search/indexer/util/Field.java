package se.vgregion.service.search.indexer.util;

import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Simon Görnsson
 * @company Monator Technologies AB
 */

public class Field extends com.liferay.portal.kernel.search.Field {

	public static final String CREATED = "created";
	public static final String FAVOURITES_COUNT = "favouritesCount";
	public static final String LIKES_COUNT = "likesCount";
	public static final String TITLE_ = "title";
	public static final String STATUS = "status";
	public static final String URL_TITLE = "urlTitle";
	public static final String USER_SCREEN_NAME = "userScreenName";




    public Field(String name, Map<Locale, String> localizedValues) {
        super(name, localizedValues);
    }
}