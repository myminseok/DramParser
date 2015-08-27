package pivotal.io.batch.command;

/**

 */
public class CommandPDE extends Command {

    public static Command instance=null;

    public static Command getInstance(){
        if(instance==null){
            instance= new CommandPDE();
        }
        return instance;
    }

    public CommandPDE(){
        this.name= type.PDE;

    }


    public boolean isMatching(byte[] v){

        return       this.getBit(v, INDEX.CKE0.getBinaryIndex())==0 &&
                this.getBit(v, INDEX.CS0.getBinaryIndex())==1
                ;
    }



}
