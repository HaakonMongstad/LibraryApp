package network;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

class Message {
    //    String type;
    messageType type;
    String input1;
    String input2;
    int number;
    ArrayList<database.Item> items;

    protected Message() {
        this.type = messageType.NONE;
        this.input1 = "";
        this.input2 = "";
        this.items = new ArrayList<>();
        this.number = 0;

        System.out.println("server-side message created");
    }
    protected Message(messageType type, String input1, String input2, ArrayList<database.Item> items, int number) {
        this.items = items;
        this.type = type;
        this.input1 = input1;
        this.input2 = input2;
        this.number = number;
        System.out.println("server-side message created");
    }
}
