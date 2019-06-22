package lama;

public class LikeImplementationHelper {
	
	public static boolean match(String str, String regex, int regexPosition, int strPosition) {
		if (strPosition == str.length() && regexPosition == regex.length()) {
			return true;
		}
		if (regexPosition >= regex.length()) {
			return false;
		}
		char cur = regex.charAt(regexPosition);
		if (strPosition >= str.length()) {
			if (cur == '%') {
				return match(str, regex, regexPosition + 1, strPosition);
			} else {
				return false;
			}
		}
		switch (cur) {
		case '%':
			// match
			boolean foundMatch = match(str, regex, regexPosition, strPosition + 1);
			if (!foundMatch) {
				return match(str, regex, regexPosition + 1, strPosition);
			} else {
				return true;
			}
		case '_':
			return match(str, regex, regexPosition + 1, strPosition + 1);
		default:
			if (toUpper(cur) == toUpper(str.charAt(strPosition))) {
				return match(str, regex, regexPosition + 1, strPosition + 1);
			} else {
				return false;
			}
		}
	}

	private static char toUpper(char cur) {
		if (cur >= 'a' && cur <= 'z') {
			char c = (char) (cur + 'A' - 'a');
			return c;
		} else {
			return cur;
		}
	}

}
