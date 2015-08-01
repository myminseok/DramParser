package pivotal.io.batch.domain;


public class CommandACT extends Command{

    public CommandACT() {
        this.name = Command.type.ACT;
        this.setCS0(0);
        this.setACTN(0);
    }

//    void initStateTransitonMap(){
//        this.stateTransitionMap.put(State.type.IDLE, State.type.BankActive);
//
//    }

    public boolean isMatching(byte[] v){
        return this.getBit(v,POS.CS0.getPosition())==0
                && this.getBit(v,POS.ACTN.getPosition())==0;
    }



}
