package br.inf.cs.interlis.MDbConnector;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import br.inf.cs.interlis.MDbConnector.exception.AliasAlreadyExistsException;
import br.inf.cs.interlis.MDbConnector.exception.AliasNotFoundException;

/**
 * Classe responsável para concentrar todas as conexões MongoDB.
 * Permite chamadas encadeadas de métodos sobre métodos.<p>
 * Aplicasse o padrão Singleton
 * <p><bold>Instâncias únicas para toda a aplicação<bold></p>
 * 
 * @author jeff_kalffman
 */
public class MDBclient {

	private static MDBclient mClient = new MDBclient();

	private static Map<String, MongoClient> clients = new HashMap<>();

	private MDBclient() {
	}

	/**
	 * Retorna os aliases das conexões criadas.
	 * 
	 * @return Set&ltString&gt aliases
	 */
	public static Set<String> getAliases() {
		return clients.keySet();
	}

	/**
	 * Cria um cliente estático para conexão com o MongoDB com apelido e endereço e porta padrão do mongoDB.
	 * 
	 * @param Strig alias
	 * @return MDBclient
	 * @throws AliasAlreadyExistsException
	 */
	public static MDBclient createClient(String alias) throws AliasAlreadyExistsException {
		if (!clients.containsKey(alias)) {
			clients.put(alias, new MongoClient());
		} else {
			throw new AliasAlreadyExistsException(alias);
		}
		try {
			use(alias);
			return mClient;
		} catch (AliasNotFoundException e) {
			return mClient;
		}
	}

	/**
	 * Cria um cliente estático para conexão com o MongoDB com endereço e porta direcionados.
	 * 
	 * @param String alias
	 * @param String host
	 * @param Integer port
	 * @return MDBclient
	 * @throws AliasAlreadyExistsException
	 */
	public static MDBclient createClient(String alias, String host, Integer port) throws AliasAlreadyExistsException {
		if (!clients.containsKey(alias)) {
			clients.put(alias, new MongoClient(host, port));
		} else {
			throw new AliasAlreadyExistsException(alias);
		}
		try {
			use(alias);
			return mClient;
		} catch (AliasNotFoundException e) {
			return mClient;
		}
	}

	/**
	 * Cria um cliente estático para conexão com o MongoDB com endereço, porta, usuário, senha e banco de dados.
	 * 
	 * @param String alias
	 * @param String host
	 * @param Integer port
	 * @param String user
	 * @param String password
	 * @param String database
	 * @return MDBclient
	 * @throws AliasAlreadyExistsException
	 */
	public static MDBclient createClient(String alias, String host, Integer port, String user, String password,
			String database) throws AliasAlreadyExistsException {

		if (!clients.containsKey(alias)) {
			clients.put(alias, new MongoClient(
					new MongoClientURI("mongodb://" + user + ":" + password + "@" + host + "/authSource=" + database)));
		} else {
			throw new AliasAlreadyExistsException(alias);
		}
		try {
			use(alias);
			return mClient;
		} catch (AliasNotFoundException e) {
			return mClient;
		}
	}

	/**
	 * Retorna o MongoClient apelidado.
	 * 
	 * @param String alias
	 * @return MongoClient
	 * @throws AliasNotFoundException
	 */
	public static MongoClient use(String alias) throws AliasNotFoundException {
		MDBclient.clients.get(alias);
		if (clients.containsKey(alias)) {
			return clients.get(alias);
		} else {
			throw new AliasNotFoundException(alias);
		}
	}


	/**
	 * Encerrar a conexão de um alias criado
	 * 
	 * @param String alias
	 * @throws AliasNotFoundException
	 */
	public static void close(String alias) throws AliasNotFoundException {
		if(clients.containsKey(alias)){
			clients.get(alias).close();
			clients.remove((alias));
		} else {
			throw new AliasNotFoundException("alias");
		}
	}
	
	/**
	 * Encerra as conexões de todos os clientes criados.
	 * 
	 */
	public static void closeAll() {
		for (Entry<String, MongoClient> entry : clients.entrySet()) {
			entry.getValue().close();
		}
	}

}
