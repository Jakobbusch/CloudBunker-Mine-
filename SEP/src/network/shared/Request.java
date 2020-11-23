package network.shared;

import java.io.Serializable;

public class Request implements Serializable {

    public enum TYPE {
        ADD,
        REMOVE,
        LOGIN,
        MESSAGE,
        FILE,
        DOWNLOAD,
        EDITPASS,
        EDITEMAIL,
        CREATEACC,
        ACCOUNTS,
        DELETE,
        EMAIL,
        DELETEFILE,
        ADMIN
    }

    public TYPE type;
    public Object argument;

    private Account account;

    /**
     * A constructor for the Request class
     * @param type An enum type for the type of request
     * @param t An Object that is going to be sent
     * @param account an Account object
     */
    public Request(TYPE type, Object t, Account account) {
        this.type = type;
        this.argument = t;
        this.account = account;
    }

    /**
     * A constructor for the Request class
     * @param type an enum type
     * @param t an object that is going to be sent
     */
    public Request(TYPE type, Object t) {
        this.type = type;
        this.argument = t;
    }

    /**
     * A set method for an object
     * @param t an object
     */
    public void set(Object t)
    {
        this.argument =t;
    }

    /**
     * A getter method for an object
     * @return an Object
     */
    public Object get()
    {
        return argument;
    }
}
