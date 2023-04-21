
package database;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class mongoDB {
    public static MongoClient mongo;
    public static MongoDatabase database;
    public static MongoCollection<Document> userCollection;
    public static MongoCollection<Document> itemCollection;
    private static final String URI =
            "mongodb+srv://Haakon:Trmn8r01@library.bcdyb5u.mongodb.net/?retryWrites=true&w=majority";
    private static final String DB = "Library";
    private static final String COLLECTION = "users";
    public static void start() {
        mongo = MongoClients.create(URI);
        database = mongo.getDatabase(DB);
        userCollection = database.getCollection(COLLECTION);
        itemCollection = database.getCollection("items");

//ping();
 //       update();
//findAndRead();
//        mongo.close();
    }
    public static void update() {
        Document doc = new Document();
        doc.put("name", "My First Program");
        doc.put("type", "Code");
        doc.put("price", 0.0);
        doc.put("winner", "");
        userCollection.insertOne(doc);
        doc.put("foo", "bar");
    }

    public Document findUser(String username){
        return (Document)userCollection.find(Filters.eq("username", username)).first();
    }
    public boolean userExists(String username){
        return !(findUser(username) == null);
    }

    public boolean passwordMatch(String username, String password){
       Document d = findUser(username);
       return (d.get("password").equals(password));
    }
    public void createUser(String username, String password){
        Document d = new Document();
        d.put("username", username);
        d.put("password", password);
        userCollection.insertOne(d);
    }
    public void close(){
        mongo.close();
    }
    public static void ping() {
        try {
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            Document commandResult = database.runCommand(command);
            System.out.println("Connected successfully to server.");
        } catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
        }
    }
}
