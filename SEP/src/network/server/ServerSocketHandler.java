package network.server;

import network.database.Model;
import network.shared.Account;
import network.shared.Files;
import network.shared.Request;

import java.beans.PropertyChangeEvent;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class ServerSocketHandler implements Runnable{
    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;
    private Socket socket;
    private Model model;
    private ArrayList<String> theFiles;
    private ArrayList<Account> accounts;
    private ArrayList<String> messages;

    /**
     * A constuctor method  that initializes the Object input and output streams
     * @param socket a Socket object
     * @param model a connecton to the database interface
     */
    public ServerSocketHandler(Socket socket, Model model) {
        theFiles = new ArrayList<>();
        accounts=new ArrayList<Account>();

        this.socket = socket;
        this.model = model;

        try {
            outToClient = new ObjectOutputStream(socket.getOutputStream());
            inFromClient = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addPropertyChangeListener("FileAdded", this::fileAdded);
        model.addPropertyChangeListener("newUserAdded",this::userAdded);
        model.addPropertyChangeListener("newMessageAdded",this::messageAdded);
        model.addPropertyChangeListener("userDeleted",this::userDeleted);
        model.addPropertyChangeListener("fileDeleted",this::fileDeleted);

//        model.addListener("TaskRemoved", this::removeTask);
    }


    @Override
    public void run() {
        try {
            while (true) {
                    Request req = (Request) inFromClient.readObject();

                if(req.equals(null)){
                    System.out.println("Client disconnected :) ");
                }
                if (req.type == Request.TYPE.LOGIN) {

                    Account temp = (Account) req.get();


                    if (model.compareLogin(temp))
                    {
                        // send confirmation message
                        if(model.isAdmin(temp))
                        {
                            temp.setAdmin(true);
                            System.out.println("Server socket: IM AN ADMIN :)");
                            outToClient.writeObject(new Request(Request.TYPE.ADMIN, temp));
                        }

                        System.out.println("Login successful");
                        outToClient.writeObject(new Request(Request.TYPE.LOGIN, true));
                        outToClient.writeObject(new Request(Request.TYPE.EMAIL,model.getTempEmail()));
                        readFolderFiles();
                        //outToClient.writeObject(new Request(Request.TYPE.ADD, ));
                        accounts.add(temp);
                        outToClient.writeObject(new Request(Request.TYPE.ADD,  model.fileArray()));
                        outToClient.writeObject(new Request(Request.TYPE.ACCOUNTS, model.userArray()));

                    } else {
                        // send error message to client
                        System.out.println("Login unsuccessful");
                        outToClient.writeObject(new Request(Request.TYPE.LOGIN, false));
                    }
                } else if (req.type == Request.TYPE.REMOVE) {

                }
                else if(req.type== Request.TYPE.DELETE)
                {
                    String username = (String)req.argument;
                    model.deleteUser(username);
                }
                else if ( req.type== Request.TYPE.FILE)
                {
                    //recieving files
                    Files temp=(Files)req.get();
                    recieveFle(temp);
                    System.out.println("file done");

                    temp.setPath("./myfolder/"+temp.getFileName());
                    model.addFile(temp);

                } else if(req.type == Request.TYPE.MESSAGE){
                    String temp = (String)req.get();
                    System.out.println("SSH MESSAGE: "+ temp);
                    model.addMessage(temp);

                }
                else if(req.type==Request.TYPE.EDITPASS)
                {
                    // recieve an account from the socketClient

                    for(int i=0;i<accounts.size();i++)
                    {
                        model.editPassword((Account) req.get());

                    }
                }
                else if(req.type==Request.TYPE.EDITEMAIL) {
                    // recieve an account from the socketClient

                    for (int i = 0; i < accounts.size(); i++) {
                        model.editEmail((Account) req.get());

                    }
                }
                else if(req.type==Request.TYPE.CREATEACC)
                {

                        model.createUser((Account) req.get());


                }



                else if(req.type== Request.TYPE.DOWNLOAD)
                {
                    // send requested file to the client
                    System.out.println("ServerSocketHandler: receive download req and send file");
                    String temp=(String)req.get();
                    String path = model.getPath(temp);
                    System.out.println("am i null? " + path);
                    sendFile(path);

                }
                else if (req.type==Request.TYPE.DELETEFILE)
                {
                    String filename=(String) req.argument;
                    model.deleteFile(filename);
                }

                else {
                    System.out.println("Wrong request type");
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client disconnected :)");
        }
    }

    /**
     * A method for intial reading of the files from the database and adn adding them to the listvView
     */
    public void readFolderFiles(){
        File folder = new File("./myfolder");
        File [] listOfFiles = folder.listFiles();

        for(File file : listOfFiles)
        {
            if(!file.equals(null))
            {

                theFiles.add(file.getName());
            }
        }
    }

    /**
     * A method for receiving a file from the client
     * * @param fileName A string attribute for the file that is going to be sent
     */
    public void recieveFle(Files files)  {
        // A name for the file to be received
        String FILE_TO_RECEIVED = files.getFileName();
        int bytesRead;
        int current=0;
        FileOutputStream fos=null;
        BufferedOutputStream bos=null;
        try{
            System.out.println("Array size "+files.getMybytearray().length);
            fos = new FileOutputStream("./myfolder/" +FILE_TO_RECEIVED);
            bos = new BufferedOutputStream(fos);
            bos.write(files.getMybytearray());

            bos.flush();
            System.out.println("File "+FILE_TO_RECEIVED
                    + " downloaded (" + current + " bytes read)");

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * A method for sending a file to the client
     * @param filePath A String that contains the filepath of the file from the server
     */
    public void sendFile(String filePath) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;

        try {
            // File that will be sent
            File myFile = new File(filePath);
            byte[] mybytearray = new byte[(int) myFile.length()];
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);
            bis.read(mybytearray, 0, mybytearray.length);

            System.out.println("Sending " + myFile.getName() + "(" + mybytearray.length + " bytes)");
            // sends the file

            Request temp=new Request(Request.TYPE.DOWNLOAD,new Files(myFile.getName(),mybytearray));
            outToClient.writeObject(temp);
            System.out.println("Done.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        }
    }



    /**
     * A method for sending a Request object of type ADD to the client
     * @param evt an Property change event that is used to get the object that will be send to the client
     */
    public void fileAdded(PropertyChangeEvent evt) {
        try {
            Files newlyAdded = (Files) evt.getNewValue();
            ArrayList<Files> list = new ArrayList<>();
            list.add(newlyAdded);
            Request req = new Request(Request.TYPE.ADD, list);
            outToClient.writeObject(req);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method for adding aand distributing a message to all the clients
     * @param evt A Property change event that contains the message
     */
    public void messageAdded(PropertyChangeEvent evt){
        try{
            String newlyAdded = (String)evt.getNewValue();
            ArrayList<String> list = new ArrayList<>();
            list.add(newlyAdded);
            System.out.println(list);
            Request req = new Request(Request.TYPE.MESSAGE,list);

            outToClient.writeObject(req);

        }catch (NullPointerException | IOException e){
            e.printStackTrace();
        }
    }

    /**
     * A method to add a user to the List of accounts
     * @param evt  A property change event that contains the user that will be added
     */
    public void userAdded(PropertyChangeEvent evt) {
        try {
            Account newlyAdded = (Account) evt.getNewValue();
            ArrayList<Account> list = new ArrayList<>();
            list.add(newlyAdded);
            Request req = new Request(Request.TYPE.ACCOUNTS, list);

            outToClient.writeObject(req);
        }catch (IOException e) {
                e.printStackTrace();
            }

        }

    /**
     * A method for deleting an user from the List of users
     * @param evt A Property change event that contains a String for the username of the user that will be deleted
     */
    public void userDeleted(PropertyChangeEvent evt)
        {
            String username=(String)evt.getNewValue();

            Request req = new Request(Request.TYPE.DELETE, username);
            try {
                outToClient.writeObject(req);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    /**
     * A remove method for sending a Request of type REMOVE to the client
     * @param evt  an Property change event that is used to get the object that will be send to the client
     */
    public void removeTask(PropertyChangeEvent evt) {
        try {
            Request req = new Request(Request.TYPE.REMOVE, evt.getNewValue());
            outToClient.writeObject(req);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method for deleting  file from the listView of all clients
     * @param evt A Property change event that contains a String for the filename of the file that will be deleted
     */
    public void fileDeleted(PropertyChangeEvent evt)
    {
        String filename=(String)evt.getNewValue();
        Request request=new Request(Request.TYPE.DELETEFILE,filename);
        try {
            outToClient.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}