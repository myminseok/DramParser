package pivotal.io.batch.state;


import pivotal.io.batch.command.Command;

public class StateReading extends State{


    public static State instance=null;

    public static State getInstance(){
        if(instance==null){
            instance= new StateReading();
        }
        return instance;
    }


    public StateReading() {
        name= type.Reading;
    }


    void initValidTransitMap(){
        validTransitMap.put(Command.type.WR,  type.Writing);
        validTransitMap.put(Command.type.RD,  type.Reading);
        validTransitMap.put(Command.type.PRE,  type.IDLE);
        validTransitMap.put(Command.type.ACT,  type.BankActive); // TODO ??
    }


}
