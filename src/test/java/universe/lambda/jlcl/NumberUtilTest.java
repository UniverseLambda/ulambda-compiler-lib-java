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

import org.junit.jupiter.api.Test;
import universe.lambda.jlcl.utils.NumberUtil;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NumberUtilTest {
	@Test
	void valid_isInteger() {
		assertTrue(NumberUtil.isInteger("16"), "16");

		assertTrue(NumberUtil.isInteger("0X16"), "0X16");
		assertTrue(NumberUtil.isInteger("0x16"), "0x16");
		assertTrue(NumberUtil.isInteger("0x16F"), "0x16F");
		assertTrue(NumberUtil.isInteger("0x16F"), "0x16F");
		assertTrue(NumberUtil.isInteger("0X16f"), "0X16f");
		assertTrue(NumberUtil.isInteger("0x16f"), "0x16f");
		assertTrue(NumberUtil.isInteger("0x16D"), "0x16D");
		assertTrue(NumberUtil.isInteger("0x16D"), "0x16D");
		assertTrue(NumberUtil.isInteger("0X16d"), "0X16d");
		assertTrue(NumberUtil.isInteger("0x16d"), "0x16d");

		assertTrue(NumberUtil.isInteger("0b0110"), "0b0110");
		assertTrue(NumberUtil.isInteger("0B0110"), "0B0110");

		assertTrue(NumberUtil.isInteger("06750"), "06750");
	}
}
