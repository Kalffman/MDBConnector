package br.inf.cs.interlis.MDbConnector.exception;

public class AliasNotFoundException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	public AliasNotFoundException(String exception) {
		super(exception +" não existe; Use o método createClient(\"alias\", \"host\", \"port\") para utilizar o cliente específico.");
	}

}
