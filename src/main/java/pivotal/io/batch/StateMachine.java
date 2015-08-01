package pivotal.io.batch;

import pivotal.io.batch.domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kimm5 on 8/1/15.
 */
public class StateMachine {

    public static Command undefinedCommand = new CommandUndefined();
    public ArrayList<Command> commands = new ArrayList();
    Map<State.type, State> stateObjectMap = new HashMap<State.type, State>();

    public State.type prevSate;
    public State.type currentState;
    public Command currentCommand;

    public StateMachine(){
        currentState = State.type.Undefined;
        initCommand();
        initStateObject();
    }

    public State getState(State.type state){
        return this.stateObjectMap.get(state);
    }


    public boolean transit(Command command){
        State.type next = getState(currentState).nextState(command.getName());

        if(next.equals(State.type.Undefined)){
            return false;
        }else{
            prevSate=currentState;
            currentState= next;
            currentCommand=command;
            return true;
        }

    }


    public boolean isValidTransit(Command command){
        State.type next = getState(currentState).nextState(command.getName());
        return !next.equals(State.type.Undefined);
    }



    private void initStateObject(){
        stateObjectMap.put(State.type.Undefined, StateUndefined.getInstance());
        stateObjectMap.put(State.type.ActivePowerDown,  StateActivePowerDown.getInstance());
        stateObjectMap.put(State.type.BankActive,  StateBankActive.getInstance());
        stateObjectMap.put(State.type.IDLE,  StateIDLE.getInstance());
        stateObjectMap.put(State.type.Reading,  StateReading.getInstance());
        stateObjectMap.put(State.type.Writing,  StateWriting.getInstance());
    }


    private void initCommand(){
//
//        Command pdx = new Command("PDX");
//        pde.setCKE0(1);
//        pde.setCS0(1);


        Command des = new CommandDES();
        Command nop = new CommandNOP();
        Command ref = new CommandREF();
//        Command sre = new CommandSRE();
        Command act = new CommandACT();
        Command pre = new CommandPRE();//PRE,PREA
        Command wr = new CommandWR();
        Command rd = new CommandRD();



// keep order
        commands.add(nop);
        commands.add(rd);
        commands.add(wr);
        commands.add(ref);
        commands.add(des);
        commands.add(pre);
//        commands.add(sre);
        commands.add(act);

    }



    public Command.type getCommandType(byte[] v){
        for(Command cmd: commands){
            if(cmd.isMatching(v)){
                return cmd.getName();
            };
        }
        return Command.type.UND;
    }


    public Command getCommand(byte[] v){
        for(Command cmd: commands){
            if(cmd.isMatching(v)){
                return cmd;
            };
        }
        return undefinedCommand;
    }


    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[SM:");
        sb.append("prev:"+this.prevSate);
        sb.append(", ");
        sb.append("current:"+this.currentState);
        sb.append(", ");
        sb.append("by:" + (currentCommand == null ? "" : currentCommand.getName()));
        sb.append("]");
        return sb.toString();
    }





}
