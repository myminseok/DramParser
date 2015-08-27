package pivotal.io.batch.domain;

/**
 * Created by kimm5 on 8/10/15.
 */
public class StateReturnHolder {

    public State.type triedStateType =null;
    public StateCommand triedCommand =null;

    public void clear(){
        this.triedStateType =null;
        this.triedCommand =null;
    }
}
