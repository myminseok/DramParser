package pivotal.io.batch.domain;

/**

 */
public class StateCommandPDE extends StateCommand{

    public static StateCommand instance=null;

    public static StateCommand getInstance(){
        if(instance==null){
            instance= new StateCommandPDE();
        }
        return instance;
    }

    public StateCommandPDE(){
        this.name= type.PDE;

    }


    public boolean isMatching(byte[] v){

        return       this.getBit(v, INDEX.CKE0.getBinaryIndex())==0 &&
                this.getBit(v, INDEX.CS0.getBinaryIndex())==1
                ;
    }



}
