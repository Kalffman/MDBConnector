package br.inf.cs.interlis.MDbConnector.util;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public class MDButil {

	public static JsonDeserializer<Date> dateDeserializer(){
		return new DateTimeDesSer();
	}
	
	public static JsonSerializer<Date> dateSerializer(){
		return new DateTimeDesSer();
	}
	
	public static Map<String, String> fieldsAndTypes(Class<?> t){
		Map<String, String> fieldsAndTypes = new HashMap<String, String>();
		Field[] fields = t.getDeclaredFields();
		for(Field f : fields) {
			Class<?> type = f.getType();
			fieldsAndTypes.put(f.getName(), type.getName());
		}
		return fieldsAndTypes;
	}
}
