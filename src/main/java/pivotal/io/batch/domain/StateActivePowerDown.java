package pivotal.io.batch.domain;


public class StateActivePowerDown extends State{

    public static State instance=null;

    public static State getInstance(){
        if(instance==null){
            instance= new StateActivePowerDown();
        }
        return instance;
    }

    private StateActivePowerDown() {
        name= type.ActivePowerDown;
        initStateMap();
    }


    void initStateMap(){
//        stateCommandMap.put(type.ActivePowerDown, new Command.type[]{Command.type.PDX});
        stateCommandMap2.put(Command.type.PDX, State.type.BankActive);
    }


}
