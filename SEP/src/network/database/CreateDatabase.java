package network.database;


import java.sql.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * A class used for initializing the database. It should be started once before operating the program
 * @author  Jakob
 */
public class CreateDatabase {

    public static void main(String[] args) {
        String driver = "org.postgresql.Driver";

        String url = "jdbc:postgresql://localhost:5432/postgres";

        String user = "postgres";


        // Change the pw String to your password and run it one time only!
        String pw = "cptSmartypants321";

        Connection connection = null;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(url, user, pw);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        String sql = "CREATE SCHEMA IF NOT EXISTS \"CloudBunker\";";
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "CREATE TABLE IF NOT EXISTS \"CloudBunker\".users ("
                + "  username varchar(50) NOT NULL, "
                + "  password varchar(50) NOT NULL, "
                + "  email varchar(50) NOT NULL, "
                + "  isadmin boolean "+ ");";

        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String preparedSql = "INSERT INTO \"CloudBunker\".users (username, password,email, isadmin) "
                + "SELECT * FROM (SELECT ?, ?,?,?) AS tmp ";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(preparedSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        addUserToDatabase(preparedStatement, "test", "test", "testemail3", true);


        try {
            String querysql = "SELECT  username, password,email FROM \"CloudBunker\".users ORDER BY username;";
            PreparedStatement queryUserStatement = connection.prepareStatement(querysql);
            ResultSet resultSet = queryUserStatement.executeQuery();

            ArrayList<Object[]> results = new ArrayList<Object[]>();

            while (resultSet.next()) {
                Object[] row = new Object[resultSet.getMetaData().getColumnCount()];

                for (int i = 0; i < row.length; i++) {
                    row[i] = resultSet.getObject(i + 1);
                }
                results.add(row);
            }

            resultSet.close();
            queryUserStatement.close();

            for (int i = 0; i < results.size(); i++) {
                Object[] row = results.get(i);
                String username = row[0].toString();
                String password = row[1].toString();
                String email = row[2].toString();

                System.out.println(username + " " + password + " " + email);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     * A method for adding a user to the database
     *
     * @param preparedStatement a prepared statement object used for the sql querry
     * @param username          a String object to be added for the username
     * @param password          a String object to be added for the password
     * @param email             a String object to be added for the email
     */
    private static void addUserToDatabase(PreparedStatement preparedStatement, String username, String password, String email, boolean isadmin) {
        try {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setBoolean(4,isadmin);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

