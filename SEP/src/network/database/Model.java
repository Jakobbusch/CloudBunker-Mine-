package network.database;

import network.shared.Account;
import network.shared.Files;
import network.shared.PropertyChangeSubject;

public interface Model extends PropertyChangeSubject {


    boolean compareLogin(Account temp);

    Object fileArray();

    void addFile(Files temp);
    void addMessage(String temp);
    void editPassword(Account account);
    void editEmail(Account account);
    void createUser(Account account);
    Object userArray();
    String getPath(String fileName);
    void deleteUser(String username);
    String getTempEmail();
    boolean isAdmin(Account temp);
    void deleteFile(String filename);

}
