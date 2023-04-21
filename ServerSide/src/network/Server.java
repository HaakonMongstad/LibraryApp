package network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import com.google.gson.Gson;

import org.bson.Document;

import com.google.gson.GsonBuilder;

import com.mongodb.client.FindIterable;
import database.Item;
import database.mongoDB;

class Server extends Observable {

    ArrayList<Item> items = new ArrayList<>();
    mongoDB mongo;

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
        try {
            String temp = "";
            switch (message.type) {
                case LOGIN:
                    if (!mongo.userExists(message.input1)){
                        Message send = new Message(messageType.LOGINFAILED, "","",null,0);
                        GsonBuilder builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    else if (!mongo.passwordMatch(message.input1, message.input2)){
                        Message send = new Message(messageType.LOGINFAILED, "","",null,0);
                        GsonBuilder builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    else{
                        Message send = new Message(messageType.LOGINSUCCEED, "","",null,0);
                        GsonBuilder builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    break;
                case REGISTER:
                    if (mongo.userExists(message.input1)){
                        Message send = new Message(messageType.REGISTERFAIL, "","",null,0);
                        GsonBuilder builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    else{
                        mongo.createUser(message.input1, message.input2);
                        Message send = new Message(messageType.REGISTERSUCCESS, "","",null,0);
                        GsonBuilder builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    break;
                case LOADCATALOG:
                    items = new ArrayList<>();
                    FindIterable<Document> docs = mongoDB.itemCollection.find();
                    Iterator it = docs.iterator();
                    while(it.hasNext()){
                        Document doc = (Document) it.next();
                        if (doc.get("type").equals(message.input1)) {
                            items.add(new Item((String) doc.get("type"),(String) doc.get("title"), (String) doc.get("author"), (String) doc.get("length"), (String) doc.get("summary"), (String) doc.get("img")));
                        }
                    }
                    Message send = new Message(messageType.LOADCATALOG, message.input1, "",items,0);
                    GsonBuilder builder = new GsonBuilder();
                    gson = builder.create();
                    client.sendToClient(gson.toJson(send));
                    break;
            }
//            output = "";
//            for (int i = 0; i < message.number; i++) {
//                output += temp;
//                output += " ";
//            }
//            this.setChanged();
//            this.notifyObservers(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

