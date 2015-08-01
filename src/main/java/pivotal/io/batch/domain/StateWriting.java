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
        initStateMap();
    }


    void initStateMap(){
//        stateCommandMap.put(type.Writing, new Command.type[]{Command.type.WR, Command.type.RD, Command.type.PRE});

        stateCommandMap2.put(Command.type.WR,  type.Writing);
        stateCommandMap2.put(Command.type.RD,  type.Reading);
        stateCommandMap2.put(Command.type.PRE,  type.IDLE);


    }


}
