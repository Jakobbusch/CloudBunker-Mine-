package user.model;

import network.client.Client;

public class ModelFactory {


    private DataModelManager model;
    private Client client ;

    /**
     * A constructor for the ModelFactory class
     * @param client a Client Object
     */
    public ModelFactory(Client client) {
        this.client =   client;
    }

    /**
     * A method for returning the DataModelmanager class
     * @return a DataModelManager Object
     */
    public DataModelManager getModel(){
        if(model == null){
            model = new DataModelManager(client);
        }
        return model;
    }

}


