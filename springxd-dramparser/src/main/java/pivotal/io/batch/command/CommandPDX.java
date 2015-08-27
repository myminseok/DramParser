package pivotal.io.batch.command;

/**

 */
public class CommandPDX extends Command {

    public static Command instance=null;

    public static Command getInstance(){
        if(instance==null){
            instance= new CommandPDX();
        }
        return instance;
    }

    public CommandPDX(){
        this.name= type.PDX;

    }


    public boolean isMatching(byte[] v){

        return       this.getBit(v, INDEX.CKE0.getBinaryIndex())==1 &&
                this.getBit(v, INDEX.CS0.getBinaryIndex())==1
                ;
    }


}
