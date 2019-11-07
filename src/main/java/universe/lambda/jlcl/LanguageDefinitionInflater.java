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

import universe.lambda.jlcl.token.Token;
import universe.lambda.jlcl.token.Tokenizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LanguageDefinitionInflater {
    private static LanguageDefinition inflaterDef;

    public static LanguageDefinition inflate(Path path) {
        init();

        if(!Files.isRegularFile(path)) {
            Logger.error("file to inflate does not exists!");
            return null;
        }

        if(!Files.isReadable(path)) {
            Logger.fatal("file to inflate is not readable!");
            return null;
        }

        Token[] tokens;

        try (var reader = Files.newBufferedReader(path)) {
            Tokenizer tokenizer = new Tokenizer(inflaterDef, reader, path.toString());
            tokens = tokenizer.readAllTokens();
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }

        LanguageDefinition.Builder builder = new LanguageDefinition.Builder();

        for(int i = 0; i < tokens.length; i++) {
            Token current = tokens[i];
            if(current.getValue().equals("token")) {
                if(i + 2 >= tokens.length) {
                    Logger.fatal(
                            current.getSource()
                                    + ":" + current.getLine() + ":" + current.getColumn()
                                    + ": missing tokens after '"
                                    + current.getValue()
                                    + "'");
                    return null;
                }

                Token id = tokens[++i];
                Token value = tokens[++i];

                if(!id.getDescriptor().getName().equals(LanguageDefinition.IDENTIFIER)) {
                        Logger.fatal(
                                current.getSource()
                                        + ":" + id.getLine() + ":" + id.getColumn()
                                        + ": invalid token: '" + id.getValue() + "'"
                        );
                        return null;
                }

                if(
                        !value.getDescriptor().getName().equals(LanguageDefinition.STRING) &&
                        !value.getDescriptor().getName().equals(LanguageDefinition.CHAR)
                ) {
                    Logger.fatal(
                            current.getSource()
                                    + ":" + value.getLine() + ":" + value.getColumn()
                                    + ": invalid token: '" + value.getValue() + "'"
                    );
                    return null;
                }


                String sValue = value.getValue();
                sValue = sValue.substring(1, sValue.length() - 1);

                builder.addTokenType(id.getValue(), sValue);
            }
        }

        return builder.build();
    }

    private static void init() {
        if(inflaterDef != null) return;

        LanguageDefinition.Builder builder = new LanguageDefinition.Builder();

        builder.addTokenType("TOKEN_DEF", "token");

        // Those are for future features
        // builder.addTokenType("COMMENT_DEF", "comment");
        // builder.addTokenType("COMMENT_LINE", "line");
        // builder.addTokenType("COMMENT_BLOCK", "block");
        // builder.addTokenType("FEATURE_ENABLE", "enable");
        // builder.addTokenType("FEATURE_DISABLE", "disable");
        // builder.addTokenType("FEATURE_INTEGER", "integer");
        // builder.addTokenType("FEATURE_FLOAT", "float");
        // builder.addTokenType("FEATURE_CHAR", "char");
        // builder.addTokenType("FEATURE_STRING", "string");
        // builder.addTokenType("FEATURE_LINE_ENDING", "line_ending");
        // builder.addTokenType("FEATURE_WHITESPACE", "whitespace");

        inflaterDef = builder.build();
    }
}
