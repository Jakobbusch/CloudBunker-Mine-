package user.viewModel.homescreenViewModel;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import network.shared.Files;
import user.model.DataModel;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomescreenViewModel {


    private StringProperty sendMessage;
    private DataModel dataModel;
    private ObservableList<String> filenames;
    private ObservableList<String> messages;
    private ObservableList<String> timeStamps;
    private ObservableList<String> userName;

    /**
     *  A constructor that is used to initialize the data model
     * @param dataModel A DataModel object that
     */
    public HomescreenViewModel(DataModel dataModel) {
        // filesView = new ListView<>();

        filenames = FXCollections.observableArrayList();
        messages = FXCollections.observableArrayList();
        timeStamps = FXCollections.observableArrayList();
        userName = FXCollections.observableArrayList();
        this.dataModel = dataModel;
        sendMessage = new SimpleStringProperty();
        // search = new SimpleStringProperty();
        dataModel.addPropertyChangeListener("newFileAdded", this::newFileAdded);
        dataModel.addPropertyChangeListener("newMessageAdded",this::newMessageAdded);
        dataModel.addPropertyChangeListener("deleteFile",this::deleteFileFromList);


    }

    /**
     * A method used for adding a file to the file list
     * @param evt A property change event that contains an ArrayList of type Files that contains the file taht will be added to the list
     */
    public void newFileAdded(PropertyChangeEvent evt) {
        Platform.runLater(() -> {
            ArrayList<Files> files =(ArrayList<Files>) evt.getNewValue();

            for (int i = 0; i < ((ArrayList<Files>) evt.getNewValue()).size() ; i++) {
                filenames.add(((ArrayList<Files>) evt.getNewValue()).get(i).toString());
            }

        });
    }

    /**
     * A method used for adding a message recieved from the server to the message feed
     * @param evt a Property change event that contains an ArrayList of type String taht ontains the new message that will be added
     */
    public void newMessageAdded(PropertyChangeEvent evt){
        Platform.runLater(() -> {
            ArrayList<String> messages1 =(ArrayList<String>) evt.getNewValue();
            String temp1 = messages1.get(0);
            String[] file=temp1.split("\t");
                    messages.add(file[0]);
                    timeStamps.add(file[1]);
                    userName.add(file[2]);

           // }

        });
    }

    /**
     * A method used for getting the StringProperty for the search
     * @return A StringProperty object that contains the text of the search field
     */


    public StringProperty getSendMessage() {
        return sendMessage;
    }

    /**
     * A method used for logging out the user that terminates the program
     */
    public void logOut() {
        System.exit(0);
    }

    /**
     * A method that sends a File Object to the data model
     * @param file a File Object that will be sent to the data model
     */
    public void sendFile(File file) {
        dataModel.sendFile(file);
    }
    /**
     * A getter method taht returns a list of names
     * @return an Observable list of type String
     */
    public ObservableList<String> getNames() {
        return filenames;
    }
    /**
     * A getter method that returns a list of  messages
     * @return an Observable list of type String
     */
    public ObservableList<String> getMessages(){
        return messages;
    }

    /**
     * A getter method that returns a list of time stamps
     * @return an Observable list of type String
     */
    public ObservableList<String> getTimeStamps(){
        return timeStamps;
    }

    /**
     * A getter method for  a list of usernames
     * @return an Observable list of type String
     */
    public ObservableList<String> getUserName(){
        return userName;
    }
    public void receiveFile(String filePath){dataModel.recieveFile(filePath);}

    /**
     * A method for sending a download request to the server
     * @param fileName a  String that contains the file path to where the file will be saved
     */
    public void sendDownloadRequest(String fileName)
    {
        System.out.println("HomescreenViewModel: send download Req");
        dataModel.sendDownloadRequest(fileName); // send download request from the client to the server with a filename
    }

    /**
     * A method used for sending a message to the server
     * @param message a String that contains the message that will be sent to the server
     */
    public void sendMessage(String message){

        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm dd/MM");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        String lastUpdate = strDate;

        String output = message.substring(0, 1).toUpperCase() + message.substring(1);
            dataModel.sendMessage(output+ "\t" + lastUpdate);

    }

    /**
     * A method used for deleting a filefrom the list view and updating it
     * @param evt a Property change event that contains the file name of the file that will be deleted
     */
    public void deleteFileFromList(PropertyChangeEvent evt)
    {
        Platform.runLater(() -> {
            String filename=(String)evt.getNewValue();
            Object[] temp= filenames.toArray();
            ArrayList<String> arrayList=new ArrayList<String>();
            for(int i=0;i<temp.length;i++)
            {
                arrayList.add((String)temp[i]);
            }
            for (int i = 0; i <arrayList.size() ; i++) {
                String[] tempFilename=arrayList.get(i).split("\n");
                if(filename.equals(tempFilename[0]))
                {

                    arrayList.remove(i);
                    System.out.println("Homescreen view model: Remove me plz daddy");
                }
            }
            filenames.clear();
            for (int i = 0; i <arrayList.size() ; i++) {
                filenames.add(arrayList.get(i));
            }
        });
    }

    /**
     * A method used for sending to the server what file to delete
     * @param filename a String for the filename that will be deleted
     */
    public void deleteFile(String filename)
    {
        dataModel.deleteFile(filename);
    }

    /**
     * A method to check if the user is an admn
     * @return A boolean that if true means that the user is an admin
     */
    public boolean isAdmin()
    {
        return dataModel.isAdmin();
    }
}
