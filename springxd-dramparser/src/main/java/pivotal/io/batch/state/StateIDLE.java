package pivotal.io.batch.state;


import pivotal.io.batch.command.Command;

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
    }


    void initValidTransitMap(){

        validTransitMap.put(Command.type.ACT, type.BankActive);
        validTransitMap.put(Command.type.REF, type.IDLE);
//        validTransitMap.put(Command.type.PDE, type.IDLE);
//        validTransitMap.put(Command.type.PDX, type.IDLE);
//        validTransitMap.put(Command.type.ZQC, type.IDLE);
//        validTransitMap.put(Command.type.MRS, type.IDLE);
        validTransitMap.put(Command.type.PRE, type.IDLE);// TODO ignore...


    }


}
