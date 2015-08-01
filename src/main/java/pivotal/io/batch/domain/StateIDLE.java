package pivotal.io.batch.domain;


public class StateIDLE extends State{


    public static State instance=null;

    public static State getInstance(){
        if(instance==null){
            instance= new StateIDLE();
        }
        return instance;
    }


    public StateIDLE() {
        name= type.IDLE;
        initStateMap();
    }


    void initStateMap(){
//        stateCommandMap.put(type.IDLE, new Command.type[]{Command.type.ACT, Command.type.PDE, Command.type.REF});

        stateCommandMap2.put(Command.type.ACT, type.BankActive);
//        stateCommandMap2.put(new CommandPDE(), new StateBankActive());
        stateCommandMap2.put(Command.type.REF, type.IDLE);

    }


}
