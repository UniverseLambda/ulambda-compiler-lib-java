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
import universe.lambda.jlcl.parser.Parser;
import universe.lambda.jlcl.token.Token;
import universe.lambda.jlcl.token.Tokenizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing tests with more concrete situations.
 *
 * @since 0.2
 */
public class ConcreteTest {
	/**
	 * Tests the inflater.
	 *
	 * @since 0.2
	 */
	@Test
	void inflaterTest() {
		Logger.name = "Inflater";
		Logger.minimumLogLevel = Logger.LogLevel.DEBUG;

		Path conf = Paths.get("samples/inflater-file-sample.ulcl");
		assertTrue(Files.isRegularFile(conf));

		Path target = Paths.get("samples/test-sample");
		assertTrue(Files.isRegularFile(target));

		var def = LanguageDefinitionInflater.inflate(conf);
		assertNotNull(def);

		Token[] tokens = null;

		try (var reader = Files.newBufferedReader(target)){
			Tokenizer tokenizer = new Tokenizer(def, reader, conf.toString());
			tokens = tokenizer.readAllTokens();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		assertNotNull(tokens);

		var count = 0;

		for(var token: tokens) {
			System.out.println("Token " + count++ + ": " + token.getValue());
		}

		System.out.println("Parsing...");
		var parser = new Parser(def, null);
		assertTrue(parser.parse(tokens));
	}
}
