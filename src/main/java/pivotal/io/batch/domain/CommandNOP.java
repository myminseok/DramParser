package pivotal.io.batch.domain;

/**

 */
public class CommandNOP extends Command{
    public CommandNOP(){
        this.name= Command.type.NOP;

        this.setCKE0(1);
        this.setCS0(0);
        this.setACTN(1);
        this.setA16(1);
        this.setA15(1);
        this.setA14(1);
    }

//    // no transtion
//    void initStateTransitonMap(){
//    }

    // no transtion
    public State.type nextState(State.type type){
        return type;
    }

    public boolean isMatching(byte[] v){
        return this.getBit(v, POS.CKE0.getPosition())==1
                && this.getBit(v,POS.CS0.getPosition())==0
                && this.getBit(v,POS.ACTN.getPosition())==1
                && this.getBit(v,POS.A16.getPosition())==1
                && this.getBit(v,POS.A15.getPosition())==1
                && this.getBit(v,POS.A14.getPosition())==1
                ;
    }



}
