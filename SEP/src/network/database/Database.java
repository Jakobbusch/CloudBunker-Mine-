package network.database;

import network.shared.Account;
import network.shared.Files;

import java.util.ArrayList;

public interface Database {
    boolean compareLogin(Account temp);
    void addFile(Files file);
    void editPassword(Account acc);
    void editEmail(Account acc);
    void createUser(Account acc);
    ArrayList<Account> userArray();
    ArrayList<Files> fileArray();
    String getPath(String fileName);
    void deleteUser(String username);
    String getTempEmail();
    boolean isAdmin(Account temp);
    void deleteFile(String filename);
}
