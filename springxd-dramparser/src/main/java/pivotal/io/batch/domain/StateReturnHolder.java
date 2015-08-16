package pivotal.io.batch.domain;

/**
 * Created by kimm5 on 8/10/15.
 */
public class StateReturnHolder {

    public State.type nextStateType=null;
    public StateCommand nextCommand=null;

    public void clear(){
        this.nextStateType=null;
        this.nextCommand=null;
    }
}
