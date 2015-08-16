package pivotal.io.batch.domain;

/**

 */
public class StateCommandPDX extends StateCommand{

    public static StateCommand instance=null;

    public static StateCommand getInstance(){
        if(instance==null){
            instance= new StateCommandPDX();
        }
        return instance;
    }

    public StateCommandPDX(){
        this.name= type.PDX;

    }


    public boolean isMatching(byte[] v){

        return       this.getBit(v, INDEX.CKE0.getBinaryIndex())==1 &&
                this.getBit(v, INDEX.CS0.getBinaryIndex())==1
                ;
    }


}
