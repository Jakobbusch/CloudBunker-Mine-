package network.database;

import network.shared.Account;
import network.shared.Files;

import java.sql.*;
import java.util.ArrayList;

public class SQLDatabase implements Database {

    /**
     * @auther Jakob
     * @version 1.0
     */


    private static Connection c = null;
    private ArrayList<Account> accounts = new ArrayList<>();
    private ArrayList<Files> filesArray = new ArrayList<>();
    private String tempEmail;


    /**
     * A method for opening the connection to the database. Every time an operation is called on the database this method should be used
     *
     * @throws SQLException is not handled in this method
     */
    private static void openConn() throws SQLException {
        c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/postgres",
                        "postgres", "cptSmartypants321");

    }

    /**
     * A method to compare the login infromation that the server got to the database
     *
     * @param temp an Account object that should be compared to the database
     * @return a boolean value
     */


    @Override
    public boolean compareLogin(Account temp) {
        try {
            openConn();
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM \"CloudBunker\".users;");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                String username = rs.getString("username");
                String pass = rs.getString("password");
                String mail = rs.getString("email");
                if (temp.getUsername() != null || temp.getPassword() != null) {
                    if (temp.getUsername().equals(username) && temp.getPassword().equals(pass)) {
                        System.out.println("account validated");
                        tempEmail = mail;
                        return true;
                    }
                }

            }
            rs.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * A method that checks if an account is an admin
     * @param temp A Account object that will be checked whether is an admin or not
     * @return A boolean value that if true means that the account is an admin
     */
    @Override
    public boolean isAdmin(Account temp) {
        try {
            openConn();
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM \"CloudBunker\".users;");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                boolean isAdmin = rs.getBoolean("isadmin");
                System.out.println("SQL DATABASE is admin: "+username+" " + isAdmin);
                if (temp.getUsername() != null || temp.getPassword() != null) {
                    if (username.equals(temp.getUsername()) && password.equals(temp.getPassword())) {
                        System.out.println(isAdmin);
                        if (isAdmin) {
                            temp.setAdmin(true);
                            System.out.println("SQL: " + temp.isAdmin());
                            return true;
                        }
                    }
                } else return false;
            }
            rs.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * A method for adding a file to the database
     * @param file A Files object that will be added to the database
     */
    @Override
    public void addFile(Files file) {
        try {
            openConn();
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM \"CloudBunker\".files;");
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println("DATABASE: " + file.getFileName() + file.getDateOfUpload());


            while (rs.next()) {
                System.out.println("file not in database");

                String preparedSql = "INSERT INTO \"CloudBunker\".files (filename,path, uploaddate, owner, format, filesize)" +
                        "SELECT * FROM (SELECT ?, ?,?,?,?,?) AS tmp";

                try {
                    preparedStatement = c.prepareStatement(preparedSql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }

            if (rs.next() == false) {
                String preparedSql = "INSERT INTO \"CloudBunker\".files (filename,path, uploaddate, owner, format, filesize)" +
                        "SELECT * FROM (SELECT ?, ?,?,?,?,?) AS tmp";
                try {
                    preparedStatement = c.prepareStatement(preparedSql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("File Now in database!");

            addFileTODataBase(preparedStatement, file.getFileName(), file.getPath(), file.getDateOfUpload(), file.getOwner(), file.getFormat(), file.getFileSize());

            rs.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     * A method to delete a file from the database
     * @param filename A String object that contains the filename of the file that will be deleted
     */
    @Override
    public void deleteFile(String filename) {
        try {
            openConn();
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM \"CloudBunker\".files;");
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println(filename);


            String praparesql = "DELETE FROM \"CloudBunker\".files WHERE filename='" + filename + "';";


            try {
                PreparedStatement ps = c.prepareStatement(praparesql);
                rs = ps.executeQuery();
                ps.close();
            } catch (SQLException e) {
                System.out.println("File deleted: " + filename);
                ;
            }


            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that adds a file to the database
     * @param preparedStatement     A prepared statement object that will add the information to the database
     * @param filename              A String object that will be added to the database as filename
     * @param path                  A String object that will be added to the database as a path to the file
     * @param uploaddate            A String object that will be added to the database as an upload date
     * @param owner                 A String object that will be added to the database as the owner of the file
     * @param format                A String object that will be added to the database containing the format of the file
     * @param filesize              A String object that will be added to the database containing the filesize of the file
     */
    private static void addFileTODataBase(PreparedStatement preparedStatement, String filename, String path, String uploaddate, String owner, String format, String filesize) {
        try {
            preparedStatement.setString(1, filename);
            preparedStatement.setString(2, path);
            preparedStatement.setString(3, uploaddate);
            preparedStatement.setString(4, owner);
            preparedStatement.setString(5, format);
            preparedStatement.setString(6, filesize);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that gets all the files from the database and returns them as an ArrayList
     * @return An ArrayList containing all the files from the database
     */
    @Override
    public ArrayList<Files> fileArray() {

        try {
            openConn();
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM \"CloudBunker\".files;");
            ResultSet rs = preparedStatement.executeQuery();
            filesArray.clear();
            while (rs.next()) {

                String filename = rs.getString("filename");
                String path = rs.getString("path");
                String uploaddate = rs.getString("uploaddate");
                String owner = rs.getString("owner");
                String format = rs.getString("format");
                String filesize = rs.getString("filesize");

                filesArray.add(new Files(filename, null, uploaddate, owner, path, format, filesize));


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return filesArray;
    }

    /**
     * A getter for getting the path of the file
     * @param fileName A String that contains the filename of the file that the path is needed
     * @return A String that contains the path to the file
     */
    @Override
    public String getPath(String fileName) {
        try {
            openConn();
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM \"CloudBunker\".files;");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String tempFileName = rs.getString("filename");
                System.out.println("fileName logindatabase: " + tempFileName);
                String path = rs.getString("path");
                if (fileName.equals(tempFileName)) {
                    System.out.println("path: " + path);
                    return path;
                }

            }
            rs.close();
            preparedStatement.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * A method to return the tempEmail
     * @return A String object
     */
    @Override
    public String getTempEmail() {
        return tempEmail;
    }

    /**
     * A method to delete a user from the database
     * @param username A String object containing the username of the user that will be deleted
     */
    @Override
    public void deleteUser(String username) {
        try {
            openConn();
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM \"CloudBunker\".users;");
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println(username);

            String praparesql = "DELETE FROM \"CloudBunker\".users WHERE username='" + username + "';";

            try {
                PreparedStatement ps = c.prepareStatement(praparesql);
                rs = ps.executeQuery();
                ps.close();
            } catch (SQLException e) {
                System.out.println("User deleted: " + username);
                ;
            }


            rs.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A setter for a new password
     * @param acc An Account object that will set the password to the same account on the database
     */
    @Override
    public void editPassword(Account acc) {

        try {
            openConn();
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM \"CloudBunker\".users;");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                String name = rs.getString("username");

                String q1 = "UPDATE  \"CloudBunker\".users set password = '" + acc.getPassword() +
                        "' WHERE username = '" + name + "';";


                PreparedStatement ps = c.prepareStatement(q1);

                int x = ps.executeUpdate();

                if (x > 0)
                    System.out.println("Password Successfully Updated");
                else
                    System.out.println("ERROR OCCURED ");

            }

            preparedStatement.close();
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method for setting a new e-mail for the user
     * @param acc An Account object that will give the new e-mail
     */
    @Override
    public void editEmail(Account acc) {
        try {
            openConn();
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM \"CloudBunker\".users;");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                String name = rs.getString("username");

                String q1 = "UPDATE  \"CloudBunker\".users set email = '" + acc.getEmail() +
                        "' WHERE username = '" + name + "';";

                System.out.println(q1);
                PreparedStatement ps = c.prepareStatement(q1);

                int x = ps.executeUpdate();

                if (x > 0)
                    System.out.println("Email Successfully Updated");
                else
                    System.out.println("ERROR OCCURED ");

            }
            rs.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * A method to create a new user in the database
     * @param acc An Account object that will be added to the database
     */
    @Override
    public void createUser(Account acc) {

        try {
            openConn();
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM \"CloudBunker\".users;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {


                String preparedSql = "INSERT INTO \"CloudBunker\".users (username, password,email,isadmin) "
                        + "SELECT * FROM (SELECT ?, ?,?,?) AS tmp ";


                try {
                    preparedStatement = c.prepareStatement(preparedSql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                addUserToDatabase(preparedStatement, acc.getUsername(), acc.getPassword(), acc.getEmail(), acc.isAdmin());

                resultSet.close();
                preparedStatement.close();

            }

        } catch (SQLException e) {
            System.out.println("User Added ");
        }

    }

    /**
     * A method to add a user to the database
     * @param preparedStatement A prepared statement object that will insert the data into the database
     * @param username          A String containing the value for the username
     * @param password          A String containing the value for the pasword
     * @param email             A String containing the value for the e-mail
     * @param isadmin           A Boolean value that indicates if the account is an admin or not
     */
    private static void addUserToDatabase(PreparedStatement preparedStatement, String username, String password, String email, boolean isadmin) {
        try {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setBoolean(4, isadmin);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method to get all the users from the database
     * @return An ArrayList object that contains all the accounts in the database
     */
    @Override
    public ArrayList<Account> userArray() {

        try {
            openConn();
            PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM \"CloudBunker\".users;");
            ResultSet rs = preparedStatement.executeQuery();
            accounts.clear();
            while (rs.next()) {

                String username = rs.getString("username");
                String pass = rs.getString("password");
                String email = rs.getString("email");
                boolean isAdmin = rs.getBoolean("isadmin");
                Account temp = new Account(username, pass, email);
                temp.setAdmin(isAdmin);
                accounts.add(temp);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("WAS THIS IT ?");
        return accounts;
    }


}
