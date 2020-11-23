package user.view.homescreenView;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import user.view.ViewHandler;
import user.viewModel.homescreenViewModel.HomescreenViewModel;

import java.io.File;
import java.util.List;

/**
 * A class responsible for homescreen controls.
 * version 1.0
 * @
 */

public class HomescreenController
{

    @FXML private MenuItem adminPanel;
    @FXML private ListView<String> fileView;
    @FXML private ListView<String> feed;
    @FXML private ListView<String> timeStampFeed;
    @FXML private ListView<String> userNameFeed;
    @FXML private TextField sendMessage;
    @FXML private TextField search;
    private HomescreenViewModel homescreenViewModel;
    private ViewHandler viewHandler;



    /**
     * A method responsible for initiating connection between a controller, view model, view handler and necessary controls.
     * @version 2.0
     * @param vm - and instance of a HomescreenViewModel class.
     * @param viewHandler - and instance of a ViewHandler class.
     * @author Grzegorz Szyszka
     */
    public void init(HomescreenViewModel vm, ViewHandler viewHandler)
    {

        homescreenViewModel = vm;
        this.viewHandler = viewHandler;
        System.out.println(homescreenViewModel.isAdmin());
        if(homescreenViewModel.isAdmin() == true)
        {
            adminPanel.setDisable(false);
        }
        else if(homescreenViewModel.isAdmin()!=true)
        {
            adminPanel.setDisable(true);
        }

        sendMessage.textProperty().bindBidirectional(vm.getSendMessage());
        fileView.setItems(vm.getNames());
        search.setPromptText("Search for files...");
        FilteredList<String> filteredList = new FilteredList<>(vm.getNames(), p -> true);
        search.textProperty().addListener((observable, newValue, oldValue) -> {
            filteredList.setPredicate(file -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                if(oldValue.equals(newValue)) return true;

                String lowerCaseFilter = newValue.toLowerCase();

                if(file.toLowerCase().contains(lowerCaseFilter))
                {
                    return true;
                }
                return false;
            });
        });
        SortedList<String> sortedList = new SortedList<>(filteredList);
        fileView.setItems(sortedList);

            feed.setItems(vm.getMessages());
            timeStampFeed.setItems(vm.getTimeStamps());
            userNameFeed.setItems(vm.getUserName());

        // Enables send message by pressing enter
        sendMessage.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    onSendButton();
                }
            }
        });

    }

    /**
     * Method responsible for handling "Upload" button.
     * @version 1.0
     * @author Grzegorz Szyszka / Mathias Hansen
     */
    public void onUploadButton()
    {
        // open fileChooser in viewHandler
        File temp=viewHandler.openFileChooser();
        if(temp!= null)
        {
            homescreenViewModel.sendFile(temp);
            homescreenViewModel.sendMessage("Uploaded "+temp.getName());

            List<String> temp1 = fileView.getItems();
            int index = temp1.size();
            fileView.scrollTo(index);


        }
        else
        {
            temp=viewHandler.openFileChooser();
        }

    }

    public void updateMessages(){

    }

    /**
     * Method responsible for handling "Download" button.
     * @version 1.0
     * @author Grzegorz Szyszka
     */
    public void onDownloadButton()
    {
        File temp=viewHandler.openDirectoryChooser();
        homescreenViewModel.receiveFile(temp.getPath());
        // take a fileName form the list view


        System.out.println(temp.getPath());
        String temp1 =fileView.getSelectionModel().getSelectedItem();
        String[] file=temp1.split("\n");

        homescreenViewModel.sendDownloadRequest(file[0]);//TODO change this after database is done

    }
    /**
     * Method responsible for handling "Delete" button.
     * @version 1.0
     * @author Grzegorz Szyszka
     */
    public void onDeleteButton()
    {
        String temp = (String)  fileView.getSelectionModel().getSelectedItem();
        String[] filename=temp.split("\n");
        homescreenViewModel.deleteFile(filename[0]);
    }
    /**
     * Method responsible for handling "Send" button.
     * @version 1.0
     * @author Grzegorz Szyszka
     */
    public void onSendButton(){
        String message = sendMessage.getText();
        try {
            if (message.length() > 0) {
                homescreenViewModel.sendMessage(message);
                sendMessage.clear();
                List<String> temp = feed.getItems();
                int index = temp.size();
                feed.scrollTo(index);
                int index1 = temp.size();
                userNameFeed.scrollTo(index1);
                int index2 = temp.size();
                timeStampFeed.scrollTo(index2);
            }
        }catch (NullPointerException e){
            System.out.println("NO MESSAGE");
        }
    }
    /**
     * Method responsible for handling "Edit profile" button.
     * @version 1.0
     * @author Grzegorz Szyszka
     */
    public void onEditProfileItem(){
        viewHandler.openEditProfile();
    }
    /**
     * Method responsible for handling "Log out and exit" menu item from the profile menu.
     * @version 1.0
     * @author Grzegorz Szyszka
     */
    public void onLogOutItem()
    {
        homescreenViewModel.logOut();
    }
    /**
     * Method responsible for handling "Admin panel" menu item from the profile menu.
     * @version 1.0
     * @author Grzegorz Szyszka
     */
    public void onAdminPanelItem()
    {
        viewHandler.openAdmin();
    }

}
