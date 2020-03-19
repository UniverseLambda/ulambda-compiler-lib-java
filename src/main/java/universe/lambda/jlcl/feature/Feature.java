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

package universe.lambda.jlcl.feature;

import universe.lambda.jlcl.LanguageDefinition;

/**
 * A feature of this library.<br><br>
 *
 * Features are a set of parameters applied to a {@link LanguageDefinition.Builder} when its {@link LanguageDefinition.Builder#build()} method is
 * called. Features are stored inside a {@link FeatureList}.<br><br>
 *
 * Features can have data of the form of key-value pairs of {@code String}. This data can be set and get by {@link #setData} and
 * {@link #getData} methods respectively.
 *
 * @since 0.2
 *
 * @see FeatureList
 */
public interface Feature {
	/**
	 * Returns the name of this {@code Feature}.
	 *
	 * @return the name.
	 *
	 * @since 0.2
	 */
	String getName();

	/**
	 * Turns on or off this {@code Feature}.
	 *
	 * @param enabled whether or not to enable this {@code Feature}.
	 *
	 * @since 0.2
	 */
	void setEnabled(boolean enabled);

	/**
	 * Returns whether or not this {@code Feature} is enabled.
	 *
	 * @return {@code true} if this feature is enabled, {@code false} otherwise.
	 *
	 * @since 0.2
	 */
	boolean isEnabled();

	/**
	 * Sets the specified data to the specified value.
	 *
	 * @param key key of data to set.
	 * @param value value to set to the data.
	 *
	 * @since 0.2
	 */
	void setData(String key, String value);

	/**
	 * Gets the value of the specified data, returning {@code null} if {@code key} does not exists.
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
	 * Gets the value of the specified data, returning {@code defaultValue} if {@code key} does not exists.
	 *
	 * @param key key of the data.
	 * @param defaultValue default value to return if the data does not exist.
	 * @return the value of the {@code key} data, {@code defaultValue} if not found.
	 *
	 * @since 0.2
	 */
	String getData(String key, String defaultValue);

	/**
	 * Applies this {@code Feature} parameters to the specified {@link LanguageDefinition} builder.
	 *
	 * @param builder builder to apply this {@code Feature} to.
	 *
	 * @since 0.2
	 */
	void apply(LanguageDefinition.Builder builder);
}
