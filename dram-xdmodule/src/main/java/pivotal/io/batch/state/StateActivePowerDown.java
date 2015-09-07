package pivotal.io.batch.state;


import pivotal.io.batch.command.Command;

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
    }

    public void initValidTransitMap(){
        validTransitMap.put(Command.type.CKE_L, type.ActivePowerDown);
        validTransitMap.put(Command.type.PDX, type.BankActive);
    }

}
