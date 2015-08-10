package pivotal.io.batch.domain;


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
        validTransitMap.put(StateCommand.type.WR,  type.Writing);
        validTransitMap.put(StateCommand.type.RD,  type.Reading);
        validTransitMap.put(StateCommand.type.PRE,  type.IDLE);
    }


}
