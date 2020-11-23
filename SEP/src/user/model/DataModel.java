package user.model;

import network.client.Client;
import network.shared.Account;
import network.shared.PropertyChangeSubject;

import java.io.File;

public interface DataModel extends PropertyChangeSubject {

    public void setClient(Client client);
    public void sendLogin(Account acc);
    public void sendFile(File file);
    public void recieveFile(String filePath);
    public void sendDownloadRequest(String fileName);
    public void sendPassLogin(Account temp);
    public void sendEmailLogin(Account temp);
    public void sendAcc(Account temp);
    public void sendMessage(String message);
    public void deleteUser(String username);
    public String getAccountInfo();
    public void deleteFile(String filename);
    public boolean isAdmin();



}



