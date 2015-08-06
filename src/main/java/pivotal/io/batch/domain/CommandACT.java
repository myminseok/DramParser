package pivotal.io.batch.domain;


public class CommandACT extends Command{

    public CommandACT() {
        this.name = Command.type.ACT;
        this.setCKE0(1);
        this.setCS0(0);
        this.setACTN(0);
    }


    public boolean isMatching(byte[] v){
        return
                this.getBit(v,POS.CKE0.getPosition())==1 &&
                this.getBit(v,POS.CS0.getPosition())==0 &&
                this.getBit(v,POS.ACTN.getPosition())==0;
    }



}
