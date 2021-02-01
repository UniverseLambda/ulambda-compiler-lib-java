package universe.lambda.jlcl.token;

public class ArrayTokenSource implements TokenSource {
	private final Token[] tokens;
	private int index = 0;

	public ArrayTokenSource(Token[] tokens) {
		this.tokens = tokens;
	}

	@Override
	public Token readToken() {
		if (index >= tokens.length)
			return null;
		return tokens[index++];
	}

	@Override
	public Token[] readAllTokens() {
		return tokens;
	}
}
