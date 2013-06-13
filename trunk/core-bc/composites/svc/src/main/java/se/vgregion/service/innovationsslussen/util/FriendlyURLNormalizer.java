package se.vgregion.service.innovationsslussen.util;

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

public class FriendlyURLNormalizer {

	public static String normalize(String friendlyURL) {
		return normalize(friendlyURL, null);
	}

	public static String normalize(String friendlyURL, char[] replaceChars) {
		if (Validator.isNull(friendlyURL)) {
			return friendlyURL;
		}

		friendlyURL = GetterUtil.getString(friendlyURL);
		friendlyURL = friendlyURL.toLowerCase();
		friendlyURL = Normalizer.normalizeToAscii(friendlyURL);

		char[] charArray = friendlyURL.toCharArray();

		for (int i = 0; i < charArray.length; i++) {
			char oldChar = charArray[i];

			char newChar = oldChar;

			if (ArrayUtil.contains(_REPLACE_CHARS, oldChar) || ((replaceChars != null) && ArrayUtil.contains(replaceChars, oldChar))) {

				newChar = CharPool.DASH;
			}

			if (oldChar != newChar) {
				charArray[i] = newChar;
			}
		}

		friendlyURL = new String(charArray);

		while (friendlyURL.contains(StringPool.DASH + StringPool.DASH)) {
			friendlyURL = StringUtil.replace(
				friendlyURL, StringPool.DASH + StringPool.DASH,
				StringPool.DASH);
		}

		if (friendlyURL.startsWith(StringPool.DASH)) {
			friendlyURL = friendlyURL.substring(1, friendlyURL.length());
		}

		if (friendlyURL.endsWith(StringPool.DASH)) {
			friendlyURL = friendlyURL.substring(0, friendlyURL.length() - 1);
		}

		return friendlyURL;
	}

	private static final char[] _REPLACE_CHARS = new char[] {
		' ', ',', '\\', '\'', '\"', '(', ')', '{', '}', '?', '#', '@', '+',
		'!', '~', ';', '$', '%'
	};

}