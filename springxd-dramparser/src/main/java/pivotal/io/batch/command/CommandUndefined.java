package pivotal.io.batch.command;

/**

 */
public class CommandUndefined extends Command {

    public static Command instance=null;

    public static Command getInstance(){
        if(instance==null){
            instance= new CommandUndefined();
        }
        return instance;
    }


    public CommandUndefined(){
        this.name= type.UND;

    }


    public boolean isMatching(byte[] v){
        return false;
    }

}
