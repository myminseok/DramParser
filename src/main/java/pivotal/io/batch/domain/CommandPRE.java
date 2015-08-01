package pivotal.io.batch.domain;

/**


 */
public class CommandPRE extends Command{
    public CommandPRE(){
        this.name= Command.type.PRE;
        this.setCKE0(1);
        this.setCS0(0);
        this.setACTN(1);
        this.setA16(0);
        this.setA15(1);
        this.setA14(0);
//        this.setA10(0);
    }

//    void initStateTransitonMap(){
//        this.stateTransitionMap.put(State.type.BankActive, State.type.IDLE);
//        this.stateTransitionMap.put(State.type.Writing, State.type.IDLE);
//        this.stateTransitionMap.put(State.type.Reading, State.type.IDLE);
//
//    }


    public boolean isMatching(byte[] v){
        return this.getBit(v,POS.CKE0.getPosition())==1
                && this.getBit(v,POS.CS0.getPosition())==0
                && this.getBit(v,POS.ACTN.getPosition())==1
                && this.getBit(v,POS.A16.getPosition())==0
                && this.getBit(v,POS.A15.getPosition())==1
                && this.getBit(v,POS.A14.getPosition())==0
                ;
    }



}
