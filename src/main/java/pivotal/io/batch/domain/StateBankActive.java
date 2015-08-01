package pivotal.io.batch.domain;


public class StateBankActive extends State{

    public static State instance=null;

    public static State getInstance(){
        if(instance==null){
            instance= new StateBankActive();
        }
        return instance;
    }

    public StateBankActive() {
        name= type.BankActive;
        initStateMap();
    }


    void initStateMap(){
//        stateCommandMap.put(type.BankActive, new Command.type[]{Command.type.PDX, Command.type.RD, Command.type.WR, Command.type.PRE});
        stateCommandMap2.put(Command.type.PDE, type.ActivePowerDown);
        stateCommandMap2.put(Command.type.RD,  type.Reading);
        stateCommandMap2.put(Command.type.WR,  type.Writing);
        stateCommandMap2.put(Command.type.PRE,  type.IDLE);


    }


}
