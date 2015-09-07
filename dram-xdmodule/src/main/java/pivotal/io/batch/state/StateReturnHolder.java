package pivotal.io.batch.state;

import pivotal.io.batch.command.Command;

/**
 * Created by kimm5 on 8/10/15.
 */
public class StateReturnHolder {

    public State.type triedStateType =null;
    public Command triedCommand =null;

    public void clear(){
        this.triedStateType =null;
        this.triedCommand =null;
    }
}
