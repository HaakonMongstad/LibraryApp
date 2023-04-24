//package backend;
//
//import gui.logInController;
//
//public class logInBackend {
//    public boolean login = false;
//
//    Thread th;
//    logInController controller;
//    public logInBackend(logInController controller){
//        this.controller = controller;
//        th = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (login == false){
//                }
//                login = false;
//                controller.loginSuccess();
//            }
//        });
//    }
//    public void startBackend(){
//        Thread tempTH = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (login == false){
//                }
//                login = false;
//                controller.loginSuccess();
//            }
//        });
//        tempTH.start();
//
//    }
//}
