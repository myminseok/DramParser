package pivotal.io.batch.domain;

/**
 * Created by kimm5 on 8/10/15.
 */
public class StateInfo {

    public State prevState = StateUndefined.getInstance();
    public State currentState = StateUndefined.getInstance();
    public boolean isTransit=false;

    public StateCommand newCommand =null;
    public byte[] newData= null;


    public void clear(){

        this.isTransit=false;
        this.newCommand =null;
        this.newData=null;
    }

    public String toStringHeader(boolean isTransit){
        StringBuilder sb = new StringBuilder();
        if(isTransit) {
            sb.append("prevState, ");
            sb.append("newCommand, ");
            sb.append("currentState");
            return sb.toString();
        } else {
            sb.append("prevState");
            return sb.toString();
        }
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if(isTransit) {
            sb.append(prevState == null ? "" : prevState.getName()).append(", ");
            sb.append((newCommand == null ? "" : newCommand.getName())).append(", ");
            sb.append(currentState == null ? "" : currentState.getName());
            return sb.toString();
        } else {
            sb.append(currentState == null ? "" : currentState.getName());
            return sb.toString();
        }
    }



}
