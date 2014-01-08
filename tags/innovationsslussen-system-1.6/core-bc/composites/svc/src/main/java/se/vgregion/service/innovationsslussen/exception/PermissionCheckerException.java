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
 * The Class PermissionCheckerException.
 */
public class PermissionCheckerException extends Exception {

    private static final long serialVersionUID = -3788118533211706040L;

    /**
     * Instantiates a new permission checker exception.
     */
    public PermissionCheckerException() {
        super();
    }

    /**
     * Instantiates a new permission checker exception.
     *
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public PermissionCheckerException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Instantiates a new permission checker exception.
     *
     * @param arg0 the arg0
     */
    public PermissionCheckerException(String arg0) {
        super(arg0);
    }

    /**
     * Instantiates a new permission checker exception.
     *
     * @param arg0 the arg0
     */
    public PermissionCheckerException(Throwable arg0) {
        super(arg0);
    }



}
