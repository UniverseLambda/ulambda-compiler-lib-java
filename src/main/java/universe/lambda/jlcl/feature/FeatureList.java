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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Immutable container for {@code Feature}.<br><br>
 *
 * It has a {@code Builder} class used to construct new instances.<br><br>
 *
 * @since 0.2
 */
public class FeatureList {
	/**
	 * The array backing this {@code FeatureList}.
	 *
	 * @since 0.2
	 */
	private Feature[] features;

	/**
	 * Private constructor. It is not intended for external consumption.
	 *
	 * @param features backing array.
	 *
	 * @since 0.2
	 */
	private FeatureList(Feature[] features) {
		this.features = features;
	}

	/**
	 * Applies all enabled {@link Feature}s of this list to the specified {@link LanguageDefinition} builder.
	 *
	 * @param builder builder to apply this list to.
	 *
	 * @since 0.2
	 */
	public void apply(LanguageDefinition.Builder builder) {
		for(var curr: features) {
			if(curr.isEnabled())
				curr.apply(builder);
		}
	}

	/**
	 * Class used to build a {@code FeatureList} instance. It is the only way to obtain an instance of {@code FeatureList}.<br><br>
	 *
	 * It enables some common {@link Feature}s by default:
	 * <ul>
	 *     <li>identifier ({@link IdentifierFeature})</li>
	 *     <li>character ({@link CharacterFeature})</li>
	 *     <li>string ({@link StringFeature})</li>
	 *     <li>integer ({@link IntegerFeature})</li>
	 *     <li>float ({@link FloatFeature})</li>
	 * </ul>
	 *
	 * If a {@code Feature} needs to be enable, it must be first added to this {@code Builder} before being enabled.
	 * Because of this, {@code Feature}s can be present in this {@code Builder} while being disable.
	 *
	 * @since 0.2
	 */
	public static class Builder {
		/**
		 * HashMap backing the {@code Builder}.
		 *
		 * @since 0.2
		 */
		private HashMap<String, Feature> featureList = new HashMap<>();

		/**
		 * Default constructor. Enables all defaults features.
		 *
		 * @since 0.2
		 *
		 * @see Builder#enableDefaults()
		 */
		public Builder() {
			indexFeatures();
			enableDefaults();
		}

		/**
		 * Indexes internal features.
		 *
		 * @since 0.2
		 */
		private void indexFeatures() {
			addFeature(new IdentifierFeature());
			addFeature(new CharacterFeature());
			addFeature(new StringFeature());
			addFeature(new IntegerFeature());
			addFeature(new FloatFeature());
		}

		/**
		 * Adds a feature to this list.
		 *
		 * @param feature feature to add to this list.
		 *
		 * @since 0.2
		 */
		private void addFeature(Feature feature) {
			featureList.put(feature.getName(), feature);
		}

		/**
		 * Enables defaults features. The default features are:
		 * <ul>
		 *     <li>identifier ({@link IdentifierFeature})</li>
		 *     <li>character ({@link CharacterFeature})</li>
		 *     <li>string ({@link StringFeature})</li>
		 *     <li>integer ({@link IntegerFeature})</li>
		 *     <li>float ({@link FloatFeature})</li>
		 * </ul>
		 *
		 * @return this instance.
		 *
		 * @since 0.2
		 */
		public Builder enableDefaults() {
			enableFeature("identifier");
			enableFeature("character");
			enableFeature("string");
			enableFeature("integer");
			enableFeature("float");
			return this;
		}

		/**
		 * Disables defaults features. The default features are:
		 * <ul>
		 *     <li>identifier ({@link IdentifierFeature})</li>
		 *     <li>character ({@link CharacterFeature})</li>
		 *     <li>string ({@link StringFeature})</li>
		 *     <li>integer ({@link IntegerFeature})</li>
		 *     <li>float ({@link FloatFeature})</li>
		 * </ul>
		 *
		 * @return the current instance.
		 *
		 * @since 0.2
		 */
		public Builder disableDefaults() {
			disableFeature("identifier");
			disableFeature("character");
			disableFeature("string");
			disableFeature("integer");
			disableFeature("float");
			return this;
		}

		/**
		 * Enables all previously added features.
		 *
		 * @return the current instance.
		 *
		 * @since 0.2
		 */
		public Builder enableAll() {
			for(var curr: featureList.values()) {
				curr.setEnabled(true);
			}
			return this;
		}

		/**
		 * Disables all previously added {@link Feature}s without removing them.
		 *
		 * @return the current instance.
		 *
		 * @since 0.2
		 */
		public Builder disableAll() {
			for(var curr: featureList.values()) {
				curr.setEnabled(false);
			}
			return this;
		}

		/**
		 * Convenience method to enable the specified {@link Feature}. Calling this method is equivalent to {@code setFeatureEnabled(featureName, true}.<br>
		 * If the specified {@code featureName} is "all" then all {@code Feature}s are affected.
		 *
		 * @param featureName name of the {@code Feature} to enable.
		 * @return the current instance.
		 *
		 * @since 0.2
		 *
		 * @see Builder#setFeatureEnabled(String, boolean)
		 */
		public Builder enableFeature(String featureName) {
			setFeatureEnabled(featureName, true);
			return this;
		}

		/**
		 * Convenience method to disable the specified {@link Feature}. Calling this method is equivalent to {@code setFeatureEnabled(featureName, false}.<br>
		 * If the specified {@code featureName} is "all" then all {øcode Feature}s are affected.
		 *
		 * @param featureName name of the {@code Feature} to enable.
		 * @return the current instance.
		 *
		 * @since 0.2
		 *
		 * @see Builder#setFeatureEnabled(String, boolean)
		 */
		public Builder disableFeature(String featureName) {
			setFeatureEnabled(featureName, false);
			return this;
		}

		/**
		 * Turns on or off the specified {@link Feature}. Turning it off does not remove it.<br>
		 * If the specified {@code featureName} is "all" then all features are affected, and if it is "defaults" or
		 * "default", then defaults {@code Feature}s are affected.<br>
		 * To use this method, the targeted {@code Feature} must have been added to this list.
		 *
		 * @param featureName name of the target feature.
		 * @param value whether to enable this feature.
		 * @return the current instance.
		 *
		 * @throws IllegalArgumentException if the feature represented by {@code featureName} has not been added to this
		 * list.
		 *
		 * @since 0.2
		 */
		public Builder setFeatureEnabled(String featureName, boolean value) {
			if(featureName.equalsIgnoreCase("all")) {
				if(value)
					enableAll();
				else
					disableAll();
				return this;
			}

			if(featureName.equalsIgnoreCase("defaults") || featureName.equalsIgnoreCase("default")) {
				if(value)
					enableDefaults();
				else
					disableDefaults();
				return this;
			}

			if(!featureList.containsKey(featureName)) {
				throw new IllegalArgumentException("Feature " + featureName + " not found");
			}
			featureList.get(featureName).setEnabled(value);
			return this;
		}

		/**
		 * Creates an instance of {@code FeatureList} from the current {@code Builder}.
		 *
		 * @return the instance.
		 *
		 * @since 0.2
		 */
		public FeatureList build() {
			List<Feature> enabled = new ArrayList<>();

			for(var curr: featureList.keySet()) {
				if(featureList.get(curr).isEnabled())
					enabled.add(featureList.get(curr));
			}

			return new FeatureList(enabled.toArray(new Feature[0]));
		}
	}
}
