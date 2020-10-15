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
import universe.lambda.jlcl.parser.ParsingRule;
import universe.lambda.jlcl.parser.ParsingRuleExecutor;
import universe.lambda.jlcl.token.Token;
import universe.lambda.jlcl.token.Tokenizer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * The {@code LanguageDefinitionInflater} is a class allowing users of this library to create a LanguageDefinition
 * from a file.
 *
 * @since 0.2
 *
 * @see LanguageDefinition
 */
public final class LanguageDefinitionInflater {
	/**
	 * {@link LanguageDefinition} used by the {@code LanguageDefinitionInflater} to inflate files.<br><br>
	 *
	 * <strong>RECYCLING INTENSIFIES</strong>
	 *
	 * @since 0.2
	 */
	private static LanguageDefinition inflaterDef;

	/**
	 * Inflates a {@link LanguageDefinition} from the file represented.
	 *
	 * @param path Path to the file to inflate.
	 * @return the inflated {@code LanguageDefinition} if the process has been
	 * successful, {@code null} otherwise.
	 *
	 * @since 0.2
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

				if(!featureName.getDescriptor().getName().equals(LanguageDefinition.IDENTIFIER)) {
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
			} else if (current.getValue().equalsIgnoreCase("rule")) {
				if (i + 3 >= tokens.length) {
					Logger.fatal(
							current.getSource()
									+ ":" + current.getLine() + ":" + current.getColumn()
									+ ": missing tokens after '"
									+ current.getValue()
									+ "'");
					return null;
				}
				StringBuilder classPathBuffer = new StringBuilder();

				Token firstClassToken = tokens[i + 1];

				for (;;) {
					Token next = tokens[++i];
					classPathBuffer.append(next.getValue());
					if (!tokens[++i].getDescriptor().getName().equals("PKG_SEP")) {
						break;
					}
					classPathBuffer.append('.');
				}

				Token ruleNameToken = tokens[++i];

				if (!ruleNameToken.getDescriptor().getName().equals(LanguageDefinition.IDENTIFIER)) {
					Logger.fatal(
							ruleNameToken.getSource()
									+ ":" + ruleNameToken.getLine() + ":" + ruleNameToken.getColumn()
									+ ": unexpected token '"
									+ ruleNameToken.getValue()
									+ "'");
					return null;
				}

				String ruleName = tokens[++i].getValue();

				var ruleRecipe = new ArrayList<String>();

				while (tokens[i].getDescriptor().getName().equals(LanguageDefinition.IDENTIFIER)) {
					ruleRecipe.add(tokens[i].getValue());
					++i;
				}

				Class<?> clazz;
				String classPath = classPathBuffer.toString();

				try {
					clazz = Class.forName(classPath);
				} catch (ClassNotFoundException e) {
					// e.printStackTrace();
					Logger.fatal(
							firstClassToken.getSource()
									+ ":" + firstClassToken.getLine() + ":" + firstClassToken.getColumn()
									+ ": could not find class '"
									+ classPath
									+ "'");
					return null;
				}

				ParsingRuleExecutor executor;

				try {
					var constructor = clazz.getDeclaredConstructor();
					var tmp = constructor.newInstance();
					if (!(tmp instanceof ParsingRuleExecutor)) {
						Logger.fatal(
								firstClassToken.getSource()
										+ ":" + firstClassToken.getLine() + ":" + firstClassToken.getColumn()
										+ ": class is not extending ParsingRuleExecutor : '"
										+ classPath
										+ "'");
						return null;
					}
					executor = (ParsingRuleExecutor)tmp;
				} catch (InstantiationException e) {
					Logger.fatal(
							firstClassToken.getSource()
									+ ":" + firstClassToken.getLine() + ":" + firstClassToken.getColumn()
									+ ": could not instantiate class '"
									+ classPath
									+ "': " + e.getMessage());
					return null;
				} catch (IllegalAccessException e) {
					Logger.fatal(
							firstClassToken.getSource()
									+ ":" + firstClassToken.getLine() + ":" + firstClassToken.getColumn()
									+ ": could not instantiate class '"
									+ classPath
									+ "': not visible");
					return null;
				} catch (InvocationTargetException e) {
					Logger.fatal(
							firstClassToken.getSource()
									+ ":" + firstClassToken.getLine() + ":" + firstClassToken.getColumn()
									+ ": an exception occured when instantiating class '"
									+ classPath
									+ "': " + e.getMessage());
					return null;
				}
				catch (NoSuchMethodException e) {
					Logger.fatal(
							firstClassToken.getSource()
									+ ":" + firstClassToken.getLine() + ":" + firstClassToken.getColumn()
									+ ": could not find nullary constructor of class '"
									+ classPath
									+ "'");
					return null;
				}

				ParsingRule rule = new ParsingRule(ruleName, ruleRecipe.toArray(new String[0]), executor);

				// TODO: builder.addParsingRule(ruleName, ruleRecipe.toArray(new String[0]), );
				// We reached the next line token but the for loop will increase i again, so we decrement it.
				--i;
			}
		}

		Logger.debug("done inflating!");

		return builder.setFeatureList(features.build()).build();
	}

	/**
	 * Initialize the inflater's instance of {@link LanguageDefinition}.
	 *
	 * @since 0.2
	 */
	private static void init() {
		if(inflaterDef != null) return;

		LanguageDefinition.Builder builder = new LanguageDefinition.Builder();

		builder.addTokenType("TOKEN_DEF", "token");
		builder.addTokenType("FEATURE_ENABLE", "enable");
		builder.addTokenType("FEATURE_DISABLE", "disable");
		builder.addTokenType("RULE_DEF", "rule");
		builder.addTokenType("PKG_SEP", "::");

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
