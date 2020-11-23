package user.model;


import network.client.Client;
import network.shared.Account;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;


public class DataModelManager implements DataModel {


    private Client client;
    private DataModel model;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);


    /**
     * A constructor for setting the client
     * @param client A client object
     */
    public DataModelManager(Client client) {
        this.client = client;
        client.addPropertyChangeListener("LoginResult", this::loginResult);
        client.addPropertyChangeListener("newFileAdded",this::newFileAdded);
        client.addPropertyChangeListener("newUserAdded",this::newUserAdded);
        client.addPropertyChangeListener("newMessageAdded",this::newMessageAdded);
       // client.addPropertyChangeListener("userDeleted",this::userDeleted);
        client.addPropertyChangeListener("deleteUserFromList",this::userDeleted);
        client.addPropertyChangeListener("deleteFile",this::deleteFileFromList);
    }

    /**
     * A method for setting the client
     * @param client a Client object
     */
    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * A method to forward the property change event for deleting a file from the list
     * @param evt a Property change event that will be forwarded
     */
    public void deleteFileFromList(PropertyChangeEvent evt)
    {
        support.firePropertyChange(evt);
    }

    /**
     * A method for forwarding the delete file request to the client
     * @param filename
     */
    @Override
    public void deleteFile(String filename) {
        client.deleteFile(filename);
    }

    /**
     * A method for checking is the client an admin
     * @return A boolean taht if true means that the client is an admin
     */
    @Override
    public boolean isAdmin() {
        return client.isAdmin();
    }

    /**
     * A method for sending a login request to the server
     * @param acc An Account object that will be sent
     */
    @Override
    public void sendLogin(Account acc) {
        client.setAccount(acc);
    }
    /**
     * A method to forward the property change event for adding a user to the list
     * @param evt a Property change event that will be forwarded
     */
    public void newUserAdded(PropertyChangeEvent evt){
        support.firePropertyChange(evt);
    }
    /**
     * A method to forward the property change event for deleting a user from the list
     * @param evt a Property change event that will be forwarded
     */
    public void userDeleted(PropertyChangeEvent evt)
    {
        support.firePropertyChange(evt);
    }
    /**
     * A method to forward the property change event for adding a file ro the list
     * @param evt a Property change event that will be forwarded
     */
    public void newFileAdded(PropertyChangeEvent evt){
        support.firePropertyChange(evt);

    }
    /**
     * A method to forward the property change event for adding a message
     * @param evt a Property change event that will be forwarded
     */
    public void newMessageAdded(PropertyChangeEvent evt){
        support.firePropertyChange(evt);
    }

    /**
     * A method to forward the property change event for the result from the login
     * @param evt a Property change event that will be forwarded
     */
    public void loginResult(PropertyChangeEvent evt) {

        support.firePropertyChange(evt);

    }

    /**
     * A method for receiving a file
     * @param filePath  a String that contains the filepath
     */
    @Override
    public void recieveFile(String filePath) {
        client.recieveFile(filePath);
    }

    /**
     * A method that send a download request to the server
     * @param fileName A String containing the name of the file that will be downloaded
     */
    @Override
    public void sendDownloadRequest(String fileName) {
        System.out.println("DataModelManager: send Download Req");
        client.sendDownloadRequest(fileName);
    }


    @Override
    public void addPropertyChangeListener(String name, PropertyChangeListener listener) {
        support.addPropertyChangeListener(name, listener
        );
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(String name, PropertyChangeListener listener) {
        support.removePropertyChangeListener(name, listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    /**
     * A method for uploading a file to the server
     * @param file A File Object that will be sent to the server
     */
    @Override
    public void sendFile(File file) {
        client.uploadFile(file);
    }

    /**
     * A method for editing the password of the user
     * @param temp An Account object that contains the new password
     */
    @Override
    public void sendPassLogin(Account temp) {
        System.out.println("Edit password");
        client.editPass(temp);

    }

    /**
     * A method used for changing the e-mail of the user
     * @param temp An Account object that contains the new e-mail
     */
    @Override
    public void sendEmailLogin(Account temp) {
        System.out.println("Edit Email");
        client.editEmail(temp);

    }

    /**
     * A method used for creating an Account
     * @param temp An Account object that will be created in the database
     */
    @Override
    public void sendAcc(Account temp) {
        System.out.println("Create account");
        client.createAcc(temp);

    }

    /**
     * A method for sending a message to the serer
     * @param message A Sting that contains the message
     */
    @Override
    public void sendMessage(String message) {
        client.sendMessage(message);
    }

    /**
     *A method for deleting a user
     * @param username A Sting object that contains the username of the user that will be deleted
     */
    @Override
    public void deleteUser(String username) {
        client.deleteUser(username);
    }

    /**
     * A method for getting the account information
     * @return a String containing all the information about the account
     */
    @Override
    public String getAccountInfo() {
       return client.getAccountInfo();
    }


}

