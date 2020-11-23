package user.viewModel.loginViewModel;


import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import network.shared.Account;
import user.model.DataModel;

import java.beans.PropertyChangeEvent;

public class LoginViewModel {

    private StringProperty username;
    private StringProperty password;
    private SimpleStringProperty loginResult;
    private DataModel model;
    private StringProperty email;

    /**
     * A constructor used for initializing all the private field variables in the class
     * @param model A DataModel Object
     */
    public LoginViewModel(DataModel model) {
        this.model = model;
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
        email = new SimpleStringProperty();
        loginResult = new SimpleStringProperty();
        model.addPropertyChangeListener("LoginResult", this::getacInfo);
    }

    /**
     * A  A  method that returns the StringProperty of the username field
     * @return A StringProperty Object
     */
    public StringProperty getUsername() {
        return username;
    }

    /**
     * A  A  method that returns the StringProperty of the password field
     * @return A StringProperty Object
     */
    public StringProperty getPassword() {
        return password;
    }

    /**
     * A method used for sendng the account to the dataModel
     */
    public void getAccount() {
        System.out.println("getting acc");

        Account temp = new Account(username.getValue(), password.getValue(),email.getValue());
        model.sendLogin(temp);
    }

    /**
     * A method used for  checking the result of the login
     * @param evt a Property change event that contains a boolean that if true allows the user to continue to the next screen
     */
    public void getacInfo(PropertyChangeEvent evt) {
        boolean result = (boolean) evt.getNewValue();
        Platform.runLater(() -> {
            loginResult.set(result ? "OK" : "Failed");
        });
    }

    /**
     * A method used for getting the String property for the result from the login
     * @return  a StringProperty Object
     */
    public SimpleStringProperty loginResultProperty() {
        return loginResult;
    }


}
