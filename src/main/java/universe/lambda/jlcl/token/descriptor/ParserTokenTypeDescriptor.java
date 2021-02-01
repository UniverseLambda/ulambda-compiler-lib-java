package universe.lambda.jlcl.token.descriptor;

public class ParserTokenTypeDescriptor extends AbstractTokenTypeDescriptor {
	/**
	 * Creates a new {@code AbstractTokenTypeDescriptor} with the given name.
	 *
	 * @param name name of the descriptor.
	 * @since 0.1
	 */
	public ParserTokenTypeDescriptor(String name) {
		super(name);
	}

	@Override
	public boolean mayCorrespond(String value) {
		return false;
	}

	@Override
	public boolean correspond(String value) {
		return false;
	}
}
