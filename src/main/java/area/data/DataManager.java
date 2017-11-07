package area.data;

import java.util.List;
import com.mongodb.*;

public class DataManager {
	private MongoClient mongoclient;
	private DB 			 db;
	private DBCollection user_table;

	public DataManager() {
	   	try {
			mongoclient = new MongoClient( "localhost" , 27017);
        	db = mongoclient.getDB("area");
			user_table = db.getCollection("user");
		}
		catch (Exception test){
			test.printStackTrace();
		}
	}

	public DB GetDb() {
		return db;
	}

	public DBCollection GetUserT() {
		return user_table;
	}

	public MongoClient GetMongo() {
		return mongoclient;
	}

	public int add_user(String mail, String pwd) {
		if (check_user(mail) == 0)
			return (-1);
		BasicDBObject obj = new BasicDBObject();
		obj.put("mail", mail);
		obj.put("password", pwd);
		user_table.insert(obj);
		return (0);
	}

	public int check_user(String mail) {

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("mail", mail);

		DBCursor cursor = user_table.find(searchQuery);
		if (cursor.hasNext())
			return (0);
		return (-1);
	}

	public int check_user(String mail, String pwd) {
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("mail", mail);
		searchQuery.put("password", pwd);

		if (check_user(mail) == 0) {
			DBCursor cursor = user_table.find(searchQuery);
			if (cursor.hasNext())
				return (0);
			return (-1);
		}
		return (-1);
	}

	public int delete_user(String mail) {
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("mail", mail);

		DBCursor cursor = user_table.find(searchQuery);
		if (cursor.hasNext()) {
			user_table.remove(searchQuery);
			return (0);
		}
		return (-1);
	}

	public void add_token(String mail, String token) {
		BasicDBObject query = new BasicDBObject();
		query.put("mail", mail);

		BasicDBObject newDoc = new BasicDBObject();
		newDoc.put("twi_token", token);

		user_table.update(query, newDoc);
	}
}
