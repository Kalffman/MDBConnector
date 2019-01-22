package br.inf.cs.interlis.MDbConnector.exception;

public class AliasAlreadyExistsException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	public AliasAlreadyExistsException(String exception) {
		super(exception+ " já existe; Use o método workWith(\"alias\") para trabalhar com o alias existente!");
	}
}
