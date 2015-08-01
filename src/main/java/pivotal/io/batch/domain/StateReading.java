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
        initStateMap();
    }


    void initStateMap(){
//        stateCommandMap.put(type.Reading, new Command.type[]{Command.type.WR, Command.type.RD, Command.type.PRE});


        stateCommandMap2.put(Command.type.WR,  type.Writing);
        stateCommandMap2.put(Command.type.RD,  type.Reading);
        stateCommandMap2.put(Command.type.PRE,  type.IDLE);

    }


}
