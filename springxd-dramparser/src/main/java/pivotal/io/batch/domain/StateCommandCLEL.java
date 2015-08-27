package pivotal.io.batch.domain;

/**

 */
public class StateCommandCLEL extends StateCommand{

    public static StateCommand instance=null;

    public static StateCommand getInstance(){
        if(instance==null){
            instance= new StateCommandCLEL();
        }
        return instance;
    }

    public StateCommandCLEL(){
        this.name= type.CKE_L;

    }


    public boolean isMatching(byte[] v){
        return this.getBit(v, INDEX.CKE0.getBinaryIndex())==0;
    }

}
