package pivotal.io.batch.state;


import pivotal.io.batch.command.Command;
import pivotal.io.batch.command.CommandUndefined;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class State {

    public type name;

    public enum type {
        Undefined, IDLE, BankActive, Writing, Reading, ActivePowerDown ;
    }

    public type getName() {
        return name;
    }

    protected Map<Command.type, State.type> validTransitMap = new HashMap<Command.type, State.type>();

    protected Set<Command.type> validTransitMapKeySet;

    public State() {
        initValidTransitMap();
        validTransitMapKeySet= validTransitMap.keySet();
    }

    abstract void initValidTransitMap();


    public boolean nextState(Map<Command.type, Command> stateCommandFullSetMap, byte[] bytes,StateReturnHolder stateReturnHolder){
        // type별 command 구현체 추출.
        // command matching확인.
        Command cmd=null;
        for(Command.type cmdType: validTransitMapKeySet){
            cmd = stateCommandFullSetMap.get(cmdType);
            if(cmd==null){
                continue;
            }

            if(cmd.isMatching(bytes)){
                stateReturnHolder.triedCommand =cmd;
                stateReturnHolder.triedStateType =validTransitMap.get(cmdType);
                return true;
            }
        }
        stateReturnHolder.triedStateType =type.Undefined;
        stateReturnHolder.triedCommand = CommandUndefined.getInstance();
        return false;
    }

}
