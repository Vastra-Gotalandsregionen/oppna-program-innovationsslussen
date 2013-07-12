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
    private boolean isSelected;

    /** The is odd. */
    private boolean isOdd;

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
    public boolean getIsSelected() {
        return isSelected;
    }

    /**
     * Sets the selected.
     * 
     * @param isSelected
     *            the new selected
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * Checks if is odd.
     * 
     * @return true, if is odd
     */
    public boolean isOdd() {
        return isOdd;
    }

    /**
     * Sets the odd.
     * 
     * @param isOdd
     *            the new odd
     */
    public void setOdd(boolean isOdd) {
        this.isOdd = isOdd;
    }

}
