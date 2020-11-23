package network.client;


import network.shared.Account;
import network.shared.PropertyChangeSubject;

import java.io.File;

public interface Client extends PropertyChangeSubject {


    public void setAccount(Account acc);
    public void uploadFile(File fileName);
    public void recieveFile(String filePath);
    public void sendDownloadRequest(String fileName);
    public void editPass(Account acc);
    public void editEmail(Account acc);
    public void createAcc(Account acc);
    public void sendMessage(String message);
    public void deleteUser(String username);
    public String getAccountInfo();
    public void deleteFile(String filename);
    public boolean isAdmin();






}
