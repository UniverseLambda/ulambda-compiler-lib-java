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

package universe.lambda.jlcl;

import universe.lambda.jlcl.token.descriptor.*;

import java.util.HashMap;

/**
 * Immutable object containing definition of a language: it is used by components of this library to do work.
 * To create one, use the subclass Builder.
 */
public class LanguageDefinition {
	/**
	 * Name of the integer TokenDescriptor.
	 */
	public static final String INTEGER = "INTEGER";

	/**
	 * Name of the float TokenDescriptor
	 */
	public static final String FLOAT = "FLOAT";

	/**
	 * Name of the char TokenDescriptor
	 */
	public static final String CHAR = "CHAR";

	/**
	 * Name of the string TokenDescriptor
	 */
	public static final String STRING = "STRING";

	/**
	 * Name of the string TokenDescriptor
	 */
	public static final String IDENTIFIER = "IDENTIFIER";

	/**
	 * HashMap containing all of the descriptors, indexed by their names.
	 */
	private HashMap<String, TokenTypeDescriptor> descriptors;

	/**
	 * We don't want people to initialize it like sane people do.
	 */
	private LanguageDefinition() {}

	/**
	 * Check if a {@code String} correspond to a TokenDescriptor registered in this LanguageDefinition.
	 * @param str {@code String} to check from.
	 * @return {@code true} if a TokenDescriptor was found, {@code false} otherwise.
	 */
	public boolean correspondToken(String str) {
		for(var curr : descriptors.values()) {
			if(curr.correspond(str)) return true;
		}
		return false;
	}

	/**
	 * Check if a {@code String} MAY correspond to a TokenDescriptor registered in this LanguageDefinition.
	 * This method differs from {@code corresponToken} in the fact that the value may not be an actual valid token, but
	 * adding more characters may be a valid token.
	 * @param str {@code String} to check from.
	 * @return {@code true} if a TokenDescriptor may correspond to {@code str}, {@code false} otherwise.
	 */
	public boolean mayCorrespondToken(String str) {
		for(var curr : descriptors.values()) {
			if(curr.mayCorrespond(str)) return true;
		}
		return false;
	}

	/**
	 * Get the first TokenTypeDescriptor which value corresponds to {@code value}, if one.
	 * @param value of the TokenTypeDescriptor.
	 * @return the TokenTypeDescriptor if found, null otherwise.
	 */
	public TokenTypeDescriptor getTokenTypeDescriptorByValue(String value) {
		for(var curr : descriptors.values()) {
			if(curr.correspond(value)) return curr;
		}
		return null;
	}

	/**
	 * Get the first TokenTypeDescriptor which name corresponds to {@code value}, if one.
	 * @param name of the TokenTypeDescriptor.
	 * @return the TokenTypeDescriptor if found, null otherwise.
	 */
	public TokenTypeDescriptor getTokenTypeDescriptorByName(String name) {
		return descriptors.get(name);
	}

	/**
	 * Class for creating a LanguageDefinition.
	 */
	public static class Builder {
		HashMap<String, TokenTypeDescriptor> desc = new HashMap<>();

		/**
		 * Add a {@code DefinedTokenTypeDescriptor} with specified parameters.
		 * @param name of the {@code DefinedTokenTypeDescriptor}.
		 * @param value of the {@code DefinedTokenTypeDescriptor}.
		 * @return this builder, for inline calls.
		 */
		public Builder addTokenType(String name, String value) {
			return addTokenType(new DefinedTokenTypeDescriptor(name, value));
		}

		/**
		 * Add a {@code TokenTypeDescriptor} to the LanguageDefinition.
		 * @param descriptor to add.
		 * @return this builder, for inline calls.
		 */
		public Builder addTokenType(TokenTypeDescriptor descriptor) {
			desc.put(descriptor.getName(), descriptor);
			return this;
		}

		/**
		 * Build the LanguageDefinition.
		 * @return the built LanguageDefinition.
		 */
		public LanguageDefinition build() {
			if(!desc.containsKey(IDENTIFIER))	desc.put(IDENTIFIER, new IdentifierTokenTypeDescriptor(IDENTIFIER));
			if(!desc.containsKey(INTEGER))		desc.put(INTEGER, new IntegerTokenTypeDescriptor(INTEGER));
			if(!desc.containsKey(FLOAT))		desc.put(FLOAT, new FloatTokenTypeDescriptor(FLOAT));
			if(!desc.containsKey(CHAR))			desc.put(CHAR, new CharTokenTypeDescriptor(CHAR));
			if(!desc.containsKey(STRING))		desc.put(STRING, new CharTokenTypeDescriptor(STRING));

			var def = new LanguageDefinition();
			// we don't want modifications of this Builder HashMap to modify the LanguageDefinition HashMap.
			def.descriptors = new HashMap<>(desc);
			return def;
		}
	}
}
