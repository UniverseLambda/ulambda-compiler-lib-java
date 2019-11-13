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

package universe.lambda.jlcl.token.descriptor;

public class StringTokenTypeDescriptor extends AbstractTokenTypeDescriptor {
	public StringTokenTypeDescriptor() {
		super("STRING");
	}

	@Override
	public boolean correspond(String value) {
		if(!value.startsWith("\"") || !value.endsWith("\"")) return false;
		return analyse(value, false);
	}

	@Override
	public boolean mayCorrespond(String value) {
		if(!value.startsWith("\"")) return false;
		return analyse(value, true);
	}

	private static boolean analyse(String value, boolean defaultReturn) {
		boolean skipPrevious = false;

		for(int i = 1; i < value.length(); i++) {
			if(skipPrevious) {
				skipPrevious = false;
				continue;
			}

			int cp = value.codePointAt(i);

			if(cp == '\\') {
				skipPrevious = true;
				continue;
			}

			if(cp == '\"') {
				return i + 1 == value.length();
			}
		}

		return defaultReturn;
	}
}
