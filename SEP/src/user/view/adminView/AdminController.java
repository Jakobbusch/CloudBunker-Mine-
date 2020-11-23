package user.view.adminView;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import user.view.ViewHandler;
import user.viewModel.adminViewModel.AdminViewModel;

public class AdminController {

    @FXML private TextField search;
    @FXML private ListView<String> userView;
    @FXML private Button deleteUser;
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private TextField email;
    @FXML private Button addUser;
    @FXML private Button editUser;
    private ViewHandler viewHandler;
    private AdminViewModel adminViewModel;
    @FXML private CheckBox isAdmin;


    /**
     * A method responsible for initiating connection between a controller, view model, view handler and necessary controls.
     * @version 1.0
     * @param adminViewModel - and instance of a HomescreenViewModel class.
     * @param viewHandler - and instance of a ViewHandler class.
     * @author Grzegorz Szyszka
     */
    public void init(AdminViewModel adminViewModel, ViewHandler viewHandler)
    {
        this.adminViewModel = adminViewModel;
        this.viewHandler = viewHandler;
        userView.setItems(adminViewModel.getAccounts());
        isAdmin.selectedProperty().bindBidirectional(adminViewModel.getIsAdmin());
        search.textProperty().bindBidirectional(adminViewModel.getSearch());
        username.textProperty().bindBidirectional(adminViewModel.getUsername());
        password.textProperty().bindBidirectional(adminViewModel.getPassword());
        email.textProperty().bindBidirectional(adminViewModel.getEmail());
        FilteredList<String> filteredList = new FilteredList<>(adminViewModel.getAccounts(), p -> true);
        search.textProperty().addListener((observable, newValue, oldValue) -> {
            filteredList.setPredicate(User -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                if(oldValue.equals(newValue)) return true;

                String lowerCaseFilter = newValue.toLowerCase();

                if(User.toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                }
                return false;
            });
        });
        SortedList<String> sortedList = new SortedList<>(filteredList);
        userView.setItems(sortedList);


    }

    /**
     * Method responsible for handling "Delete user" button.
     * @version 1.0
     * @author Grzegorz Szyszka
     */
    public void onDeleteButton(){
        System.out.println("on delete Button"+userView.getSelectionModel().getSelectedItems());
        String temp=userView.getSelectionModel().getSelectedItem();
        String[] temp1=temp.split(": ");
        String[] username=temp1[1].split("\n");// username
        adminViewModel.deleteUser(username[0]);

        userView.setItems(adminViewModel.getAccounts());
    }

    /**
     * Method responsible for handling "Add user" button.
     * @version 1.0
     * @author Grzegorz Szyszka
     */
    public void onAddUserButton(){
        if(isAdmin.isSelected())
        {
            adminViewModel.getAdminAccount();
        }
        else{
            adminViewModel.getAccount();
        }
    }

    /**
     * Method responsible for handling "Edit user" button.
     * @version 1.0
     * @author Grzegorz Szyszka
     */
    public void onEditUserButton(){}

    /**
     * Method responsible for handling "Go back" button.
     * @version 1.0
     * @author Grzegorz Szyszka
     */
    public void onGoBackButton()
    {
        viewHandler.openHomescreen();
    }
}
