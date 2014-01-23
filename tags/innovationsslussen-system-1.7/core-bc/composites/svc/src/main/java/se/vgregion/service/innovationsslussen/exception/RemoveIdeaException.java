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
 * The Class RemoveIdeaException.
 */
public class RemoveIdeaException extends Exception {

    private static final long serialVersionUID = 1024790047562887785L;

    /**
     * Instantiates a new removes the idea exception.
     */
    public RemoveIdeaException() {
        super();
    }

    /**
     * Instantiates a new removes the idea exception.
     *
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public RemoveIdeaException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Instantiates a new removes the idea exception.
     *
     * @param arg0 the arg0
     */
    public RemoveIdeaException(String arg0) {
        super(arg0);
    }

    /**
     * Instantiates a new removes the idea exception.
     *
     * @param arg0 the arg0
     */
    public RemoveIdeaException(Throwable arg0) {
        super(arg0);
    }



}
