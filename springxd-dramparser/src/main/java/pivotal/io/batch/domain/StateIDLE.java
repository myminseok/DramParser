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
    }


    void initValidTransitMap(){

        validTransitMap.put(StateCommand.type.ACT, type.BankActive);
        validTransitMap.put(StateCommand.type.REF, type.IDLE);
//        validTransitMap.put(StateCommand.type.PDE, type.IDLE);
//        validTransitMap.put(StateCommand.type.PDX, type.IDLE);
//        validTransitMap.put(StateCommand.type.ZQC, type.IDLE);
//        validTransitMap.put(StateCommand.type.MRS, type.IDLE);
        validTransitMap.put(StateCommand.type.PRE, type.IDLE);// TODO ignore...


    }


}
