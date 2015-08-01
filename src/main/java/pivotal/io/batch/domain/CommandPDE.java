package pivotal.io.batch.domain;


public class CommandPDE extends Command{

    public CommandPDE() {
        this.name = Command.type.PDE;
        this.setCKE0(0);
        this.setCS0(1);
    }

//    void initStateTransitonMap(){
//        this.stateTransitionMap.put(State.type.BankActive, State.type.ActivePowerDown);
//
//    }

    public boolean isMatching(byte[] v){
        return this.getBit(v,POS.CKE0.getPosition())==0
                && this.getBit(v,POS.CS0.getPosition())==1;
    }



}
