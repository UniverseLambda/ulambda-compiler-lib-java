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

package universe.lambda.jlcl.parser;

import universe.lambda.jlcl.token.Token;

public interface ParsingRuleExecutor {
    /**
     * Execute a given parsing rule
     *
     * @param state state object (set by the lib user)
     * @param rule rule linked to this execution
     * @param tokens tokens triggering the rule
     * @return the resulting token, {@code null} if the execution failed
     */
    Token execute(Object state, ParsingRule rule, Token[] tokens);
}
