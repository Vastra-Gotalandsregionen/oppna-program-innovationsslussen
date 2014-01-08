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
