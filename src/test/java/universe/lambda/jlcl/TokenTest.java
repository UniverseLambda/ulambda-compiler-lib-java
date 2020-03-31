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

import org.junit.jupiter.api.Test;
import universe.lambda.jlcl.token.descriptor.FloatTokenTypeDescriptor;
import universe.lambda.jlcl.token.descriptor.IntegerTokenTypeDescriptor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class containing tests for {@link universe.lambda.jlcl.token.descriptor.TokenTypeDescriptor}.
 *
 * @since 0.1
 */
class TokenTest {
	private final FloatTokenTypeDescriptor floatTTD = new FloatTokenTypeDescriptor();
	private final IntegerTokenTypeDescriptor intTTD = new IntegerTokenTypeDescriptor();

	private final String[] validFloats = {
			"16F", "16f",
			"16D", "16d",
			"0.16F", "0.16f",
			"0.16D", "0.16d",
			"0.16", "0.16",

	};

	private final String[] validIntegers = {
			"16", "12",                     // DECA
			"0x98F", "0X98F",               // HEXA
			"0b011011", "0B011011",         // BINA
			"0215733", "0654751"            // OCTA
	};

	/**
	 * Tests behavior of the {@link FloatTokenTypeDescriptor} when used with valid integers.
	 *
	 * @since 0.1
	 */
	@Test
	void float_integerCorrespond() {
		for(var value : validIntegers) {
			assertTrue(floatTTD.mayCorrespond(value), value);
			assertFalse(floatTTD.correspond(value), value);
		}
	}

	/**
	 * Tests behavior of the {@link FloatTokenTypeDescriptor} when used with valid floats.
	 *
	 * @since 0.1
	 */
	@Test
	void float_floatCorrespond() {
		for(var value: validFloats) {
			assertTrue(floatTTD.mayCorrespond(value), value);
			assertTrue(floatTTD.correspond(value), value);
		}
	}

	/**
	 * Tests behavior of the {@link IntegerTokenTypeDescriptor} when used with valid integers.
	 *
	 * @since 0.1
	 */
	@Test
	void integer_integerCorrespond() {
		for(var value: validIntegers) {
			assertTrue(intTTD.mayCorrespond(value), value);
			assertTrue(intTTD.correspond(value), value);
		}
	}

	/**
	 * Tests behavior of the {@link IntegerTokenTypeDescriptor} when used with valid floats.
	 *
	 * @since 0.2
	 */
	@Test
	void integer_floatCorrespond() {
		for(var value: validFloats) {
			assertFalse(intTTD.mayCorrespond(value), value);
			assertFalse(intTTD.correspond(value), value);
		}
	}
}
