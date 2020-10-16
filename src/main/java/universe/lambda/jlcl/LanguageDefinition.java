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

package universe.lambda.jlcl;

import universe.lambda.jlcl.feature.FeatureList;
import universe.lambda.jlcl.parser.ParserRule;
import universe.lambda.jlcl.parser.ParserRuleExecutor;
import universe.lambda.jlcl.token.descriptor.DefinedTokenTypeDescriptor;
import universe.lambda.jlcl.token.descriptor.TokenTypeDescriptor;

import java.util.HashMap;

/**
 * Immutable object containing definition of a language: it is used by components of this library to do work.
 * To create one, use the subclass {@link Builder}.
 *
 * @since 0.1
 */
public class LanguageDefinition {
	/**
	 * Name of the integer {@link TokenTypeDescriptor}.
	 *
	 * @since 0.1
	 */
	public static final String INTEGER = "INTEGER";

	/**
	 * Name of the float {@link TokenTypeDescriptor}.
	 *
	 * @since 0.1
	 */
	public static final String FLOAT = "FLOAT";

	/**
	 * Name of the char {@link TokenTypeDescriptor}.
	 *
	 * @since 0.1
	 */
	public static final String CHAR = "CHAR";

	/**
	 * Name of the string {@link TokenTypeDescriptor}.
	 *
	 * @since 0.1
	 */
	public static final String STRING = "STRING";

	/**
	 * Name of the string {@link TokenTypeDescriptor}.
	 *
	 * @since 0.1
	 */
	public static final String IDENTIFIER = "IDENTIFIER";

	/**
	 * HashMap containing all the descriptors, indexed by name.
	 *
	 * @since 0.1
	 */
	private HashMap<String, TokenTypeDescriptor> descriptors;

	private HashMap<String, ParserRule> parserRules;

	/**
	 * We don't want people to instantiate it like sane people do.
	 *
	 * @since 0.1
	 */
	private LanguageDefinition() {}

	/**
	 * Checks if a {@code String} correspond to a TokenDescriptor registered in this LanguageDefinition.
	 *
	 * @param str {@code String} to check from.
	 * @return {@code true} if a TokenDescriptor was found, {@code false} otherwise.
	 *
	 * @since 0.1
	 *
	 * @see TokenTypeDescriptor#correspond(String)
	 */
	public boolean correspondToken(String str) {
		for(var curr : descriptors.values()) {
			if(curr.correspond(str)) return true;
		}
		return false;
	}

	/**
	 * Checks if a {@link String} MAY correspond to a {@link TokenTypeDescriptor} registered in this {@code LanguageDefinition}.
	 * This method differs from {@link #correspondToken(String)} in the fact that the value may not be an actual valid token, but
	 * adding more characters may be a valid token.
	 *
	 * @param str {@code String} to check from.
	 * @return {@code true} if a TokenDescriptor may correspond to {@code str}, {@code false} otherwise.
	 *
	 * @since 0.1
	 *
	 * @see TokenTypeDescriptor#mayCorrespond(String)
	 */
	public boolean mayCorrespondToken(String str) {
		for(var curr : descriptors.values()) {
			if(curr.mayCorrespond(str)) return true;
		}
		return false;
	}

	/**
	 * Gets the first {@link TokenTypeDescriptor} which value corresponds to {@code value}, if one.
	 *
	 * @param value value of the {@code TokenTypeDescriptor}.
	 * @return the {@code TokenTypeDescriptor} if found, null otherwise.
	 *
	 * @since 0.1
	 */
	public TokenTypeDescriptor getTokenTypeDescriptorByValue(String value) {
		TokenTypeDescriptor retained = null;
		for(var curr : descriptors.values()) {
			if(curr.correspond(value)) {
				if(curr.getName().equalsIgnoreCase(IDENTIFIER)) {
					retained = curr;
					continue;
				}
				return curr;
			}
		}
		return retained;
	}

	/**
	 * Gets the first {@link TokenTypeDescriptor} which name corresponds to {@code value}, if one.
	 *
	 * @param name name of the {@code TokenTypeDescriptor}.
	 * @return the {@code TokenTypeDescriptor} if found, null otherwise.
	 *
	 * @since 0.1
	 */
	public TokenTypeDescriptor getTokenTypeDescriptorByName(String name) {
		return descriptors.get(name);
	}

	public ParserRule getParserRuleByName(String name) {
		return parserRules.get(name);
	}

	public ParserRule[] getParserRules() {
		return parserRules.values().toArray(new ParserRule[0]);
	}

	/**
	 * Class used for building a LanguageDefinition.
	 *
	 * @since 0.1
	 */
	public static class Builder {
		/**
		 * Feature list which will be used by the {@link #build()} method.
		 *
		 * @since 0.2
		 */
		private FeatureList featureList;

		/**
		 * {@link HashMap} used to store all descriptors.
		 *
		 * @since 0.1
		 */
		private HashMap<String, TokenTypeDescriptor> desc = new HashMap<>();

		private HashMap<String, ParserRule> rules = new HashMap<>();

		/**
		 * Creates a new {@code Builder}. Use the default {@link FeatureList}.
		 *
		 * @since 0.1
		 */
		public Builder() {
			featureList = new FeatureList.Builder().enableDefaults().build();
		}

		/**
		 * Adds a {@code DefinedTokenTypeDescriptor} with specified parameters.
		 *
		 * @param name name of the {@link DefinedTokenTypeDescriptor}.
		 * @param value value of the {@code DefinedTokenTypeDescriptor}.
		 * @return the current instance.
		 *
		 * @since 0.1
		 */
		public Builder addTokenType(String name, String value) {
			return addTokenType(new DefinedTokenTypeDescriptor(name, value));
		}

		/**
		 * Adds a {@code TokenTypeDescriptor} to the LanguageDefinition.
		 *
		 * @param descriptor to add.
		 * @return the current instance.
		 *
		 * @since 0.1
		 */
		public Builder addTokenType(TokenTypeDescriptor descriptor) {
			desc.put(descriptor.getName(), descriptor);
			return this;
		}

		public Builder addParserRule(String name, String[] value, ParserRuleExecutor executor) {
			addParserRule(new ParserRule(name, value, executor));
			return this;
		}

		public Builder addParserRule(ParserRule rule) {
			rules.put(rule.getName(), rule);
			return this;
		}

		/**
		 * Sets the {@link FeatureList} to use when building the {@link LanguageDefinition}.
		 *
		 * @param featureList The new {@code FeatureList} to use.
		 * @return the current instance.
		 *
		 * @since 0.2
		 */
		public Builder setFeatureList(FeatureList featureList) {
			this.featureList = featureList;
			return this;
		}

		/**
		 * Builds a {@link LanguageDefinition} from this {@code Builder}.
		 *
		 * @return the built LanguageDefinition.
		 *
		 * @since 0.1
		 */
		public LanguageDefinition build() {
			if(featureList != null) {
				featureList.apply(this);
			}

			var def = new LanguageDefinition();
			// we don't want modifications of this Builder HashMap to modify the LanguageDefinition HashMap.
			def.descriptors = new HashMap<>(desc);
			def.parserRules = new HashMap<>(rules);
			return def;
		}
	}
}
