package user.view;


import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import user.view.adminView.AdminController;
import user.view.editProfileView.EditProfileController;
import user.view.homescreenView.HomescreenController;
import user.view.logInView.LoginController;
import user.viewModel.ViewModelFactory;

import java.io.File;
import java.io.IOException;

public class ViewHandler {
private ViewModelFactory vmf;
private Stage stage;
public ViewHandler(Stage stage,ViewModelFactory vmf)
{
    this.stage=stage;
    this.vmf=vmf;
}

    /**
     * A method used for starting the login view
     */
public void start(){openLogin();}
    /**
     * A method used for starting the home screen of the program
     */
public void startHomeScreen(){
    openHomescreen();
}
    /**
     * A method used for starting the admin window
     */
public void startAdmin(){openAdmin();}

    /**
     * A method for opening the log in screen
     */
    public void openLogin()
{
    FXMLLoader loader=new FXMLLoader();

    loader.setLocation(getClass().getResource("logInView/LoginView.fxml"));
    Parent root=null;
    try{
        root=loader.load();
    } catch (IOException e) {
        e.printStackTrace();
    }
    LoginController view=loader.getController();
    view.init(vmf.getLoginViewModel(),this);
    stage.setTitle("Log In");
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.getIcons().add(new Image("images/VIaLOGO.PNG"));
    stage.show();
    stage.setResizable(false);

}

    /**
     * A method used for oppening the Home screen of the program
     */

    public void openHomescreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("homescreenView/HomescreenView.fxml"));
            Parent root = null;
            root = loader.load();
            HomescreenController view = loader.getController();
            view.init(vmf.getHomescreenViewModel(), this);
            stage.setTitle("VIA Cloud Bunker");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method used for opening the Admin window
     */
    public void openAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("adminView/AdminView.fxml"));
            Parent root = null;
            root = loader.load();
            AdminController view = loader.getController();
            view.init(vmf.getAdminViewModel(), this);
            stage.setTitle("Admin panel");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method used for opening the Edit profile view where the user can edit their account information
     */
    public void openEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("editProfileView/EditProfileView.fxml"));
            Parent root = null;
            root = loader.load();
            EditProfileController view = loader.getController();
            view.init(vmf.getEditProfileViewModel(), this);
            stage.setTitle("Edit profile");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method for oppening the file chooser that returns the selected file
     * @return a File object that is selected by the user
     */
    public File openFileChooser()
    {
        FileChooser fileChooser=new FileChooser();
        Stage stageFileChooser=new Stage();
        fileChooser.setTitle("Chose a File for Upload");
        FileChooser.ExtensionFilter extensionFilter= new FileChooser.ExtensionFilter("Files",
                "*.txt","*.docx","*.pdf","*.xlsx","*.7zip","*.rar","*.pptx","*.jpeg","*.png","*.mp4", "*.jpg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file=fileChooser.showOpenDialog(stageFileChooser);
        return file;
    }
    public File openDirectoryChooser()
    {
        DirectoryChooser directoryChooser=new DirectoryChooser();
        Stage stageDirectoryChooser=new Stage();
        directoryChooser.setTitle("Select Download Location");
        File file= directoryChooser.showDialog(stageDirectoryChooser);
        return file;
    }
}
