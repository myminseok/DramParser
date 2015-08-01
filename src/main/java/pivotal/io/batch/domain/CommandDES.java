package pivotal.io.batch.domain;

/**

 */
public class CommandDES extends Command{

    public CommandDES() {
        this.name = Command.type.DES;
        this.setCKE0(1);
        this.setCS0(1);
    }

//    // no transtion
//    void initStateTransitonMap(){
//    }

    // no transtion
    public State.type nextState(State.type type){
        return type;
    }

    public boolean isMatching(byte[] v){
        return this.getBit(v,POS.CKE0.getPosition())==1
                && this.getBit(v,POS.CS0.getPosition())==1;
    }



}
