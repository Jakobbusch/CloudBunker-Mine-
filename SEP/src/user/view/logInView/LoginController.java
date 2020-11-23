package user.view.logInView;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import user.view.ViewHandler;
import user.viewModel.loginViewModel.LoginViewModel;


public class LoginController {

    /**
     * A class controlling the LoginView GUI.
     * Variables:
     * username - variable of type TextField.
     * password - variable of type TextField.
     * viewModel - variable of type LoginViewModel.
     * ldb - variable of type LoginDatabase.
     */
    @FXML
    TextField username;
    @FXML
    TextField password;

    LoginViewModel viewModel;
    ViewHandler vh;


    /**
     * @param vm An instance of the LoginViewModel.
     *           Method which binds a ViewModel with the Username and Password text fields.
     * @param vh An instance of ViewHandler
     */
    public void init(LoginViewModel vm, ViewHandler vh) {
        viewModel = vm;
        this.vh = vh;
        username.textProperty().bindBidirectional(vm.getUsername());
        password.textProperty().bindBidirectional(vm.getPassword());
        viewModel.loginResultProperty().addListener((observable, oldValue, newValue) -> loginResult(newValue));

        password.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    onLoginButton();
                }
            }
        });
    }

    /**
     * A method that either opens the home screen of the program or displays an error message to the user
     * @param newValue
     */
    private void loginResult(String newValue) {
        // TODO fill in nmethod
        System.out.println("Her til?");
        if ("OK".equals(newValue)) {
            vh.startHomeScreen();
        } else {
            Image img = new Image("images/TROELS.png");
            System.out.println("DET VIRKER IKKE ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("LOGIN INCORRECT");
            alert.setHeaderText("Look, TRoels has Information for you");
            alert.setContentText("Username or password incorrect");
            alert.getDialogPane().setGraphic(new ImageView(img));





            alert.showAndWait();

        }
    }
    /**
     * A method that handles the functionality of the login button
     */
    public void onLoginButton() {
        System.out.println("hello");

        viewModel.getAccount();

    }


}
