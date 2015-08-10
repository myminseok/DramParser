package pivotal.io.batch.domain;

/**

 */
public class StateCommandREF extends StateCommand{

    public static StateCommand instance=null;

    public static StateCommand getInstance(){
        if(instance==null){
            instance= new StateCommandREF();
        }
        return instance;
    }

    public StateCommandREF(){
        this.name= type.REF;

    }


    public boolean isMatching(byte[] v){

        return   this.getBit(v, INDEX.CKE0.getBinaryIndex())==1 &&
                this.getBit(v, INDEX.CS0.getBinaryIndex())==0 &&
                this.getBit(v, INDEX.ACTN.getBinaryIndex())==1 &&
                this.getBit(v, INDEX.A16.getBinaryIndex())==0 &&
                this.getBit(v, INDEX.A15.getBinaryIndex())==0 &&
                this.getBit(v, INDEX.A14.getBinaryIndex())==1
                ;
    }

}
