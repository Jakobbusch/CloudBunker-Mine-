package network.shared;

import java.io.Serializable;

public class Files implements Serializable {
    private String fileName;
    private byte[] mybytearray;
    private String dateOfUpload;
    private String owner;
    private String path;
    private String format;
    private String fileSize;

    /**
     * A constructor for the Files class that takes a String object and a byte array
     * @param fileName A string object that denotes the name of the file that we are sharing
     * @param mybytearray a byte array that is the file that we are sharing
     */
    public Files(String fileName,byte[] mybytearray)
    {this.fileName=fileName;
        this.mybytearray=mybytearray;
    }

    /**
     * An overloaded constructor for the Files class
     * @param fileName           A String for the filename
     * @param mybytearray        A byte[] that contains the file
     * @param uploaddate         A String for the upload date
     * @param owner              A String for the owner of the file
     * @param path               A String for the file path
     * @param format             A String for the file format
     * @param fileSize           A String for the filesize
     */
    public Files(String fileName,byte[] mybytearray, String uploaddate, String owner, String path,String format, String fileSize)
    {
        dateOfUpload=uploaddate;
        this.fileName=fileName;
        this.mybytearray=mybytearray;
        this.owner = owner;
        this.path = path;
        this.format = format;
        this.fileSize = fileSize;
    }



    /**
     * A getter for the file name
     * @return a String object for the file name
     */
    public String getFileName() {

        return fileName;
    }

    /**
     * A getter for the date of upload
     * @return A String containing the date of upload
     */
    public String getDateOfUpload(){
        return dateOfUpload;
    }

    /**
     * A getter for the owner
     * @return A String containing the owner of the file
     */
    public String getOwner() {
        return owner;
    }

    /**
     * A getter for the path of the file
     * @return A String containing the file path
     */
    public String getPath() {
        return path;
    }

    /**
     * A getter for the format of the file
     * @return A String containing the format of the file
     */
    public String getFormat() {
        return format;
    }

    /**
     * A getter for the file size
     * @return A String containing the file size
     */
    public String getFileSize() {
        return fileSize;
    }


    /**
     * A setter for the file name that takes a String object as an argument
     * @param fileName A string object for the new file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * A getter for the byte array
     * @return a byte array object
     */
    public byte[] getMybytearray() {
        return mybytearray;
    }

    /**
     * A setter for the byte array
     * @param mybytearray a byte array object that denotes a new file
     */
    public void setMybytearray(byte[] mybytearray) {
        this.mybytearray = mybytearray;
    }

    /**
     * A setter for the file path
     * @param path A String that will be the new value for the file path
     */
    public void setPath(String path) {
        this.path = path;
    }


    public String toString(){
        return fileName  + "\nSize: " +fileSize  + "\nUSER: " + owner + "\t\t DATE: "+ dateOfUpload;
    }


}
