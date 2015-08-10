package pivotal.io.batch;

import pivotal.io.batch.domain.*;

import java.util.*;

/**
 * Created by kimm5 on 8/1/15.
 */
public class StateMachine {

    public static Command undefinedCommand = new CommandUndefined();
    public ArrayList<Command> commands = new ArrayList();
    public Set commandToIgnore= new HashSet<Command.type>();

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

    public boolean ignore(Command command){
        return commandToIgnore.contains(command.getName());
    }


    public boolean transit(byte[] v) {
        // 현재 상태에서 가능한 명령 확인, ckel
        // 상태에 따라 명령의 해석이 달라짐.


            return true;
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

    private void initStateObject(){
        stateObjectMap.put(State.type.Undefined, StateUndefined.getInstance());
        stateObjectMap.put(State.type.ActivePowerDown,  StateActivePowerDown.getInstance());
        stateObjectMap.put(State.type.BankActive,  StateBankActive.getInstance());
        stateObjectMap.put(State.type.IDLE,  StateIDLE.getInstance());
        stateObjectMap.put(State.type.Reading,  StateReading.getInstance());
        stateObjectMap.put(State.type.Writing,  StateWriting.getInstance());
    }


    private void initCommand(){

        Command rfu = new CommandRFU();
        Command mrs = new CommandMRS();
        Command ref = new CommandREF();
        Command pre = new CommandPRE();//PRE,PREA
        Command wr = new CommandWR();
        Command rd = new CommandRD();
        Command pdx= new CommandPDX();
        Command pde= new CommandPDE(); // = SRE
        Command act = new CommandACT();
        Command nop = new CommandNOP();

        commands.add(rfu);
        commands.add(mrs);
        commands.add(ref);
        commands.add(pre);
        commands.add(wr);
        commands.add(rd);
        commands.add(pdx);
        commands.add(pde);
        commands.add(act);
        commands.add(nop);


        commandToIgnore.add(Command.type.DES);
        commandToIgnore.add(Command.type.NOP);
        commandToIgnore.add(Command.type.RFU);

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
                cmd.setBytes(v);
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
