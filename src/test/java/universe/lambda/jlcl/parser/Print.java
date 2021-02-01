package universe.lambda.jlcl.parser;

import universe.lambda.jlcl.token.Token;

public class Print implements ParserRuleExecutor {
	@Override
	public Token execute(Object state, ParserRule rule, Token[] tokens) {
		System.out.println("Print with tokens: ");
		for (Token token : tokens) {
			System.out.print(" * \"");
			System.out.print(token.getValue());
			System.out.print("\" (");
			System.out.print(token.getDescriptor().getName());
			System.out.println(")");
		}
		return null;
	}
}
