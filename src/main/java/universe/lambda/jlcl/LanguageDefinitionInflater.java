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

import universe.lambda.jlcl.feature.FeatureList;
import universe.lambda.jlcl.token.Token;
import universe.lambda.jlcl.token.Tokenizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LanguageDefinitionInflater {
    /**
     * {@code LanguageDefinition} used by the {@code LanguageDefinitionInflater}
     * to inflate files.
    */
    private static LanguageDefinition inflaterDef;

    /**
     * @param path Path to the file to inflate
     * @return the inflated {@code LanguageDefinition} if the process has been
     * successful, {@code null} otherwise.
    */
    public static LanguageDefinition inflate(Path path) {
        Logger.debug("inflating LanguageDefinition...");
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

	    FeatureList.Builder features = new FeatureList.Builder();

        for(int i = 0; i < tokens.length; i++) {
            Token current = tokens[i];
            Logger.debug("processing token " + i + "/" + (tokens.length - 1) + " '" + current.getValue() + "'");
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
                Logger.debug("processing token " + i + "/" + (tokens.length - 1) + " '" + id.getValue() + "'");
                Token value = tokens[++i];
                Logger.debug("processing token " + i + "/" + (tokens.length - 1) + " '" + value.getValue() + "'");

                if(!id.getDescriptor().getName().equals(LanguageDefinition.IDENTIFIER)) {
                        Logger.fatal(
                                current.getSource()
                                        + ":" + id.getLine() + ":" + id.getColumn()
                                        + ": invalid token: '" + id.getValue() + "'"
                        );
                        return null;
                }

                if(
                        !value.getDescriptor().getName().equals("STRING") &&
                        !value.getDescriptor().getName().equals("CHAR")
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
            } else if(current.getValue().equalsIgnoreCase("enable") || current.getValue().equalsIgnoreCase("disable")) {
                if(i + 1 >= tokens.length) {
                    Logger.fatal(
                            current.getSource()
                                    + ":" + current.getLine() + ":" + current.getColumn()
                                    + ": missing tokens after '"
                                    + current.getValue()
                                    + "'");
                    return null;
                }
                Token featureName = tokens[++i];

                if(!featureName.getDescriptor().getName().equals("IDENTIFIER")) {
	                Logger.fatal(
			                featureName.getSource()
					                + ":" + featureName.getLine() + ":" + featureName.getColumn()
					                + ": unexpected token '"
					                + featureName.getValue()
					                + "'");
                	return null;
                }

                String strFeatureName = featureName.getValue();

	            features.setFeatureEnabled(strFeatureName, current.getValue().equalsIgnoreCase("enable"));
            }
        }

        Logger.debug("done inflating!");

        return builder.setFeatureList(features.build()).build();
    }

    /**
     * Initialize the inflater {@code LanguageDefinition}.
    */
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
