package pivotal.io.batch.domain;

/**
 * DES
 * PDX
 */
public class CommandPDX extends Command{

    public CommandPDX() {
        this.name = Command.type.PDX;
        this.setCKE0(1);
        this.setCS0(1);
    }

    public boolean isMatching(byte[] v){
        return this.getBit(v,POS.CKE0.getBinaryIndex())==1
                && this.getBit(v,POS.CS0.getBinaryIndex())==1;
    }



}
