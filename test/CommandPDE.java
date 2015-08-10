package pivotal.io.batch.domain;


public class CommandPDE extends Command{

    public CommandPDE() {
        this.name = Command.type.PDE;
        this.setCKE0(0);
        this.setCS0(1);
    }

    public boolean isMatching(byte[] v){
        return this.getBit(v,POS.CKE0.getBinaryIndex())==0
                && this.getBit(v,POS.CS0.getBinaryIndex())==1;
    }



}
