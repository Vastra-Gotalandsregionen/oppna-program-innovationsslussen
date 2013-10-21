package se.vgregion.service.innovationsslussen.util;

import com.ibm.icu.text.Transliterator;
import com.liferay.portal.kernel.util.StringUtil;

public final class Normalizer {

    private Normalizer() {
    }

    /**
     * Normalize to ascii.
     *
     * @param s the s
     * @return the string
     */
    public static String normalizeToAscii(String s) {
        if (!hasNonASCIICode(s)) {
            return s;
        }



        String normalizedText = transliterator.transliterate(s);

        return StringUtil.replace(
                normalizedText, UNICODE_TEXT, NORMALIZED_TEXT);
    }

    private static boolean hasNonASCIICode(String s) {

        final int max = 127;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) > max) {
                return true;
            }
        }

        return false;
    }

    private static final String NORMALIZED_TEXT = "l";

    private static final String UNICODE_TEXT = "\u0142";

    private static Transliterator transliterator = Transliterator.getInstance(
            "NFD; [:Nonspacing Mark:] Remove; NFC");


}
