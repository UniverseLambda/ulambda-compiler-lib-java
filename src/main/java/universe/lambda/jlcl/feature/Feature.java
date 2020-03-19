/*
	Copyright 2020 Clément Saad

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

package universe.lambda.jlcl.feature;

import universe.lambda.jlcl.LanguageDefinition;

/**
 * A feature of this library.<br><br>
 *
 * Features are a set of parameters applied to a {@link LanguageDefinition.Builder} when it's {@link LanguageDefinition.Builder#build()} method is
 * called. Features are stored inside a {@link FeatureList}.<br><br>
 *
 * Features can have data of the form of key-value pairs of String. This data is set and get by {@link #setData} and
 * {@link Feature#getData} methods respectively.
 *
 * @since 0.2
 *
 * @see FeatureList
 */
public interface Feature {
	/**
	 * Returns the boolean indicating whether this {@code Feature} is enabled or not.
	 *
	 * @return the boolean value.
	 *
	 * @since 0.2
	 */
	String getName();

	/**
	 * Turns on or off this feature.
	 *
	 * @param enabled whether to enable this feature.
	 *
	 * @since 0.2
	 */
	void setEnabled(boolean enabled);

	/**
	 * Get whether this feature is enabled or not.
	 *
	 * @return {@code true} if this feature is enabled, {@code false} otherwise.
	 *
	 * @since 0.2
	 */
	boolean isEnabled();

	/**
	 * Set the specified data to the specified value.
	 *
	 * @param key key of data to set.
	 * @param value value to set to the data.
	 *
	 * @since 0.2
	 */
	void setData(String key, String value);

	/**
	 * Get the value of the specified data, defaulting it to {@code null} if not found.
	 *
	 * @param key key of the data.
	 * @return the value of the data, {@code null} if not found.
	 *
	 * @since 0.2
	 */
	default String getData(String key) {
		return getData(key, null);
	}

	/**
	 * Get the value of the specified data, defaulting to {@code defaultValue} if not found.
	 *
	 * @param key key of the data.
	 * @param defaultValue default value to return if the data is not found.
	 * @return the value of the {@code key} data, {@code defaultValue} if not found.
	 *
	 * @since 0.2
	 */
	String getData(String key, String defaultValue);

	/**
	 * Apply this feature parameters to the specified {@link LanguageDefinition} builder.
	 *
	 * @param builder to apply to this feature parameter.
	 *
	 * @since 0.2
	 */
	void apply(LanguageDefinition.Builder builder);
}
