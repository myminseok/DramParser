package pivotal.io.batch.command;

/**

 */
public class CommandCLEL extends Command {

    public static Command instance=null;

    public static Command getInstance(){
        if(instance==null){
            instance= new CommandCLEL();
        }
        return instance;
    }

    public CommandCLEL(){
        this.name= type.CKE_L;

    }


    public boolean isMatching(byte[] v){
        return this.getBit(v, INDEX.CKE0.getBinaryIndex())==0;
    }

}
