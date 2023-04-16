package network;
import backend.Messages;

class Message {
//    String type;
    Messages.messageType type;
    String input1;
    String input2;
    int number;
    protected Message() {
        this.type = Messages.messageType.NONE;
        this.input1 = "";
        this.input2 = "";
        this.number = 0;
        System.out.println("server-side message created");
    }
    protected Message(Messages.messageType type, String input1,String input2, int number) {
        this.type = type;
        this.input1 = input1;
        this.input2 = input2;
        this.number = number;
        System.out.println("server-side message created");
    }
}
