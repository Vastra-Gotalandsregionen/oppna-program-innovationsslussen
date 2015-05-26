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

package se.vgregion.service.innovationsslussen.ldap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.BeanMap;

/**
 * The Class TextFrag.
 */
public class TextFrag {


    private final List<TextFrag> frags = new ArrayList<TextFrag>();

    /** The text. */
    private String text;


    /**
     * Instantiates a new text frag.
     */
    public TextFrag() {
        super();
    }

    /**
     * Instantiates a new text frag.
     *
     * @param text the text
     */
    public TextFrag(String text) {
        super();
        setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    /**
     * To frag.
     *
     * @param text the text
     * @param start the start
     * @param end the end
     * @param splitExp the split exp
     * @return the text frag
     */
    public static TextFrag toFrag(String text, char start, char end, String splitExp) {
        Parser parser = new Parser();
        parser.text = text;
        parser.start = start;
        parser.end = end;

        TextFrag frag = new TextFrag();
        parser.toFrag(frag);
        return frag;
    }

    /**
     * To frag.
     *
     * @param text the text
     * @return the text frag
     */
    public static TextFrag toFrag(String text) {
        Parser parser = new Parser();
        parser.text = text;

        TextFrag frag = new TextFrag();
        parser.toFrag(frag);
        return frag;
    }

    public List<TextFrag> getFrags() {
        return frags;
    }

    /**
     * The Class Parser.
     */
    public static class Parser {

        /** The cursor. */
        private int cursor = 0;

        /** The sb. */
        private final StringBuilder sb = new StringBuilder();
        //public String text, SPLIT_EXP = Pattern.quote(",");
        /** The split exp. */
        private String text;

        private static final String SPLIT_EXP = "[,]";

        /** The end. */
        private char start = '(', end = ')';

        /**
         * To frag.
         *
         * @param parent the parent
         */
        public void toFrag(TextFrag parent) {
            while (text.length() > cursor) {
                char c = text.charAt(cursor++);

                if (c == end) {
                    mkAndAddFrags(parent);
                    return;
                }

                if (c == start) {
                    TextFrag newFrag = mkAndAddFrags(parent);

                    toFrag(newFrag);
                    continue;
                }
                sb.append(c);

            }
            mkAndAddFrags(parent);
        }

        /**
         * Mk and add frags.
         *
         * @param parent the parent
         * @return the text frag
         */
        TextFrag mkAndAddFrags(TextFrag parent) {
            String newText = sb.toString().trim();
            if (isBlanc(newText)) {
                return null;
            }
            String[] parts = newText.split(SPLIT_EXP);
            List<TextFrag> frags = new ArrayList<TextFrag>();
            for (String part : parts) {
                frags.add(mkAndAddFrag(parent, part));
            }
            if (frags.isEmpty()) {
                return null;
            }
            return frags.get(frags.size() - 1);
        }

        /**
         * Mk and add frag.
         *
         * @param parent the parent
         * @param newText the new text
         * @return the text frag
         */
        TextFrag mkAndAddFrag(TextFrag parent, String newText) {
            TextFrag tf = new TextFrag(newText.trim());
            sb.delete(0, sb.length());
            parent.getFrags().add(tf);
            return tf;
        }

        /**
         * Checks if is blanc.
         *
         * @param s the s
         * @return true, if is blanc
         */
        boolean isBlanc(String s) {
            if (s == null) {
                return false;
            }
            return "".equals(s.trim());
        }

    }

    /**
     * Find.
     *
     * @param match the match
     * @return the text frag
     */
    public TextFrag find(String match) {
        if (match.equals(getText())) {
            return this;
        }
        for (TextFrag child : getFrags()) {
            TextFrag finding = child.find(match);
            if (finding != null) {
                return finding;
            }
        }
        return null;
    }

    private boolean toStringRunning = false;

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        try {
            if (toStringRunning) {
                return " recursive call to " + getClass().getSimpleName() + ".toString()";
            }
            toStringRunning = true;
            return new HashMap(new BeanMap(this)).toString();
        } finally {
            toStringRunning = false;
        }
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        String s = "CREATE TABLE address\n" + "(\n" + "  addressid bigint NOT NULL,\n" + "  companyid bigint,\n"
                + "  userid bigint,\n" + "  username character varying(75),\n"
                + "  createdate timestamp without time zone,\n" + "  modifieddate timestamp without time zone,\n"
                + "  classnameid bigint,\n" + "  classpk bigint,\n" + "  street1 character varying(75),\n"
                + "  street2 character varying(75),\n" + "  street3 character varying(75),\n"
                + "  city character varying(75),\n" + "  zip character varying(75,7),\n" + "  regionid bigint,\n"
                + "  countryid bigint,\n" + "  typeid integer,\n" + "  mailing boolean,\n"
                + "  primary_ boolean,\n" + "  CONSTRAINT address_pkey PRIMARY KEY (addressid )\n" + ")";

        Parser parser = new Parser();
        TextFrag parent = new TextFrag();
        //parser.text = "hej(och)hå";

    }

}
