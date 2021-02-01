package universe.lambda.jlcl.token;

public interface TokenSource {
	Token readToken();
	Token[] readAllTokens();
}
