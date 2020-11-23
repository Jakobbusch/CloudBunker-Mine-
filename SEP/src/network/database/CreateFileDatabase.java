package network.database;

import java.sql.*;

    public class CreateFileDatabase {
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

            sql = "CREATE TABLE IF NOT EXISTS \"CloudBunker\".files ("
                    + "  filename varchar(150) NOT NULL, "
                    + "  path varchar(150) NOT NULL, "
                    + "  uploaddate varchar (150) NOT NULL, "
                    + "  owner varchar (150) NOT NULL, "
                    + "  format varchar (150) NOT NULL, "
                    + "  filesize varchar(150) NOT NULL" + ");";

            try {
                Statement statement = connection.createStatement();
                statement.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }


            String preparedSql = "INSERT INTO \"CloudBunker\".files (filename, path, uploaddate,owner,format,filesize) "
                    + "SELECT * FROM (SELECT ?, ?,?,?,?,?) AS tmp ";

            PreparedStatement preparedStatement = null;

            try {

                preparedStatement = connection.prepareStatement(preparedSql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            addFileTODataBase(preparedStatement, "FILENAME","PATH","DATE","OWNER","FORMAT","SIZE");


            try{

            sql =   "set search_path=\"CloudBunker\"; "+
                    "CREATE TABLE IF NOT EXISTS \"CloudBunker\".log_files ("
                    + "  log_id serial,"
                    + "  log_time timestamp,"
                    + "  filename_now varchar(150), "
                    + "  path_now varchar(150), "
                    + "  uploaddate varchar (150), "
                    + "  owner varchar (150), "
                    + "  format varchar (150), "
                    + "  filesize_now varchar(150),"
                    + "  filename_before varchar(150), "
                    + "  path_before varchar(150), "
                    + "  filesize_before varchar(150),"
                    + "  CONSTRAINT log_pkey PRIMARY key (log_id)"
                    + ");";


                Statement statement = connection.createStatement();
                statement.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //              Create log trigger
            try{
            sql =   "set search_path=\"CloudBunker\"; "+
                    "CREATE OR REPLACE FUNCTION log_uploadFile() RETURNS TRIGGER as $$ " +
                    "begin if(tg_op ='INSERT') then "+
                    "insert into \"CloudBunker\".log_files (log_time,filename_now,path_now,uploaddate,owner,format,filesize_now) "+
                    "values (now(),new.filename,new.path,new.uploaddate,new.owner,new.format,new.filesize);"+
                    "return new;"+
                    "end if;"+
                    "if(tg_op='DELETE') then " +
                    "insert into \"CloudBunker\".log_files (log_time,uploaddate,owner,format,filename_before,path_before,filesize_before) " +
                    "values (now(),old.uploaddate,old.owner,old.format,old.filename,old.path,old.filesize); " +
                    "return new;"+
                    "end if;"+
                    "return null;"+
                    "end;"+
                    "$$ language plpgsql;" +


                    // creating triggers
                    "create trigger log_insert "+
                    "after insert on \"CloudBunker\".files for each row "+
                    "execute procedure log_uploadFile();"+


                    "create trigger log_delete "+
                    "after delete on \"CloudBunker\".files for each row "+
                    "execute procedure log_uploadFile();";

                Statement statement = connection.createStatement();
                statement.execute(sql);

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

        /**
         *  A method for adding a File to the database
         * @param preparedStatement  a prepared statement object used for the sql querry
         * @param filename           a string object that will be added to the database
         * @param path               a string object that will be added to the database
         * @param uploaddate         a string object that will be added to the database
         * @param owner              a string object that will be added to the database
         * @param format             a string object that will be added to the database
         * @param filesize           a string object that will be added to the database
         */
        private static void addFileTODataBase(PreparedStatement preparedStatement, String filename, String path, String uploaddate, String owner, String format, String filesize){
            try{
                preparedStatement.setString(1,filename);
                preparedStatement.setString(2,path);
                preparedStatement.setString(3,uploaddate);
                preparedStatement.setString(4,owner);
                preparedStatement.setString(5,format);
                preparedStatement.setString(6,filesize);
                preparedStatement.executeUpdate();

            }catch (SQLException e){
                e.printStackTrace();
            }
        }



    }

