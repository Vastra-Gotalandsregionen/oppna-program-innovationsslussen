/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.service.innovationsslussen.idea.permission;


public final class IdeaActionKeys {

    private IdeaActionKeys() {
    }

    public static final String ADD_COMMENT_PUBLIC = "ADD_COMMENT_PUBLIC";
    public static final String ADD_COMMENT_PRIVATE = "ADD_COMMENT_PRIVATE";
	public static final String ADD_DOCUMENT_PUBLIC = "ADD_DOCUMENT_PUBLIC";
	public static final String ADD_DOCUMENT_PRIVATE = "ADD_DOCUMENT_PUBLIC";
    public static final String ADD_LIKE = "ADD_LIKE";
    public static final String ADD_FAVORITE = "ADD_FAVORITE";

    public static final String CREATE_IDEA = "CREATE_IDEA";
    public static final String CREATE_IDEA_FOR_OTHER_USER = "CREATE_IDEA_FOR_OTHER_USER";

    public static final String DELETE = "DELETE";
    public static final String DELETE_COMMENT_PUBLIC = "DELETE_COMMENT_PUBLIC";
    public static final String DELETE_COMMENT_PRIVATE = "DELETE_COMMENT_PRIVATE";
    public static final String DELETE_LIKE = "DELETE_LIKE";
    public static final String DELETE_FAVORITE = "DELETE_FAVORITE";

    public static final String PERMISSIONS = "PERMISSIONS";

    public static final String UPDATE_FROM_BARIUM = "UPDATE_FROM_BARIUM";

    public static final String VIEW_COMMENT_PUBLIC = "VIEW_COMMENT_PUBLIC";
    public static final String VIEW_COMMENT_PRIVATE = "VIEW_COMMENT_PRIVATE";

    public static final String VIEW_IDEA_PUBLIC = "VIEW_IDEA_PUBLIC";
    public static final String VIEW_IDEA_PRIVATE = "VIEW_IDEA_PRIVATE";

    public static final String VIEW_IN_BARIUM = "VIEW_IN_BARIUM";

}
