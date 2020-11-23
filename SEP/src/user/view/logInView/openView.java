package user.view.logInView;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class openView {
    public static void main(String[] args) {
       Stage stage=new Stage();
        FXMLLoader loader=new FXMLLoader();

        loader.setLocation(openView.class.getResource("logInView/LoginView.fxml"));
        Parent root=null;
        try{
            root=loader.load();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        LoginController view=loader.getController();

        stage.setTitle("Log In");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
