/*
	Copyright 2019 Clément Saad

	This file is part of the uLambda Compiler Library.

	The uLambda Compiler Library is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	The uLambda Compiler Library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with the uLambda Compiler Library.  If not, see <https://www.gnu.org/licenses/>.
 */

package universe.lambda.jlcl.utils;

public class NumberUtil {
	/**
	 * String containing all valid characters for constituting a number
	 */
	private static final String numberChars =
			".0123456789AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";

	/**
	 * Method used to check if the input value is an integer
	 * @param value to test
	 * @return true if the value is an integer, false otherwise
	 */
	public static boolean isInteger(String value) {
		var prefix = getPrefix(value);
		var no_prefix = value.substring(prefix.length());
		return isPartOf(no_prefix, getIntegerCodepointSetByPrefix(getPrefix(value)));
	}

	/**
	 * Method used to check if the input value is a float
	 * @param value to test
	 * @return true if the value is a float, false otherwise
	 */
	public static boolean isFloat(String value, String[] suffixes) {
		if(countCharOccurrences(value, '.') > 1) {
			return false;
		}

		var prefix = getPrefix(value);
		var no_prefix = value.substring(prefix.length());

		if(getBaseByPrefix(prefix) <= 10) {
			for(String curr : suffixes) {
				if(no_prefix.endsWith(curr)) {
					no_prefix = no_prefix.substring(0, no_prefix.length() - curr.length());
					break;
				}
			}
		}

		var result = isPartOf(no_prefix, getFloatCodepointSetByPrefix(prefix));

		if(result) {
			int count = countCharOccurrences(no_prefix, '.');
			result = count <= 1;
		}
		return result;
	}

	/**
	 * Method used to check if the input String only contains code points specified in the input array
	 * @param value to test
	 * @param cps authorized code points
	 * @return {@code true} iff {@code value} is composed of the code points in {@code cps}
	 */
	private static boolean isPartOf(String value, int[] cps) {
		for(int v_i = 0; v_i < value.length(); v_i++) {
			var v_cp = value.codePointAt(v_i);
			var compResult = false;

			for (int c_cp : cps) {
				if (v_cp == c_cp) {
					compResult = true;
					break;
				}
			}

			if(!compResult) return false;
		}
		return true;
	}

	/**
	 * Method used to get the 'number prefix' of an integer.
	 * @param value to get the prefix from
	 * @return the number prefix if one
	 */
	public static String getPrefix(String value) {
		if(value.length() == 1 && value.equals("0")) {
			return "";
		}

		if(value.startsWith("0")) {
			if(Character.isLetter(value.codePointAt(1))) return value.substring(0, 2);
			return "0";
		}
		return "";
	}

	/**
	 * Util method to get the code points for a float by the prefix
	 * @param prefix number prefix
	 * @return float code points
	 */
	public static int[] getFloatCodepointSetByPrefix(String prefix) {
		return getCodepointSetByPrefix(prefix, true);
	}

	/**
	 * Util method to get the code points for an integer by prefix
	 * @param prefix number prefix
	 * @return integer code points
	 */
	public static int[] getIntegerCodepointSetByPrefix(String prefix) {
		return getCodepointSetByPrefix(prefix, false);
	}

	/**
	 * Method used to get code points for a number by prefix
	 * @param prefix number prefix
	 * @param isFloat flag indicating if it needs to add float-specifics code points
	 * @return number code points
	 */
	public static int[] getCodepointSetByPrefix(String prefix, boolean isFloat) {
		var base = getBaseByPrefix(prefix);
		return getCodepointSet(base, isFloat);
	}

	/**
	 * Method used to get code points for a number by base
	 * @param base of the number
	 * @param isFloat flag indicating if it needs to add float-specifics code points
	 * @return number code points
	 */
	public static int[] getCodepointSet(int base, boolean isFloat) {
		return getNumberChars(base, isFloat).codePoints().toArray();
	}

	/**
	 * Method used to get number base from number prefix
	 * @param prefix number prefix
	 * @return number base
	 */
	public static int getBaseByPrefix(String prefix) {
		switch (prefix.toLowerCase()) {
			default:
				return 10;
			case "0x":
				return 16;
			case "0":
				return 8;
			case "0b":
				return 2;
		}
	}

	/**
	 * Method used to get a String containing all authorized code points for a given base
	 * @param base number base
	 * @param isFloat flag indicating if it needs to add float-specifics code points
	 * @return String containing all authorized code points
	 */
	private static String getNumberChars(int base, boolean isFloat) {
		var start = (isFloat) ? 0 : 1;
		var end = 1 + Math.min(base, 10) + Math.max(0, (base - 10) * 2);
		return numberChars.substring(start, end);
	}

	/**
	 * Method used to get the number of occurrence of a given code point in a given String
	 * @param target to count from
	 * @param cp code point to count occurrence of
	 * @return number of occurrence
	 */
	private static int countCharOccurrences(String target, int cp) {
		var count = 0;

		for (int i = 0; i < target.length(); i++) {
			var curr = target.codePointAt(i);
			if (curr == cp) {
				count++;
			}
		}
		return count;
	}
}
