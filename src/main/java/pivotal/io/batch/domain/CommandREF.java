package pivotal.io.batch.domain;

/**

 */
public class CommandREF extends Command{
    public CommandREF(){
        this.name= Command.type.REF;
//        this.setCKE0(1);
        this.setCS0(0);
        this.setACTN(1);
        this.setA16(0);
        this.setA15(0);
        this.setA14(1);
    }

//    void initStateTransitonMap(){
//        this.stateTransitionMap.put(State.type.Undefined, State.type.IDLE);
//        this.stateTransitionMap.put(State.type.IDLE, State.type.IDLE);
//    }

    public boolean isMatching(byte[] v){
//        return this.getBit(v,POS.CKE0.getPosition())==1
        return  this.getBit(v,POS.CS0.getPosition())==0
                && this.getBit(v,POS.ACTN.getPosition())==1
                && this.getBit(v,POS.A16.getPosition())==0
                && this.getBit(v,POS.A15.getPosition())==0
                && this.getBit(v,POS.A14.getPosition())==1
                ;
    }



}
