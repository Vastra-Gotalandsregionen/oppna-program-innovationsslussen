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

package se.vgregion.service.innovationsslussen.exception;

/**
 * The Class FavoriteException.
 */
public class FavoriteException extends Exception {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7173396209424362762L;

    /**
     * Instantiates a new favorite exception.
     */
    public FavoriteException() {
        super();
    }

    /**
     * Instantiates a new favorite exception.
     *
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public FavoriteException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Instantiates a new favorite exception.
     *
     * @param arg0 the arg0
     */
    public FavoriteException(String arg0) {
        super(arg0);
    }

    /**
     * Instantiates a new favorite exception.
     *
     * @param arg0 the arg0
     */
    public FavoriteException(Throwable arg0) {
        super(arg0);
    }



}
