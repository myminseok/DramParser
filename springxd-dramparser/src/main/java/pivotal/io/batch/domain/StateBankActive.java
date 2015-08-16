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
    }

    public void initValidTransitMap(){
        validTransitMap.put(StateCommand.type.PDE, type.ActivePowerDown);
        validTransitMap.put(StateCommand.type.RD,  type.Reading);
        validTransitMap.put(StateCommand.type.WR,  type.Writing);
        validTransitMap.put(StateCommand.type.PRE,  type.IDLE);


    }


}
