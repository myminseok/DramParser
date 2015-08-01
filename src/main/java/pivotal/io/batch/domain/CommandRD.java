package pivotal.io.batch.domain;

/**

 */
public class CommandRD extends Command{
    public CommandRD(){
        this.name= Command.type.RD;
        //this.setCKE0(1);
        this.setCS0(0);
        this.setACTN(1);
        this.setA16(1);
        this.setA15(0);
        this.setA14(1);
    }
//
//    void initStateTransitonMap(){
//
//        this.stateTransitionMap.put(State.type.BankActive, State.type.Reading);
//        this.stateTransitionMap.put(State.type.Reading, State.type.Reading);
//        this.stateTransitionMap.put(State.type.Writing, State.type.Reading);
//    }

    public boolean isMatching(byte[] v){
//        return this.getBit(v,POS.CKE0.getPosition())==1
        return    this.getBit(v,POS.CS0.getPosition())==0
                && this.getBit(v,POS.ACTN.getPosition())==1
                && this.getBit(v,POS.A16.getPosition())==1
                && this.getBit(v,POS.A15.getPosition())==0
                && this.getBit(v,POS.A14.getPosition())==1
                ;
    }



}
