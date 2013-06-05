package se.vgregion.service.idea.util;

import com.ibm.icu.text.Transliterator;
import com.liferay.portal.kernel.util.StringUtil;

public class Normalizer {

	public static String normalizeToAscii(String s) {
		if (!_hasNonASCIICode(s)) {
			return s;
		}
		
		

		String normalizedText = _transliterator.transliterate(s);

		return StringUtil.replace(
			normalizedText, _UNICODE_TEXT, _NORMALIZED_TEXT);
	}

	private static boolean _hasNonASCIICode(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) > 127) {
				return true;
			}
		}

		return false;
	}

	private static final String _NORMALIZED_TEXT = "l";

	private static final String _UNICODE_TEXT = "\u0142";

	private static Transliterator _transliterator = Transliterator.getInstance(
		"NFD; [:Nonspacing Mark:] Remove; NFC");	

	
}
