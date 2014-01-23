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

package se.vgregion.portal.innovationsslussen.domain.pageiterator;

/**
 * The Class PageIteratorPage.
 * 
 * @author Erik Andersson
 * @company Monator Technologies AB
 * 
 * 
 */
public class PageIteratorPage {

    /** The pagenumber. */
    private int pagenumber;

    /** The is selected. */
    private boolean selected;

    /** The is odd. */
    private boolean odd;

    /**
     * Gets the pagenumber.
     * 
     * @return the pagenumber
     */
    public int getPagenumber() {
        return pagenumber;
    }

    /**
     * Sets the pagenumber.
     * 
     * @param pagenumber
     *            the new pagenumber
     */
    public void setPagenumber(int pagenumber) {
        this.pagenumber = pagenumber;
    }

    /**
     * Gets the checks if is selected.
     * 
     * @return the checks if is selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the selected.
     * 
     * @param selected
     *            the new selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Checks if is odd.
     * 
     * @return true, if is odd
     */
    public boolean isOdd() {
        return odd;
    }

    /**
     * Sets the odd.
     * 
     * @param odd
     *            the new odd
     */
    public void setOdd(boolean odd) {
        this.odd = odd;
    }

}
