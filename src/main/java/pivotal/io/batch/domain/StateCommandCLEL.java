package pivotal.io.batch.domain;

/**

 */
public class StateCommandCLEL extends StateCommand{
    public StateCommandCLEL(){
        this.name= type.CLEL;
        this.setCKE0(0);

    }


    public boolean isMatching(byte[] v){

        return this.getBit(v,POS.CKE0.getBinaryIndex())==0;
    }

}
