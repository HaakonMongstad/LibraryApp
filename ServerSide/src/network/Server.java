package network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import com.google.gson.Gson;

import backend.Messages;
import com.google.gson.GsonBuilder;

class Server extends Observable {

    Map<String,String> users = new HashMap<>();

    public static void main(String[] args) {
        new Server().runServer();
    }

    private void runServer() {
        try {
            users.put("a","b");
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
                    if (!users.containsKey(message.input1)){
                        Message send = new Message(Messages.messageType.LOGINFAILED, "","",0);
                        GsonBuilder builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    else if (!users.get(message.input1).equals(message.input2)){
                        Message send = new Message(Messages.messageType.LOGINFAILED, "","",0);
                        GsonBuilder builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    else{
                        Message send = new Message(Messages.messageType.LOGINSUCCEED, "","",0);
                        GsonBuilder builder = new GsonBuilder();
                        gson = builder.create();
                        client.sendToClient(gson.toJson(send));
                    }
                    break;
//                case "lower":
//                    temp = message.input.toLowerCase();
//                    break;
//                case "strip":
//                    temp = message.input.replace(" ", "");
//                    break;
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
