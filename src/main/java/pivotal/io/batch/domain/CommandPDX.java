package pivotal.io.batch.domain;


public class CommandPDX extends Command{

    public CommandPDX() {
        this.name = Command.type.PDX;
        this.setCKE0(1);
        this.setCS0(1);
    }

//    void initStateTransitonMap(){
//        this.stateTransitionMap.put(State.type.ActivePowerDown, State.type.BankActive);
//    }

    public boolean isMatching(byte[] v){
        return this.getBit(v,POS.CKE0.getPosition())==1
                && this.getBit(v,POS.CS0.getPosition())==1;
    }



}
