package br.inf.cs.interlis.MDbConnector;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * Classe responsável por manipular as MongoCollection's do MongoDatabase passado como parâmetro no construtor.
 * Permite chamadas encadeadas de métodos sobre métodos para uma instância.
 * 
 * @author jeff_kalffman
 */
public class MDBcollection {

	private MongoDatabase db;
	
	private String actualCollection;
	
	/**
	 * Construtor padrão da classe.
	 * 
	 * @param MongoDatabase dataBase
	 */
	public MDBcollection(MongoDatabase dataBase) {
		this.db = dataBase;
		getCollections().forEach(System.out::println);
	}

	/**
	 * Retorna o banco de dados responsável pelas collections disponíveis
	 * 
	 * @return MongoDatabase
	 */
	public MongoDatabase getDatabase() {
		return db;
	}

	/**
	 * Retorna uma lista de collections existentes no banco de dados.
	 * 
	 * @return List&ltString&gt collections
	 */
	public List<String> getCollections() {
		List<String> list = new ArrayList<>();
		for(String s : db.listCollectionNames()) {
			list.add(s);
		}
		return list;
	}

	/**
	 * Declara qual collection será usado na instância
	 * 
	 * @param collection
	 * @return this.
	 */
	public MDBcollection use(String collection){
		this.actualCollection = collection;
		return this;
	}
	
	/**
	 * Retorna a MongoCollection declarada
	 * 
	 * @return MongoCollection
	 */
	public MongoCollection<Document> getCollection(){
		if(this.actualCollection != null && this.actualCollection.length() > 0) {
			CodecRegistry codec = fromRegistries(MongoClient.getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().automatic(true).build()));
			return db.getCollection(this.actualCollection).withCodecRegistry(codec);
		} else {
			throw new IllegalArgumentException("Não foi definido a collection na instância");
		}
	}
	
	/**
	 * Persiste um MDBdocument na collection declarada
	 * 
	 * @param MDBdocument doc
	 */
	public void save(MDBdocument<?> doc) {
		MongoCollection<Document> coll = getCollection();
		coll.insertOne(doc.toDocument());
	}
	
	/**
	 * Persiste Uma lista de MDBdocument ao mesmo tempo na collection dclarada
	 * 
	 * @param MDBdocument docs
	 */
	public void save(List<MDBdocument<?>> docs) {
		MongoCollection<Document> coll = getCollection();
		coll.insertMany(docs.stream().map(d -> d.toDocument()).collect(Collectors.toList()));
	}
	
	/**
	 * Retorna uma Lista de Document baseado no argumento chamado
	 * 
	 * @param ObjectId id
	 * @return FindIterable&ltDocument&gt Documents
	 */
	public FindIterable<Document> findById(ObjectId id) {
		MongoCollection<Document> coll = getCollection();
		return coll.find(new Document("_id", id));
	}
	
	/**
	 * Atualiza e retorna o Document atualizado 
	 *
	 * @param doc
	 * @return Document "atualizado"
	 */
	public Document update(MDBdocument<?> doc) {
		MongoCollection<Document> coll = getCollection();
		return coll.findOneAndUpdate(Filters.all("_id", doc.get_id()), new Document("$set", doc.toDocument()));
	}

	/**
	 * "Deleta" e retorna o Document "deletado"
	 * <p>
	 * Atribui a flag visible do MDBdocument como false
	 * 
	 * @param MDBdocument doc
	 * @return Document "atualizado"
	 */
	public Document delete(MDBdocument<?> doc) {
		doc.setVisible(false);
		MongoCollection<Document> coll = getCollection();
		return coll.findOneAndUpdate(Filters.all("_id", doc.get_id()), new Document("$set", doc.toDocument()));
	}
}




