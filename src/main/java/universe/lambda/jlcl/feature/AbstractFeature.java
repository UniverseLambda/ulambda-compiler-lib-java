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
import universe.lambda.jlcl.LanguageDefinitionInflater;

import java.util.HashMap;

/**
 * This class provides a skeletal implementation of the {@link Feature} interface to minimize effort required to
 * implement this interface.
 *
 * @since 0.2
 */
public abstract class AbstractFeature implements Feature {
	/**
	 * Name of the feature. Used by {@link LanguageDefinition} and {@link LanguageDefinitionInflater} to index features.
	 *
	 * @since 0.2
	 */
	private final String name;

	/**
	 * Flag indicating if the feature is enabled.
	 *
	 * @since 0.2
	 */
	private boolean enabled;

	/**
	 * HashMap used to store the data of the feature.
	 *
	 * @since 0.2
	 */
	private HashMap<String, String> data = new HashMap<>();

	/**
	 * Construct a feature with the given name.
	 *
	 * @param name name of the feature.
	 *
	 * @since 0.2
	 */
	public AbstractFeature(String name) {
		this.name = name;
		this.enabled = false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setData(String key, String value) {
		data.put(key, value);
	}

	@Override
	public String getData(String key, String defaultValue) {
		if (data.containsKey(key)) return data.get(key);
		return defaultValue;
	}
}
