package pivotal.io.batch.domain;


public class StateUndefined extends State{

    public static State instance=null;

    public static State getInstance(){
        if(instance==null){
            instance= new StateUndefined();
        }
        return instance;
    }


    public StateUndefined() {
        name= type.Undefined;
    }



    void initValidTransitMap(){
        validTransitMap.put(StateCommand.type.REF, type.IDLE);
//        validTransitMap.put(StateCommand.type.PDE, type.IDLE);
//        validTransitMap.put(StateCommand.type.PDX, type.IDLE);
//        validTransitMap.put(StateCommand.type.ZQC, type.IDLE);
//        validTransitMap.put(StateCommand.type.MRS, type.IDLE);
        validTransitMap.put(StateCommand.type.PRE, type.IDLE);// TODO ignore...

    }
}
