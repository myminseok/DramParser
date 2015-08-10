package pivotal.io.batch.domain;

/**

 */
public class StateCommandUndefined extends StateCommand{

    public static StateCommand instance=null;

    public static StateCommand getInstance(){
        if(instance==null){
            instance= new StateCommandUndefined();
        }
        return instance;
    }


    public StateCommandUndefined(){
        this.name= type.UND;

    }


    public boolean isMatching(byte[] v){
        return false;
    }

}
