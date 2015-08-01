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
        initStateMap();
    }

    void initStateMap(){
        stateCommandMap2.put(Command.type.REF,  type.IDLE);
    }
}
