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
import universe.lambda.jlcl.token.descriptor.FloatTokenTypeDescriptor;

/**
 * Feature adding the default float TokenTypeDescriptor ({@link FloatTokenTypeDescriptor}) to the {@link LanguageDefinition}.
 *
 * @since 0.2
 *
 * @see FloatTokenTypeDescriptor
 * @see Feature
 * @see LanguageDefinition
 */
public class FloatFeature extends AbstractFeature {

	/**
	 * Creates a new {@code FloatFeature}.
	 *
	 * @since 0.2
	 */
	public FloatFeature() {
		super("float");
	}

	@Override
	public void apply(LanguageDefinition.Builder builder) {
		builder.addTokenType(new FloatTokenTypeDescriptor());
	}
}
