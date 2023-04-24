
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
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class mongoDB {
    private static Random rand = new Random((new Date()).getTime());
    public static MongoClient mongo;
    public static MongoDatabase database;
    public static MongoCollection<Document> userCollection;
    public static MongoCollection<Document> itemCollection;
    public static MongoCollection<Document> userInventory;
     private static final String URI =
            "mongodb+srv://Haakon:Trmn8r01@library.bcdyb5u.mongodb.net/?retryWrites=true&w=majority";
    private static final String DB = "Library";
    private static final String COLLECTION = "users";
    public static void start() {
        mongo = MongoClients.create(URI);
        database = mongo.getDatabase(DB);
        userCollection = database.getCollection(COLLECTION);
        itemCollection = database.getCollection("items");
        userInventory = database.getCollection("user inventory");

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
    public Document findItem(String item){
        return (Document)itemCollection.find(Filters.eq("title",item)).first();
    }
    public Document getItem(Item item){
        return (Document)itemCollection.find(Filters.eq("title",item.title)).first();
    }
    public void decrementQnt(Item item){
        itemCollection.updateOne(getItem(item),Updates.set("qnt",(getItem(item).getInteger("qnt")) - 1));
    }
    public void incrementQnt(Item item){
        itemCollection.updateOne(getItem(item),Updates.set("qnt",(getItem(item).getInteger("qnt")) + 1));

    }
    public void addItem(String username,Item item){
        userCollection.findOneAndUpdate(Filters.eq("username",username), Updates.push("inventory",item.title));
    }
    public void removeItem(String username,Item item){
        userCollection.findOneAndUpdate(Filters.eq("username",username), Updates.pull("inventory",item.title));
    }
    public void addReservation(String username, Item item){
        userCollection.findOneAndUpdate(Filters.eq("username",username), Updates.push("reservations",item.title));
    }
    public void removeReservation(String username, Item item){
        System.out.println(username + " " + item);
        userCollection.findOneAndUpdate(Filters.eq("username",username), Updates.pull("reservations",item.title));
    }

    public boolean userExists(String username){
        return !(findUser(username) == null);
    }

    public boolean passwordMatch(String username, String password){
       Document d = findUser(username);
       return ((decrypt((String)d.get("password"))).equals(password));
    }
    public void createUser(String username, String password){
        Document d = new Document();
        d.put("username", username);
        d.put("password", encrypt(password));
        d.put("inventory", new ArrayList<String>());
        d.put("reservations", new ArrayList<String>());
        userCollection.insertOne(d);
    }

    public void creatUserInventory(String username){
        Document d = new Document();
        d.put(username, new ArrayList<Item>());
        userInventory.insertOne(d);
    }
//    public ArrayList<Item> titlesToItems(String username){
//        ArrayList<Item> items = new ArrayList<>();
//        ArrayList<String> titles =
//        return items;
//    }
    public ArrayList<String> getInventory(String username){
        return (ArrayList<String>) findUser(username).get("inventory");
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

    public static String encrypt(String str) {
        BASE64Encoder encoder = new BASE64Encoder();
        byte[] salt = new byte[8];
        rand.nextBytes(salt);
        return encoder.encode(salt) + encoder.encode(str.getBytes());
    }

    public static String decrypt(String encstr) {

        if (encstr.length() > 12) {
            String cipher = encstr.substring(12);
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                return new String(decoder.decodeBuffer(cipher));
            } catch (Exception e) {
            }
        }
        return null;
    }
}
