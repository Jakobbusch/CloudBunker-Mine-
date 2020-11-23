package user.viewModel.adminViewModel;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import network.shared.Account;
import user.model.DataModel;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

public class AdminViewModel {

    private StringProperty search;
    private StringProperty username;
    private StringProperty password;
    private StringProperty email;
    private DataModel dataModel;
    private ObservableList<String> accountNames;
    private BooleanProperty isAdmin;


    /**
     * A construtor that initializes all the private fields in the class
     * @param dataModel A DataModel Object
     */
    public AdminViewModel(DataModel dataModel)
    {
        accountNames = FXCollections.observableArrayList();
        this.dataModel = dataModel;
        search = new SimpleStringProperty();
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
        email = new SimpleStringProperty();
        isAdmin=new SimpleBooleanProperty();
        dataModel.addPropertyChangeListener("newUserAdded",this::newUserAdded);
        dataModel.addPropertyChangeListener("deleteUserFromList",this::deleteUserFromList);
    }

    /**
     * A method for returning the StringPtoperty of the search field
     * @return A StringProperty object
     */
    public StringProperty getSearch()
    {
        return search;
    }

    /**
     * A method that returns the StringProperty of the usermane field
     * @return a String property Object
     */
    public StringProperty getUsername(){return username;}

    /**
     * A  method that returns the StringProperty of the password field
     * @return A StringProperty object
     */
    public StringProperty getPassword(){return password;}

    /**
     * A  method that returns the StringProperty of the e-mail field
     * @return A StringProperty object
     */
    public StringProperty getEmail() {return email;}

    public void newUserAdded(PropertyChangeEvent evt){
        Platform.runLater(() -> {
            ArrayList<Account> accounts =(ArrayList<Account>) evt.getNewValue();

            for (int i = 0; i < ((ArrayList<Account>) evt.getNewValue()).size() ; i++) {
                accountNames.add(((ArrayList<Account>) evt.getNewValue()).get(i).toString());
            }
    });
    }

    /**
     * A method used for deleting a user fom the list View
     * @param evt A Property change event containing a String for the username of the user tha will be deleted
     */
    public void deleteUserFromList(PropertyChangeEvent evt)
    {
        Platform.runLater(() -> {
            Object [] temp=accountNames.toArray();

            ArrayList<String> temp1 = new ArrayList<String>();
            String username=(String)evt.getNewValue();
            for (int i = 0; i <temp.length ; i++) {
                temp1.add((String)temp[i]);
            }
            for(int i = 0 ;i< temp1.size();i++)
            {    String[] temp2=temp1.get(i).split(": ");
                String[] usernameFromList=temp2[1].split("\n");// username
                if(usernameFromList[0].equals(username))
                {
                    temp1.remove(i);
                }
            }
            accountNames.clear();
            for (int i = 0; i <temp1.size() ; i++) {

                accountNames.add(temp1.get(i).toString());
            }

        });
    }

    /**
     * A getter to get an Observable list of all the account
     * @return an Observable list of type String
     */
    public ObservableList<String> getAccounts(){
        return accountNames;}


    /**]
     * A method used for creating a new user
     */
    public void getAccount() {
        System.out.println("getting acc");

        Account temp = new Account(username.getValue(), password.getValue(), email.getValue());


        if(username.getValue() != null && password.getValue() != null && email.getValue() != null)
        {
            dataModel.sendAcc(temp);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Create New User");
            alert.setHeaderText(null);
            alert.setContentText("You have successfully created a new user");

            alert.showAndWait();
        }
        if (username.getValue() == null || password.getValue() == null || email.getValue() == null){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Create New User");
            alert.setHeaderText(null);
            alert.setContentText("Missing password,username or email");

            alert.showAndWait();
        }
    }

    /**
     * A method used for creating a new admin user
     */
    public void getAdminAccount()
    {
        System.out.println("getting admin acc");

        Account temp = new Account(username.getValue(), password.getValue(), email.getValue());
        temp.setAdmin(isAdmin.getValue());

        if(username.getValue() != null && password.getValue() != null && email.getValue() != null && isAdmin.getValue()!=null)
        {
            dataModel.sendAcc(temp);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Create New Admin User");
            alert.setHeaderText(null);
            alert.setContentText("You have successfully created a new admin user");

            alert.showAndWait();
        }
        // if some of the fields are null send a alert 
        if (username.getValue() == null || password.getValue() == null || email.getValue() == null || isAdmin.getValue()==null){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Create New Admin User");
            alert.setHeaderText(null);
            alert.setContentText("Missing password,username, email or admin. ");

            alert.showAndWait();
        }
    }

    /**
     * A method that deletes an user
     * @param username
     */
    public void deleteUser(String username)
    {
     dataModel.deleteUser(username);
    }

    /**
     * A method for getting if the user is an admin
     * @return A Boolean Property taht if true means that the user is an admin
     */
    public BooleanProperty getIsAdmin()
    {
        return isAdmin;
    }


}
