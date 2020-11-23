import javafx.application.Application;
import javafx.stage.Stage;
import network.client.Client;
import network.client.SocketClient;
import user.model.ModelFactory;
import user.view.ViewHandler;
import user.viewModel.ViewModelFactory;

public class SApplication extends Application {
  public void start(Stage stage){
      Client client = new SocketClient();
      ModelFactory mf = new ModelFactory(client);
      ViewModelFactory vmf = new ViewModelFactory(mf);
      ViewHandler vh = new ViewHandler(stage, vmf);
       vh.start();
  }

}
