package user.viewModel.editProfileViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import network.shared.Account;
import user.model.DataModel;

/**
 * A class Responsible for populating the view and connecting the model and the controller
 */
public class EditProfileViewModel {

    private StringProperty username;
    private StringProperty password;
    private StringProperty email;
    private DataModel model;

    /**
     * A Constructor that injects the model interface and initiates the StringProperties
     * @param model
     */
    public EditProfileViewModel(DataModel model){
        this.model = model;
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
        email = new SimpleStringProperty();
    }

    /**
     * A Method for returning the StringProperty username
     * @return username
     */
    public StringProperty getUsername() {
        String temp1 = model.getAccountInfo();
        String[] file=temp1.split("\t");

        username.setValue(file[0]);
        return username;
    }

    /**
     * A Method for returning the StringProperty password
     * @return password
     */
    public StringProperty getPassword() {
        return password;
    }

    /**
     * A Method for returning the StringProperty email
     * @return email
     */
      public StringProperty getEmail() {
          String temp1 = model.getAccountInfo();
          String[] file=temp1.split("\t");

          email.setValue(file[1]);
        return email;
    }

    /**
     * A method for editing the information about a user
     */
    public void getAccount() {
        System.out.println("getting acc");

        Account temp = new Account(username.getValue(), password.getValue(), email.getValue());
        System.out.println(email.getValue());


        if(password.getValue() != null)
        {
            model.sendPassLogin(temp);
        }
        else if (email.getValue() != null)
        {
            model.sendEmailLogin(temp);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit User");
        alert.setHeaderText(null);
        alert.setContentText("You have successfully edited your password/email");

        alert.showAndWait();


    }
}
