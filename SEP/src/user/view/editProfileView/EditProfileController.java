package user.view.editProfileView;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import user.view.ViewHandler;
import user.viewModel.editProfileViewModel.EditProfileViewModel;

/**
 * A Class responsible for controlling the edit view.
 */
public class EditProfileController {


    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private TextField email;
    private ViewHandler viewHandler;
    private EditProfileViewModel editProfileViewModel;


    /**
     * An init method initiating connection between controller, viewmodel and viewhandler and populating the fields
     * @param vm is the Viewmodel of the edit view
     * @param vh is the viewhandler that handles all the views
     */
    public void init(EditProfileViewModel vm, ViewHandler vh){
        viewHandler = vh;
        editProfileViewModel = vm;
        username.textProperty().bindBidirectional(vm.getUsername());
        password.clear();
        email.textProperty().bindBidirectional(vm.getEmail());


    }

    /**
     * A Method for saving credentials
     */
    public void onSaveButton(){
        editProfileViewModel.getAccount();
        viewHandler.startHomeScreen();
    }

    /**
     * A method for heading back to the HomeScreenView
     */
    public void onBackButton(){
        viewHandler.startHomeScreen();
    }

}
