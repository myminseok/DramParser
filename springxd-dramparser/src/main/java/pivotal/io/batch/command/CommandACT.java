package pivotal.io.batch.command;

/**

 */
public class CommandACT extends Command {

    public static Command instance=null;

    public static Command getInstance(){
        if(instance==null){
            instance= new CommandACT();
        }
        return instance;
    }

    public CommandACT(){
        this.name= type.ACT;

    }


    public boolean isMatching(byte[] v){
        return
                this.getBit(v, INDEX.CKE0.getBinaryIndex())==1 &&
                        this.getBit(v, INDEX.CS0.getBinaryIndex())==0 &&
                        this.getBit(v, INDEX.ACTN.getBinaryIndex())==0;
    }




}
