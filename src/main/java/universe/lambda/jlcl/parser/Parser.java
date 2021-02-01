package universe.lambda.jlcl.parser;

import universe.lambda.jlcl.LanguageDefinition;
import universe.lambda.jlcl.Logger;
import universe.lambda.jlcl.token.ArrayTokenSource;
import universe.lambda.jlcl.token.Token;
import universe.lambda.jlcl.token.TokenSource;
import universe.lambda.jlcl.token.descriptor.DefinedTokenTypeDescriptor;
import universe.lambda.jlcl.token.descriptor.ParserTokenTypeDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
	private final LanguageDefinition def;
	private final Object state;

	public Parser(LanguageDefinition definition, Object state) {
		this.def = definition;
		this.state = state;
	}

	public boolean parse(Token[] tokens) {
		return parse(new ArrayTokenSource(tokens));
	}

	public boolean parse(TokenSource source) {
		ParserRule[] ruleSet = def.getParserRules();
		Token lookahead = source.readToken();
		Stack<Token> parseStack = new Stack<>();

		if (lookahead == null) {
			System.out.println("Empty");
			return false;
		}

		for (;;) {
			boolean hasReduced = false;

			// for (int i = 0; i < parseStack.size(); ++i) {
			for (int i = parseStack.size() - 1; i >= 0; --i) {
				Token[] tokens = new Token[i + 1];
				for (int j = tokens.length - 1; j >= 0; --j) {
					tokens[j] = parseStack.pop();
				}

				for (ParserRule rule : ruleSet) {
					String[] values = rule.getValue();
					if (values.length != tokens.length) {
						continue;
					}

					boolean found = true;

					for (int j = 0; j < values.length; ++j) {
						if (!tokens[j].getDescriptor().getName().equals(values[j])) {
							found = false;
							break;
						}
					}

					if (found) {
						var result = rule.getExecutor().execute(state, rule, tokens);

						if (result == null) {
							// FIXME: remove 
							result = new Token(new ParserTokenTypeDescriptor(rule.getName()), "mdr", "nosrc", 1, 1);
							// Logger.error("Parsing error. Stopping.");
							// return false;
						}

						parseStack.push(result);
						hasReduced = true;
						break;
					}
				}

				if (hasReduced) {
					break;
				}

				for (Token token : tokens) {
					parseStack.push(token);
				}
			}

			if (!hasReduced) {
				if (lookahead == null) {
					break;
				}

				parseStack.push(lookahead);
				lookahead = source.readToken();
			}
		}

		System.out.println("Reached the end (parseStack size = " + parseStack.size() + ")");
		System.out.println("Content: ");
		for (var curr: parseStack) {
			System.out.println(" * " + curr.getValue() + " (" + curr.getDescriptor().getName() + ")");
		}
		return parseStack.size() == 1;
	}
}
