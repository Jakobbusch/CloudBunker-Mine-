package network.client;

import network.shared.Account;
import network.shared.Request;
import network.shared.Files;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SocketClient implements Client {

    private ClientSocketHandler socketHandler;
    private Account acc;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    private Socket socket = null;
    private String filePath;


    /**
     * A constructor fr the SocketClient class that establishes the connection to the server and passes it to a new thead
     */

    public SocketClient() {
        try {
            socket = new Socket("localhost", 2910);
            System.out.println("Connected");
            socketHandler = new ClientSocketHandler(
                    new ObjectOutputStream(socket.getOutputStream()),
                    new ObjectInputStream(socket.getInputStream()), this);
            Thread t = new Thread(socketHandler);
            t.setDaemon(true);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method for sending an Account object used for logging in to the server
     *
     * @param acc An Account object that will be sent to the server
     */
    @Override
    public void setAccount(Account acc) {
        this.acc = acc;
        socketHandler.sendToServer(Request.TYPE.LOGIN, acc);
    }

    /**
     * A method to send a request to the server that a new account is created
     * @param acc An account object that will be sent to the server
     */
    @Override
    public void createAcc(Account acc) {

        socketHandler.sendToServer(Request.TYPE.CREATEACC, acc);
    }

    /**
     *A method fro sending messages to the server
     * @param message A String that contains the message that will be sent to the server
     */
    @Override
    public void sendMessage(String message) {
        String message1 = message +"\t"+ acc.getUsername();
        socketHandler.sendToServer(Request.TYPE.MESSAGE,message1);
    }

    /**
     * A method to send a request to the server that a file is deleted
     * @param filename A String containing the filename of the file that will be deleted
     */
    @Override
    public void deleteFile(String filename) {
        socketHandler.sendToServer(Request.TYPE.DELETEFILE,filename);
    }

    /**
     * A method to chek if the Account associated with this class is an admin
     * @return A boolean value that if true means that the account is an admin
     */
    @Override
    public boolean isAdmin() {
        return acc.isAdmin();
    }

    /**
     * A method that fires a PropertyChange that a file has been deleted
     * @param filename A String that contains the filename of the file that has been deleted
     */
    public void deleteFileFromList(String filename)
    {
        support.firePropertyChange("deleteFile",null,filename);
    }

    /**
     * A method to send a request to the server that a user will be deleted
     * @param username A string for the username of the user that will be deleted
     */
    @Override
    public void deleteUser(String username) {
        socketHandler.sendToServer(Request.TYPE.DELETE,username);
    }

    /**
     * A method for changing the password and sending that change to the server
     * @param acc An Account that contains the updated password
     */
    @Override
    public void editPass(Account acc) {
        this.acc = acc;
        socketHandler.sendToServer(Request.TYPE.EDITPASS, acc);

    }

    /**
     * A method for changing the e-mail of the ueser
     * @param acc An Account that contains the new e-mail
     */
    @Override
    public void editEmail(Account acc) {
        this.acc = acc;
        socketHandler.sendToServer(Request.TYPE.EDITEMAIL, acc);
    }

        @Override
        public void addPropertyChangeListener (String name, PropertyChangeListener listener){
            support.addPropertyChangeListener(name, listener
            );
        }

        @Override
        public void addPropertyChangeListener (PropertyChangeListener listener){
            support.addPropertyChangeListener(listener);
        }

        @Override
        public void removePropertyChangeListener (String name, PropertyChangeListener listener){
            support.removePropertyChangeListener(name, listener);
        }

        @Override
        public void removePropertyChangeListener (PropertyChangeListener listener){
            support.removePropertyChangeListener(listener);
        }

        /**
         * A method for getting the conformation or rejection of the login details that were provided by the client
         *
         * @param argument a boolean value that determine if the user will be logged into the system
         */
        public void loginResult ( boolean argument){
            support.firePropertyChange("LoginResult", null, argument);
        }

    /**
     * A method for setting the file-path
     * @param filePath A String object that contains the filepath
     */
        @Override
        public void recieveFile (String filePath){
            this.filePath = filePath;
        }

    /**
     * A method to send a Download request to the server for a specific file
     * @param fileName A String that contains the filename of the file that will be downloaded
     */
        @Override
        public void sendDownloadRequest (String fileName){
            System.out.println("Client : send Download Req");
            socketHandler.sendToServer(Request.TYPE.DOWNLOAD, fileName);
        }

    /**
     * A getter for the file path
     * @return A String containing the file path
     */
        public String getFilePath () {
            return filePath;
        }

    /**
     * A method for recieving files from the server
     * @param filePAth A String that designates the path where the file will be saved
     * @param files A Files object that contains the byte[] for the file
     */
        public void downloadFile (String filePath, Files files){
            // A name for the file to be received
            String FILE_TO_RECEIVED = files.getFileName();
            int bytesRead;
            int current = 0;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            try {
                System.out.println("Array size " + files.getMybytearray().length);
                fos = new FileOutputStream(filePAth + "\\" + FILE_TO_RECEIVED);
                bos = new BufferedOutputStream(fos);
                bos.write(files.getMybytearray());
                bos.flush();
                System.out.println("File " + FILE_TO_RECEIVED
                        + " downloaded ");

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
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
     * A method for firing a Property change to the model manager that adds a new file to the ListView
     * @param files An ArrayList of type Files that will be put into the ListView
     */
    public void addNewFile (ArrayList<Files> files){
            support.firePropertyChange("newFileAdded", null, files);

        }
    /**
     * A method for firing a Property change to the model manager that adds a new user to the ListView
     * @param users An ArrayList of type Account that will be put into the ListView
     */
        public void addNewUser(ArrayList<Account> users){
            support.firePropertyChange("newUserAdded",null,users);
        }
    /**
     * A method for firing a Property change to the model manager that delets a user from the ListView
     * @param username An ArrayList of type Account that will be taken out of the ListView
     */
        public void deleteUserFromList(String username){
            support.firePropertyChange("deleteUserFromList",null,username);
        }

    /**
     * A method for setting the email in the Account variable
     * @param email A String for the e-mail
     */
        public void setEmail(String email){
            acc.setEmail(email);
        }

    /**
     * A method for firing a Property change to the model manager that adds a new message to the ListView
     * @param messages An ArrayList of type String containing a message  that will be put into the ListView
     */
        public void addNewMessage(ArrayList<String> messages){
            support.firePropertyChange("newMessageAdded",null,messages);
        }

    /**
     * A method for getting the Account information
     * @return A String object
     */
    @Override
        public String getAccountInfo(){
            System.out.println("USERNAME"+acc.getUsername());
            System.out.println( "EMAIL"+acc.getEmail());
            return acc.getUsername() + "\t" + acc.getEmail();

        }

    /**
     * A method for setting the account as an admin
     * @param temp An admin Account
     */
        public void setAdminAccount(Account temp)
        {
            this.acc = temp;
        }


        /**
         * A method for uploading Files to the server
         *
         * @param fileName A string used for the file name
         */

        @Override
        public void uploadFile (File fileName){
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            // OutputStream os = null;
            try {
                // File that will be sent
                File myFile = fileName;
                byte[] mybytearray = new byte[(int) myFile.length()];
                fis = new FileInputStream(myFile);
                bis = new BufferedInputStream(fis);
                bis.read(mybytearray, 0, mybytearray.length);
                //sending file
                System.out.println("Sending " + fileName.getName() + "(" + mybytearray.length + " bytes)");
                String filesize = String.valueOf(mybytearray.length/1024);
                String filetype = fileName.getName();
                SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss dd/MM/YYYY");
                Date now = new Date();
                String strDate = sdfDate.format(now);
                String lastUpdate = strDate;


                Files temp = new Files(fileName.getName(), mybytearray, lastUpdate, acc.getUsername(), fileName.getPath(), filetype.substring(filetype.lastIndexOf(".")), filesize + " KB");

                    socketHandler.sendToServer(Request.TYPE.FILE, temp);

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
    }



