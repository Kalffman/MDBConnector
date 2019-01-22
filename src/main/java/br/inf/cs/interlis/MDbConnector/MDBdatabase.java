package br.inf.cs.interlis.MDbConnector;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Classe responsável para manipular as instâncias do MongoCliente.
 * Permite chamadas encadeadas de métodos sobre métodos para <b>uma instância</b>.
 * @author jeff_kalffman
 */
public class MDBdatabase {

	private MongoClient mClient;

	private List<String> databases = new ArrayList<>();

	/**
	 * Construtor padrão.
	 * 
	 * @param MongoClient mClient
	 */
	@SuppressWarnings("deprecation")
	public MDBdatabase(MongoClient mClient) {
		this.mClient = mClient;

		for (String db : mClient.getDatabaseNames()) databases.add(db);
		
		getDatabases().forEach(System.out::println);
	}

	/**
	 * MongoClient responsável pelos bancos de dados disponíveis.
	 * 
	 * @return MongoClient
	 */
	public MongoClient getClient() {
		return mClient;
	}

	/**
	 * Lista de banco de dados do cliente conectado
	 * 
	 * @return List&ltString&gt databases
	 */
	public List<String> getDatabases() {
		return databases;
	}

	/**
	 * Retorna o banco de dados disponível.
	 * 
	 * @param database
	 * @return MongoDatabase
	 * @throws IllegalArgumentException Se o nome do banco de dados for inválido.
	 */
	public MongoDatabase use(String database) {
		MongoDatabase dataBase = mClient.getDatabase(database);
		return dataBase;
	}

}
