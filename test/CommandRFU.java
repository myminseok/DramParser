package pivotal.io.batch.domain;

/**
reserved.

 */
public class CommandRFU extends Command{
    public CommandRFU(){
        this.name= type.RFU;
        this.setCKE0(1);
        this.setCS0(0);
        this.setACTN(1);
        this.setA16(0);
        this.setA15(1);
        this.setA14(1);
    }


    public boolean isMatching(byte[] v){

        return this.getBit(v,POS.CKE0.getBinaryIndex())==1 &&
                this.getBit(v,POS.CS0.getBinaryIndex())==0 &&
                this.getBit(v,POS.ACTN.getBinaryIndex())==1 &&
                this.getBit(v,POS.A16.getBinaryIndex())==0 &&
                this.getBit(v,POS.A15.getBinaryIndex())==1 &&
                this.getBit(v,POS.A14.getBinaryIndex())==1
                ;


    }



}
