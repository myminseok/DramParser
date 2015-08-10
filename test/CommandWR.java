package pivotal.io.batch.domain;

/**

 */
public class CommandWR extends Command{
    public CommandWR(){
        this.name= Command.type.WR;
//        this.setCKE0(1);
        this.setCS0(0);
        this.setACTN(1);
        this.setA16(1);
        this.setA15(0);
        this.setA14(0);
    }


    public boolean isMatching(byte[] v){

//        return this.getBit(v,POS.CKE0.getBinaryIndex())==1 &&
          return      this.getBit(v,POS.CS0.getBinaryIndex())==0 &&
                this.getBit(v,POS.ACTN.getBinaryIndex())==1 &&
                this.getBit(v,POS.A16.getBinaryIndex())==1 &&
                this.getBit(v,POS.A15.getBinaryIndex())==0 &&
                this.getBit(v,POS.A14.getBinaryIndex())==0
                ;
    }

}
