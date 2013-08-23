package se.vgregion.service.innovationsslussen.ldap;

import org.apache.commons.collections.BeanMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("serial")
public class TextFrag {

    private final List<TextFrag> frags = new ArrayList<TextFrag>();
    private String text;


    public TextFrag() {
        super();
    }

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


    public static TextFrag toFrag(String text, char start, char end, String splitExp) {
        Parser parser = new Parser();
        parser.text = text;
        parser.start = start;
        parser.end = end;

        TextFrag frag = new TextFrag();
        ;
        parser.toFrag(frag);
        return frag;
    }

    public static TextFrag toFrag(String text) {
        Parser parser = new Parser();
        parser.text = text;

        TextFrag frag = new TextFrag();
        ;
        parser.toFrag(frag);
        return frag;
    }

    public List<TextFrag> getFrags() {
        return frags;
    }

    public static class Parser {
        int cursor = 0;
        StringBuilder sb = new StringBuilder();
        //public String text, splitExp = Pattern.quote(",");
        public String text, splitExp = "[,]";
        public char start = '(', end = ')';

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

        TextFrag mkAndAddFrags(TextFrag parent) {
            String newText = sb.toString().trim();
            if (isBlanc(newText)) {
                return null;
            }
            String[] parts = newText.split(splitExp);
            List<TextFrag> frags = new ArrayList<TextFrag>();
            for (String part : parts) {
                frags.add(mkAndAddFrag(parent, part));
            }
            if (frags.isEmpty()) {
                return null;
            }
            return frags.get(frags.size() - 1);
        }

        TextFrag mkAndAddFrag(TextFrag parent, String newText) {
            TextFrag tf = new TextFrag(newText.trim());
            sb.delete(0, sb.length());
            parent.getFrags().add(tf);
            return tf;
        }

        boolean isBlanc(String s) {
            if (s == null) {
                return false;
            }
            return "".equals(s.trim());
        }

    }

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
        parser.text = s;
        parser.toFrag(parent);
        System.out.println(parent);

        System.out.println("Finding: " + parent.find("zip character varying"));
    }

}
