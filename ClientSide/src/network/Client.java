package network;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gui.logInController;
import gui.mainController;

public class Client {
    private static String host = "127.0.0.1";
    private Socket socketCopy;
    private BufferedReader fromServer;
    private PrintWriter toServer;
    private Scanner consoleInput = new Scanner(System.in);
    public boolean logInPressed = false;
    private boolean newUserPressed = false;
    private String user;
    private  String password;
    public boolean loginSuccess = false;
    public boolean loginFail = false;

//    public logInBackend backend;
    public logInController controller1;
    public mainController controller2;
    public boolean registerPressed;
    public boolean messageCreated = false;
    public Message request = null;

    public static void main(String[] args) {
        try {
          //  new Client().setUpNetworking(backend);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUserPassword(String user, String password){
        this.user = user;
        this.password = password;
    }
    public void setMessage(messageType type, String input1, String input2, ArrayList<backend.Item> items, int number){
        request = new Message(type,input1,input2,items,number);
    }
    public void setMessageCreated(){
        messageCreated = true;
    }

    public void setLogInPressed(){
        logInPressed = true;
    }
    public void setNewUserPressed(){
        newUserPressed = true;
    }
    public void setUpNetworking(logInController controller) throws Exception {
        @SuppressWarnings("resource")
        Socket socket = new Socket(host, 4242);
        socketCopy = socket;
        System.out.println("Connecting to... " + socket);
        fromServer = new BufferedReader(new
                InputStreamReader(socket.getInputStream()));
        toServer = new PrintWriter(socket.getOutputStream());
        this.controller1 = controller;
        Thread readerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String input;
                try {
                    while ((input = fromServer.readLine()) != null) {
                        System.out.println("From server: " + input);
                        processRequest(input);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Thread writerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (messageCreated == true){
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        sendToServer(gson.toJson(request));
                        messageCreated = false;
                    }
//                    if (logInPressed == true){
//                        Message request = new Message(messageType.LOGIN,user,password,null,0);
//                        GsonBuilder builder = new GsonBuilder();
//                        Gson gson = builder.create();
//                        sendToServer(gson.toJson(request));
//                        logInPressed = false;
//                    }
//                    else if(registerPressed){
//                        Message request = new Message(messageType.REGISTER,user,password,null,0);
//                        GsonBuilder builder = new GsonBuilder();
//                        Gson gson = builder.create();
//                        sendToServer(gson.toJson(request));
//                        registerPressed = false;
//                    }
                }
            }
        });
        readerThread.start();
        writerThread.start();
    }
    public void closeSocket(){
        try {
            socketCopy.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    protected void processRequest(String input) {
        String output = "Error";
        Gson gson = new Gson();
        Message message = gson.fromJson(input, Message.class);
        try {
            String temp = "";
            switch (message.type) {
                case LOGINSUCCEED:
                    controller1.loginSuccess();
                    break;

                case LOGINFAILED:
                    break;
                case LOADCATALOG:
                    controller2.loadCatalog(message.items);
                    System.out.println("HEREE2");
                    break;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    protected void sendToServer(String string) {
        System.out.println("Sending to server: " + string);
        toServer.println(string);
        toServer.flush();
    }
}