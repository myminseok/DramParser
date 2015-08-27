package pivotal.io.batch.state;


import pivotal.io.batch.command.Command;

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
        validTransitMap.put(Command.type.PDE, type.ActivePowerDown);
        validTransitMap.put(Command.type.RD,  type.Reading);
        validTransitMap.put(Command.type.WR,  type.Writing);
        validTransitMap.put(Command.type.PRE,  type.IDLE);
        validTransitMap.put(Command.type.ACT,  type.BankActive); // TODO ???


    }


}
