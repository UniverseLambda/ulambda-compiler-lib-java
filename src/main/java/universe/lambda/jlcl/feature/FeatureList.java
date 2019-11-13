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

package universe.lambda.jlcl.feature;

import universe.lambda.jlcl.LanguageDefinition;

import java.util.HashMap;

public class FeatureList {
	private Feature[] features;

	private FeatureList(Feature[] features) {
		this.features = features;
	}

	public void apply(LanguageDefinition.Builder builder) {
		for(var curr: features) {
			curr.apply(builder);
		}
	}

	public static class Builder {
		private HashMap<String, Feature> featureList = new HashMap<>();

		public Builder() {
			indexFeatures();
			enableDefaults();
		}

		private void indexFeatures() {
			addFeature(new IdentifierFeature());
			addFeature(new CharacterFeature());
			addFeature(new StringFeature());
			addFeature(new IntegerFeature());
			addFeature(new FloatFeature());
		}

		private void addFeature(Feature feature) {
			featureList.put(feature.getName(), feature);
		}

		public Builder enableDefaults() {
			enableFeature("identifier");
			enableFeature("character");
			enableFeature("string");
			enableFeature("integer");
			enableFeature("float");
			return this;
		}

		public Builder enableAll() {
			for(var curr: featureList.values()) {
				curr.setEnabled(true);
			}
			return this;
		}

		public Builder disableAll() {
			for(var curr: featureList.values()) {
				curr.setEnabled(false);
			}
			return this;
		}

		public Builder enableFeature(String featureName) {
			setFeatureEnabled(featureName, true);
			return this;
		}

		public Builder disableFeature(String featureName) {
			setFeatureEnabled(featureName, false);
			return this;
		}

		public Builder setFeatureEnabled(String featureName, boolean value) {
			if(!featureList.containsKey(featureName)) {
				throw new IllegalArgumentException("Feature " + featureName + " not found");
			}
			featureList.get(featureName).setEnabled(value);
			return this;
		}

		public FeatureList build() {
			for(var curr: featureList.keySet()) {
				if(!featureList.get(curr).isEnabled()) featureList.remove(curr);
			}
			return new FeatureList(featureList.values().toArray(new Feature[0]));
		}
	}
}
