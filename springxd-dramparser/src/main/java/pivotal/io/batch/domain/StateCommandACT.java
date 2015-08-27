package pivotal.io.batch.domain;

/**

 */
public class StateCommandACT extends StateCommand{

    public static StateCommand instance=null;

    public static StateCommand getInstance(){
        if(instance==null){
            instance= new StateCommandACT();
        }
        return instance;
    }

    public StateCommandACT(){
        this.name= type.ACT;

    }


    public boolean isMatching(byte[] v){
        return
                this.getBit(v, INDEX.CKE0.getBinaryIndex())==1 &&
                        this.getBit(v, INDEX.CS0.getBinaryIndex())==0 &&
                        this.getBit(v, INDEX.ACTN.getBinaryIndex())==0;
    }




}
