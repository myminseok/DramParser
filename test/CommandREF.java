package pivotal.io.batch.domain;

/**
 SRE, REF
 */
public class CommandREF extends Command{
    public CommandREF(){
        this.name= Command.type.REF;
        this.setCKE0(1);
        this.setCS0(0);
        this.setACTN(1);
        this.setA16(0);
        this.setA15(0);
        this.setA14(1);
    }


    public boolean isMatching(byte[] v){

        return this.getBit(v,POS.CKE0.getBinaryIndex())==1 &&
               this.getBit(v,POS.CS0.getBinaryIndex())==0 &&
                this.getBit(v,POS.ACTN.getBinaryIndex())==1 &&
                this.getBit(v,POS.A16.getBinaryIndex())==0 &&
                this.getBit(v,POS.A15.getBinaryIndex())==0 &&
                this.getBit(v,POS.A14.getBinaryIndex())==1
                ;
    }

}
