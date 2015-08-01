package pivotal.io.batch.domain;


import java.util.HashMap;
import java.util.Map;

public abstract class State {

    public type name;

    public enum type {
        Undefined, IDLE, BankActive, Writing, Reading, ActivePowerDown ;
    }


    public type getName() {
        return name;
    }

//    Map<State.type, Command.type[]> stateCommandMap = new HashMap<State.type, Command.type[]>();

    Map<Command.type, State.type> stateCommandMap2 = new HashMap<Command.type, State.type>();


    public State() {

        initStateMap();
    }

    abstract void initStateMap();


    public State.type nextState(Command.type command){
        if (!stateCommandMap2.containsKey(command)) return State.type.Undefined;
        return stateCommandMap2.get(command);
    }


//    public boolean isValid(State.type type, Command.type command){
//        if(!stateCommandMap.containsKey(type)) return false;
//        Command.type[] commands = stateCommandMap.get(type);
//        return ArrayUtils.indexOf(commands, command)>-1;
//    }

}
