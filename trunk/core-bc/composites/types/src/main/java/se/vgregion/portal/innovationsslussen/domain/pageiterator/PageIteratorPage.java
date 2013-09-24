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
