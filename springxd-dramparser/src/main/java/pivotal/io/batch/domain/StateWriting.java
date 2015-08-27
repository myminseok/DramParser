package pivotal.io.batch.domain;


public class StateWriting extends State{

    public static State instance=null;

    public static State getInstance(){
        if(instance==null){
            instance= new StateWriting();
        }
        return instance;
    }


    public StateWriting() {
        name= type.Writing;
    }


    void initValidTransitMap(){
        validTransitMap.put(StateCommand.type.WR,  type.Writing);
        validTransitMap.put(StateCommand.type.RD,  type.Reading);
        validTransitMap.put(StateCommand.type.PRE,  type.IDLE);
        validTransitMap.put(StateCommand.type.ACT,  type.BankActive); // TODO ??


    }


}
