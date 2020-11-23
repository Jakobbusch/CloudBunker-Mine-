package network.client;


import network.shared.Account;
import network.shared.Files;
import network.shared.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ClientSocketHandler implements Runnable {

    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private SocketClient socketClient;


    /**
     * A constructor used for initializing the output and input streams as well as the socket client
     *
     * @param outToServer  and ObjectOutputStream object used for writing to the server
     * @param inFromServer an ObjectInputStream object used for receiving information from the server
     * @param socketClient a SocketClient object that is used for handling the requests that the client receives from the server
     */

    public ClientSocketHandler(ObjectOutputStream outToServer, ObjectInputStream inFromServer, SocketClient socketClient) {

        this.outToServer = outToServer;
        this.inFromServer = inFromServer;
        System.out.println(socketClient.toString());
        this.socketClient = socketClient;
    }

    @Override
    public void run() {
        try {

            while (true) {
                Request req = (Request) inFromServer.readObject();
                if (req.type == Request.TYPE.LOGIN) {
                    socketClient.loginResult((boolean) req.argument);
                } else if(req.type == Request.TYPE.EMAIL){
                    String email = (String) req.get();
                    socketClient.setEmail(email);
                }

                else if (req.type == Request.TYPE.ADD) {

                    System.out.println("Den er her :) ");
                    Object argument1 = req.argument;
                    ArrayList<Files> argument = (ArrayList<Files>) argument1;
                    socketClient.addNewFile(argument);


                } else if (req.type == Request.TYPE.DOWNLOAD) {
                    System.out.println("receiving file path and file");
                    network.shared.Files temp = (network.shared.Files) req.get();
                    socketClient.downloadFile(socketClient.getFilePath(), temp);
                } else if (req.type == Request.TYPE.ACCOUNTS) {

                    Object argument1 = req.argument;
                    ArrayList<Account> argument = (ArrayList<Account>) argument1;
                    socketClient.addNewUser(argument);


                }else if(req.type== Request.TYPE.DELETE)
                {
                    String username=(String) req.argument;
                    socketClient.deleteUserFromList(username);
                }
                else if(req.type == Request.TYPE.MESSAGE){
                    Object argument1 = req.get();
                    ArrayList<String> argument = (ArrayList<String>)argument1;
                    socketClient.addNewMessage(argument);
                }
                else if( req.type== Request.TYPE.DELETEFILE)
                {
                    String filename=(String) req.argument;
                    socketClient.deleteFileFromList(filename);
                }
                else if (req.type == Request.TYPE.ADMIN)
                {
                    Account temp = (Account) req.argument;
                    System.out.println("clientSocket : "+temp.isAdmin());
                    socketClient.setAdminAccount(temp);
                }
            }
        } catch (Exception e) {
            System.out.println("Lost connection to server :( ");
        }
    }

    /**
     * A method for sending requests to the server
     * @param type A Request type
     * @param obj  An Object that will be sent to the server
     */
    public void sendToServer(Request.TYPE type, Object obj) {
        try {
            outToServer.writeObject(new Request(type, obj));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



