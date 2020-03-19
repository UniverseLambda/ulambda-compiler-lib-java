/*
	Copyright 2019, 2020 Clément Saad

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

/**
 * Class containing helpful methods to identify numbers (integers AND floats).
 *
 * @since 0.1
 */
public final class NumberUtil {
	/**
	 * {@link String} containing all valid characters for constituting a number.
	 *
	 * @since 0.1
	 */
	private static final String numberChars =
			".0123456789AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";

	/**
	 * Static-access-only class, so no instance :)
	 */
	private NumberUtil() {}

	/**
	 * Checks whether or not the input value is an integer.
	 *
	 * @param value value to test.
	 * @return {@code true} if the value is an integer, {@code false} otherwise.
	 *
	 * @since 0.1
	 */
	public static boolean isInteger(String value) {
		if(value.length() == 2 && Character.isAlphabetic(value.codePointAt(1))) return false;

		var prefix = getPrefix(value);
		var no_prefix = value.substring(prefix.length());
		return isPartOf(no_prefix, getIntegerCodepointSet(getPrefix(value)));
	}

	/**
	 * Checks whether or not the input value is a float.
	 *
	 * @param value value to test.
	 * @param suffixes recognized suffixes.
	 * @return {@code true} if the value is a float, {@code false} otherwise.
	 *
	 * @since 0.1
	 */
	public static boolean isFloat(String value, String[] suffixes) {
		if(countCharOccurrences(value, '.') > 1) {
			return false;
		}

		// FIX: "." is recognized as a valid float number, which may cause some problems with possible "." tokens.
		if(value.equals(".")) return false;

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

		var result = isPartOf(no_prefix, getFloatCodepointSet(prefix));

		if(result) {
			int count = countCharOccurrences(no_prefix, '.');
			result = count <= 1;
		}
		return result;
	}

	/**
	 * Checks whether or not {@code value} only contains code-points from {@code cps}.
	 *
	 * @param value value to test.
	 * @param cps authorized code-points.
	 * @return {@code true} iff {@code value} is composed of the code points in {@code cps}.
	 *
	 * @since 0.1
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
	 * Gets the 'number prefix' of the specified {@code value}.
	 *
	 * @param value value to get the prefix from.
	 * @return the prefix in {@code value} if any.
	 *
	 * @since 0.1
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
	 * Gets the code points for a float with the specified prefix.
	 *
	 * @param prefix prefix of the float.
	 * @return authorized code-points for a float.
	 *
	 * @since 0.2
	 */
	public static int[] getFloatCodepointSet(String prefix) {
		return getCodepointSet(prefix, true);
	}

	/**
	 * Gets authorized code-points for an integer with the specified prefix.
	 *
	 * @param prefix prefix of the integer.
	 * @return authorized code-points for an integer.
	 *
	 * @since 0.2
	 */
	public static int[] getIntegerCodepointSet(String prefix) {
		return getCodepointSet(prefix, false);
	}

	/**
	 * Gets authorized code-points for a number with the specified prefix.
	 *
	 * @param prefix prefix of the number.
	 * @param isFloat flag indicating whether it needs to add float-specifics code points.
	 * @return authorized code-points for the specified arguments.
	 *
	 * @since 0.2
	 */
	public static int[] getCodepointSet(String prefix, boolean isFloat) {
		var base = getBaseByPrefix(prefix);
		return getCodepointSet(base, isFloat);
	}

	/**
	 * Gets authorized code-points for a number of the specified base.
	 *
	 * @param base base of the number.
	 * @param isFloat flag indicating whether it needs to add float-specifics code points.
	 * @return authorized code-points for the specified arguments.
	 *
	 * @since 0.1
	 */
	public static int[] getCodepointSet(int base, boolean isFloat) {
		return getNumberChars(base, isFloat).codePoints().toArray();
	}

	/**
	 * Gets the base for the given number prefix.
	 *
	 * @param prefix prefix of the number.
	 * @return base of the prefix.
	 *
	 * @since 0.1
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
	 * Gets a {@link String} containing all authorized code-points for the given base.
	 *
	 * @param base base of the number.
	 * @param isFloat flag indicating whether it needs to add float-specifics code-points.
	 * @return String containing all authorized code points.
	 *
	 * @since 0.1
	 */
	private static String getNumberChars(int base, boolean isFloat) {
		var start = (isFloat) ? 0 : 1;
		var end = 1 + Math.min(base, 10) + Math.max(0, (base - 10) * 2);
		return numberChars.substring(start, end);
	}

	/**
	 * Gets the occurrence count of the given code-point in the given {@link String}.
	 *
	 * @param target {@code String} to count from.
	 * @param cp code-point to count occurrence of.
	 * @return occurrence count.
	 *
	 * @since 0.1
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
