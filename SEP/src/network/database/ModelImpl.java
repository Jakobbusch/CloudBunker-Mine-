package network.database;

import network.shared.Account;
import network.shared.Files;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ModelImpl implements Model {

    private Database database;
    PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * A constructor
     * @param database A Database interface
     */
    public ModelImpl(Database database) {
        this.database = database;
    }

    /**
     * A method for returning the result from the database
     * @param temp An account object that will be compared to the database
     * @return a boolean value that if true logs the user in the system
     */
    @Override
    public boolean compareLogin(Account temp) {
        return database.compareLogin(temp);
    }

    /**
     * A method to return an object
     * @return A Object tat will be returned
     */
    @Override
    public Object fileArray() {
        return database.fileArray();
    }

    /**
     * A method to add a file to the database and Fire a Property change event
     * @param temp A files object that will be saved to the database and in the ListView
     */
    @Override
    public void addFile(Files temp) {
        database.addFile(temp);
        support.firePropertyChange("FileAdded",null,temp);
    }

    /**
     * A message to add a message to the message feed
     * @param temp A string for the message that will be added
     */
    @Override
    public void addMessage(String temp) {
        System.out.println("MESSAGE IN MODELIMPL: " + temp);
        support.firePropertyChange("newMessageAdded",null,temp);
    }

    /**
     * A method to send the new password to the database
     * @param account An Account that contains the new password
     */
    @Override
    public void editPassword(Account account) {
        database.editPassword(account);
    }

    /**
     * A method to update the e-mail of the user in the database
     * @param account An Account object that contains the new value for an e-mail
     */
    @Override
    public void editEmail(Account account) {
        database.editEmail(account);
    }

    /**
     * A method to create a new user in the database
     * @param account  an account objec that will be added to the database
     */
    @Override
    public void createUser(Account account) {
        database.createUser(account);
        support.firePropertyChange("newUserAdded",null,account);

    }

    /**
     * A getter method for the user array
     * @return an Object
     */
    @Override
    public Object userArray() {
        return database.userArray();
    }

    /**
     * A getter to return the path to the file from the database
     * @param fileName A String that contains the file name of the file that we want the path to
     * @return A String object containing the path of the file
     */
    @Override
    public String getPath(String fileName) {
        return database.getPath(fileName);
    }

    /**
     * A method to delete a user from the database
     * @param username a String fro the username of the user that will be deleted
     */
    @Override
    public void deleteUser(String username) {
        database.deleteUser(username);
        support.firePropertyChange("userDeleted",null,username);
    }

    /**
     * A method to get the e-mail user
     * @return a String object containing the e-mail
     */
    @Override
    public String getTempEmail() {
       return database.getTempEmail();
    }

    @Override
    public void addPropertyChangeListener(String name, PropertyChangeListener listener) {
        support.addPropertyChangeListener(name, listener);
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
     * A method to return if the account is an admin
     * @param temp An Account object that will be compared to the database whether its an ddmin or not
     * @return a boolean value that if true means that the account is an admin
     */
    @Override
    public boolean isAdmin(Account temp)
    {
        System.out.println("MODEL IMPL IS ADMIN : "+temp.isAdmin());
        return database.isAdmin(temp);
    }

    /**
     * A method for deleting a file from the database
     * @param filename A String for the filename of the file that will be deleted
     */
    @Override
    public void deleteFile(String filename) {
        database.deleteFile(filename);
        support.firePropertyChange("fileDeleted",null,filename);
    }
}
