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
    }

    public void initValidTransitMap(){
        validTransitMap.put(StateCommand.type.CKE_L, type.ActivePowerDown);
        validTransitMap.put(StateCommand.type.PDX, type.BankActive);
    }

}
