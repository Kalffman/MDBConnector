package br.inf.cs.interlis.MDbConnector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.inf.cs.interlis.MDbConnector.util.DateTimeDesSer;
import br.inf.cs.interlis.MDbConnector.util.MDButil;

/**
 * Representa o Document que será inserido no mongoDB.
 * <p>
 * Atributos
 * <ul>
 * 	<li>private ObjectId _id</li>
 * 	<li>private <T> value</li>
 * 	<li>private Date timestamp</li>
 * 	<li>private boolean visible</li>
 * </ul>
 * <p>
 * É feita a serialização e deserialização de objetos Java para Json ou viceversa com a biblioteca Gson da google.
 * 
 * @
 * @param <T>
 * @author jeff_kalffman
 */
public class MDBdocument<T> {
	
	private Class<T> clazz;
	private Gson g;
	private ObjectId _id;
	private T value;
	private Date timestamp = Calendar.getInstance().getTime();
	private boolean visible = true;
	
	/**
	 * Construtor para inicializar MDBdocument provindo de um POJO
	 * 
	 * @param <T> t
	 */
	@SuppressWarnings("unchecked")
	public MDBdocument(T t){
		this.clazz = (Class<T>) t.getClass();
		this._id = new ObjectId();
		this.timestamp = Calendar.getInstance().getTime();
		this.value = t;
		GsonBuilder gBuilder = new GsonBuilder();
		gBuilder.registerTypeAdapter(Date.class, MDButil.dateDeserializer());
		gBuilder.registerTypeAdapter(Date.class, MDButil.dateSerializer());
		this.g = gBuilder.create();
	}
	
	/**
	 * Construtor para inicializar o MDBdocument. Intenção de  "wrappear" um Document
	 * 
	 * @param Document doc
	 * @param Class<T> clazz
	 * @param boolean isList
	 */
	@SuppressWarnings("unchecked")
	public MDBdocument(Document doc, Class<T> clazz, boolean isList) {
		this.clazz = clazz;
		this._id = doc.getObjectId("_id");
		this.timestamp = doc.getDate("timestamp");
		this.visible = doc.getBoolean("visible");
		GsonBuilder gBuilder = new GsonBuilder();
		gBuilder.registerTypeAdapter(Date.class, MDButil.dateDeserializer());
		gBuilder.registerTypeAdapter(Date.class, MDButil.dateSerializer());
		this.g = gBuilder.create();

		if(isList) {
			List<Document> value = (List<Document>) doc.get("value");
			
			List<T> values = new ArrayList<T>();
			for(Document cur : value) {
				values.add(g.fromJson(cur.toJson(), this.clazz));
			}
			this.value = (T) values;
		} else {
			Document value = (Document) doc.get("value");
			this.value = g.fromJson(value.toJson(), this.clazz);
		}
	}
	
	public Document toDocument() {
		HashMap<String, Object> map = new HashMap<>();
		map.put("_id", this._id);
		map.put("value", this.value);
		map.put("timestamp", this.timestamp);
		map.put("visible", this.visible);
		return new Document(map);
	}
	
	@SuppressWarnings("unused")
	public Map<String, String> getFieldTypeAndValue(){
		Map<String, String> fieldTypeAndValue = new HashMap<>();
		Field[] fields = this.clazz.getDeclaredFields();
		for(Field f : fields) {
			Class<?> type = f.getType();
			fieldTypeAndValue.put(f.getName(), f.toString());
		}
		return fieldTypeAndValue;
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public T getValue() {
		return (T) value;
	}

	public void update(T value) {
		this.value = value;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
}
