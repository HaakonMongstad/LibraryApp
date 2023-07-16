package network;

import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import com.google.gson.Gson;

import com.mongodb.client.model.Updates;
import org.bson.Document;

import com.google.gson.GsonBuilder;

import com.mongodb.client.FindIterable;
import database.Item;
import database.mongoDB;

import java.util.Random;

import javafx.scene.image.Image;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

class Server extends Observable {

    ArrayList<Item> items = new ArrayList<>();
    mongoDB mongo;
    Iterator it;
    FindIterable<Document> docs;
    Document doc;

    public static void main(String[] args) {
        new Server().runServer();
    }

    private void runServer() {
        try {
            mongo = new mongoDB();
            mongo.start();
            setUpNetworking();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void setUpNetworking() throws Exception {
        @SuppressWarnings("resource")
        ServerSocket serverSock = new ServerSocket(4242);
        while (true) {
            Socket clientSocket = serverSock.accept();
            System.out.println("Connecting to... " + clientSocket);
            ClientHandler handler = new ClientHandler(this, clientSocket);
            this.addObserver(handler);
            Thread t = new Thread(handler);
            t.start();
        }
    }

    protected void processRequest(String input, ClientHandler client) {
        String output = "Error";
        Gson gson = new Gson();
        Message message = gson.fromJson(input, Message.class);
        Message send;
        GsonBuilder builder;
        try {
            String temp = "";
            switch (message.type) {
                case LOGIN:
                    if (!mongo.userExists(message.input1)){
                        send = new Message(messageType.LOGINFAILED, "","",null,0);
                        builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    else if (!mongo.passwordMatch(message.input1, message.input2)){
                        send = new Message(messageType.LOGINFAILED, "","",null,0);
                        builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    else{
                        items.clear();
                        docs = mongoDB.itemCollection.find();
                        it = docs.iterator();
                        while(it.hasNext()){
                            doc = (Document)it.next();
                            byte[] b = null;
                            try (FileInputStream stream = new FileInputStream("C:/Users/skjal/OneDrive/OneDrive Documents/GitHub/sp-23-final-project-HaakonMongstad/ServerSide/src/network/" + doc.get("img") + ".png")) {
                                b = new byte[stream.available()];
                                stream.read(b);
                            } catch (Exception e) {
                            }
                            if (mongo.getInventory(message.input1).contains((String)doc.get("title"))){
                                items.add(new Item((String) doc.get("type"),(String) doc.get("title"), (String) doc.get("author"),
                                        (String) doc.get("length"), (String) doc.get("summary"), b,(int)doc.get("qnt")));
                            }
                        }
                        System.out.println(mongo.findUser(message.input1).get("inventory"));
                        send = new Message(messageType.LOGINSUCCEED, message.input1, "",items,0);
                        builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    break;
                case REGISTER:
                    if (mongo.userExists(message.input1)){
                        send = new Message(messageType.REGISTERFAIL, "","",null,0);
                        builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    else{
                        mongo.createUser(message.input1, message.input2);
                        send = new Message(messageType.REGISTERSUCCESS, "","",null,0);
                        builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    break;
                case LOADCATALOG:
                    items.clear();
                    docs = mongoDB.itemCollection.find();
                    it = docs.iterator();
                    while(it.hasNext()){
                        doc = (Document) it.next();
                        byte[] b = null;
                        if (doc.get("type").equals(message.input1)) {
                            try (FileInputStream stream = new FileInputStream("C:/Users/skjal/OneDrive/OneDrive Documents/GitHub/sp-23-final-project-HaakonMongstad/ServerSide/src/network/" + doc.get("img") + ".png")) {
                                b = new byte[stream.available()];
                                stream.read(b);
                            } catch (Exception e) {
                            }
                            items.add(new Item((String) doc.get("type"),(String) doc.get("title"), (String) doc.get("author"),
                                    (String) doc.get("length"), (String) doc.get("summary"), b,(int)doc.get("qnt")));
                        }
                    }
                    send = new Message(messageType.LOADCATALOG, message.input1, "",items,0);
                    builder = new GsonBuilder();
                    gson = builder.create();
                    client.sendToClient(gson.toJson(send));
                    break;
                case CHECKOUT:
                    if(message.items.get(0).qnt <= 0){
                        send = new Message(messageType.CHECKOUTFAIL, "","",null,0);
                        builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    else if (((ArrayList<String>)mongo.findUser(message.input1).get("inventory")).size() >= 3){
                        send = new Message(messageType.CHECKOUTFAIL, "","",null,0);
                        builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    else if(((ArrayList<String>)mongo.findUser(message.input1).get("inventory")).contains(message.items.get(0).title)){
                        send = new Message(messageType.CHECKOUTFAIL, "","",null,0);
                        builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    else{
                        mongo.decrementQnt(message.items.get(0));
                        mongo.addItem(message.input1, message.items.get(0));

                        items.clear();
                        ArrayList<Item> allItems = new ArrayList<>();
                        docs = mongoDB.itemCollection.find();
                        it = docs.iterator();
                        while(it.hasNext()){
                            doc = (Document)it.next();
                            byte[] b = null;
                            try (FileInputStream stream = new FileInputStream("C:/Users/skjal/OneDrive/OneDrive Documents/GitHub/sp-23-final-project-HaakonMongstad/ServerSide/src/network/" + doc.get("img") + ".png")) {
                                b = new byte[stream.available()];
                                stream.read(b);
                            } catch (Exception e) {
                            }
                            allItems.add(new Item((String) doc.get("type"),(String) doc.get("title"), (String) doc.get("author"),
                                    (String) doc.get("length"), (String) doc.get("summary"), b,(int)doc.get("qnt")));

                            if (mongo.getInventory(message.input1).contains((String)doc.get("title"))){
                                items.add(new Item((String) doc.get("type"),(String) doc.get("title"), (String) doc.get("author"),
                                        (String) doc.get("length"), (String) doc.get("summary"), b,(int)doc.get("qnt")));
                            }
                        }

                        send = new Message(messageType.CHECKOUTSUCCESS, "","",items,0);
                        Message update = new Message(messageType.UPDATE,"","",allItems,0 );
                        builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                        this.setChanged();
                        this.notifyObservers(gson.toJson(update));
                    }
                    break;
                case RETURN:
                    docs = mongoDB.userCollection.find();
                    it = docs.iterator();
                    Message unreserve = null;
                    Message otherInventory = null;
                    boolean found = false;
                    while(it.hasNext()){
                        doc = (Document)it.next();
                        if (((ArrayList<String>)doc.get("reservations")).contains(message.items.get(0).title)){
                            found = true;
                            mongo.removeReservation((String)doc.get("username"),message.items.get(0));
                            mongo.addItem((String)doc.get("username"),message.items.get(0));
                            ArrayList<Item> titleToItem = new ArrayList<>();
                            ArrayList<Item> checkouts = new ArrayList<>();
                            for (String s : (ArrayList<String>)doc.get("reservations")){
                                Document d = mongo.findItem(s);
                                byte[] b = null;
                                try (FileInputStream stream = new FileInputStream("C:/Users/skjal/OneDrive/OneDrive Documents/GitHub/sp-23-final-project-HaakonMongstad/ServerSide/src/network/" + doc.get("img") + ".png")) {
                                    b = new byte[stream.available()];
                                    stream.read(b);
                                } catch (Exception e) {
                                }
                                if (!(message.items.get(0).title.equals(s))) {
                                    titleToItem.add(new Item((String) d.get("type"), (String) d.get("title"), (String) d.get("author"),
                                            (String) d.get("length"), (String) d.get("summary"), b, (int) d.get("qnt")));
                                }
                            }
                            for (String s : (ArrayList<String>)doc.get("inventory")){
                                Document d = mongo.findItem(s);
                                byte[] b = null;
                                try (FileInputStream stream = new FileInputStream("C:/Users/skjal/OneDrive/OneDrive Documents/GitHub/sp-23-final-project-HaakonMongstad/ServerSide/src/network/" + doc.get("img") + ".png")) {
                                    b = new byte[stream.available()];
                                    stream.read(b);
                                } catch (Exception e) {
                                }
                                checkouts.add(new Item((String) d.get("type"),(String) d.get("title"), (String) d.get("author"),
                                        (String) d.get("length"), (String) d.get("summary"), b,(int)d.get("qnt")));
                            }
                            checkouts.add(message.items.get(0));
                            titleToItem.remove(message.items.get(0));

                            System.out.println("checkouts " + checkouts);
                            System.out.println("titletoitem" + titleToItem);
                            otherInventory = new Message(messageType.UPDATEONE,(String)doc.get("username"),"",checkouts,0);
                            unreserve = new Message(messageType.RESERVEONE,(String)doc.get("username"),"",titleToItem,0);
                            break;
                        }
                    }
                    if (!found) {
                        mongo.incrementQnt(message.items.get(0));
                    }
                    mongo.removeItem(message.input1, message.items.get(0));
                    items.clear();
                    ArrayList<Item> allItems = new ArrayList<>();
                    docs = mongoDB.itemCollection.find();
                    it = docs.iterator();
                    while(it.hasNext()){
                        doc = (Document)it.next();
                        byte[] b = null;
                        try (FileInputStream stream = new FileInputStream("C:/Users/skjal/OneDrive/OneDrive Documents/GitHub/sp-23-final-project-HaakonMongstad/ServerSide/src/network/" + doc.get("img") + ".png")) {
                            b = new byte[stream.available()];
                            stream.read(b);
                        } catch (Exception e) {
                        }
                        allItems.add(new Item((String) doc.get("type"),(String) doc.get("title"), (String) doc.get("author"),
                                (String) doc.get("length"), (String) doc.get("summary"), b,(int)doc.get("qnt")));

                        if (mongo.getInventory(message.input1).contains((String)doc.get("title"))){
                            items.add(new Item((String) doc.get("type"),(String) doc.get("title"), (String) doc.get("author"),
                                    (String) doc.get("length"), (String) doc.get("summary"), b,(int)doc.get("qnt")));
                        }
                    }
                    send = new Message(messageType.CHECKOUTSUCCESS, "","",items,0);
                    Message update = new Message(messageType.UPDATE,"","",allItems,0 );
                    builder = new GsonBuilder();
                    gson = builder.create();
                    if (unreserve != null){
                        this.setChanged();
                        this.notifyObservers(gson.toJson(unreserve));
                        this.setChanged();
                        this.notifyObservers(gson.toJson(otherInventory));
                    }
                    client.sendToClient(gson.toJson(send));
                    this.setChanged();
                    this.notifyObservers(gson.toJson(update));
                    break;
                case RESERVE:
                    if (((ArrayList<String>)mongo.findUser(message.input1).get("reservations")).contains(message.items.get(0).title)
                    || ((ArrayList<String>)mongo.findUser(message.input1).get("inventory")).contains(message.items.get(0).title)){
                        send = new Message(messageType.CHECKOUTFAIL, "","",null,0);
                        builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    else if((((ArrayList<String>)mongo.findUser(message.input1).get("inventory")).size() +
                            ((ArrayList<String>)mongo.findUser(message.input1).get("inventory")).size()) >= 3){
                        send = new Message(messageType.CHECKOUTFAIL, "","",null,0);
                        builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    else{
                        mongo.addReservation(message.input1, message.items.get(0));
                        allItems = new ArrayList<>();
                        items.clear();
                        Document user = mongo.findUser(message.input1);
                        it = docs.iterator();
                        while(it.hasNext()){
                            doc = (Document)it.next();
                            byte[] b = null;
                            try (FileInputStream stream = new FileInputStream("C:/Users/skjal/OneDrive/OneDrive Documents/GitHub/sp-23-final-project-HaakonMongstad/ServerSide/src/network/" + doc.get("img") + ".png")) {
                                b = new byte[stream.available()];
                                stream.read(b);
                            } catch (Exception e) {
                            }
                            allItems.add(new Item((String) doc.get("type"),(String) doc.get("title"), (String) doc.get("author"),
                                    (String) doc.get("length"), (String) doc.get("summary"), b,(int)doc.get("qnt")));
                        }

                        for (String reservation : (ArrayList<String>)user.get("reservations")){
                            Document doc = mongo.findItem(reservation);
                            byte[] b = null;
                            try (FileInputStream stream = new FileInputStream("C:/Users/skjal/OneDrive/OneDrive Documents/GitHub/sp-23-final-project-HaakonMongstad/ServerSide/src/network/" + doc.get("img") + ".png")) {
                                b = new byte[stream.available()];
                                stream.read(b);
                            } catch (Exception e) {
                            }
                            items.add(new Item((String) doc.get("type"),(String) doc.get("title"), (String) doc.get("author"),
                                    (String) doc.get("length"), (String) doc.get("summary"), b,(int)doc.get("qnt")));
                        }
                        send = new Message(messageType.RESERVESUCCESS, "","",items,0);
                        update = new Message(messageType.UPDATE,"","",allItems,0);
                        builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                        client.sendToClient(gson.toJson(update));
                    }
                    break;
                case UNRESERVE:
                    mongo.removeReservation(message.input1,message.items.get(0));
                    allItems = new ArrayList<>();
                    items.clear();
                    Document user = mongo.findUser(message.input1);
                    it = docs.iterator();
                    while(it.hasNext()){
                        doc = (Document)it.next();
                        byte[] b = null;
                        try (FileInputStream stream = new FileInputStream("C:/Users/skjal/OneDrive/OneDrive Documents/GitHub/sp-23-final-project-HaakonMongstad/ServerSide/src/network/" + doc.get("img") + ".png")) {
                            b = new byte[stream.available()];
                            stream.read(b);
                        } catch (Exception e) {
                        }
                        allItems.add(new Item((String) doc.get("type"),(String) doc.get("title"), (String) doc.get("author"),
                                (String) doc.get("length"), (String) doc.get("summary"), b,(int)doc.get("qnt")));
                    }

                    for (String reservation : (ArrayList<String>)user.get("reservations")){
                        Document doc = mongo.findItem(reservation);
                        byte[] b = null;
                        try (FileInputStream stream = new FileInputStream("C:/Users/skjal/OneDrive/OneDrive Documents/GitHub/sp-23-final-project-HaakonMongstad/ServerSide/src/network/" + doc.get("img") + ".png")) {
                            b = new byte[stream.available()];
                            stream.read(b);
                        } catch (Exception e) {
                        }
                        items.add(new Item((String) doc.get("type"),(String) doc.get("title"), (String) doc.get("author"),
                                (String) doc.get("length"), (String) doc.get("summary"), b,(int)doc.get("qnt")));
                    }
                    send = new Message(messageType.RESERVESUCCESS, "","",items,0);
                    update = new Message(messageType.UPDATE,"","",allItems,0);
                    builder = new GsonBuilder();
                    gson = builder.create();
                    client.sendToClient(gson.toJson(send));
                    client.sendToClient(gson.toJson(update));
                    break;
                case SEARCH:
                    items.clear();
                    docs = mongoDB.itemCollection.find();
                    it = docs.iterator();
                    while(it.hasNext()){
                        doc = (Document)it.next();
                        byte[] b = null;
                        try (FileInputStream stream = new FileInputStream("C:/Users/skjal/OneDrive/OneDrive Documents/GitHub/sp-23-final-project-HaakonMongstad/ServerSide/src/network/" + doc.get("img") + ".png")) {
                            b = new byte[stream.available()];
                            stream.read(b);
                        } catch (Exception e) {
                        }
                        if (message.input1.equals((String)doc.get("title"))){
                            items.add(new Item((String) doc.get("type"),(String) doc.get("title"), (String) doc.get("author"),
                                    (String) doc.get("length"), (String) doc.get("summary"), b,(int)doc.get("qnt")));
                        }
                    }
                    send = new Message(messageType.LOADCATALOG, "","",items,0);
                    builder = new GsonBuilder();
                    gson = builder.create();
                    client.sendToClient(gson.toJson(send));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

