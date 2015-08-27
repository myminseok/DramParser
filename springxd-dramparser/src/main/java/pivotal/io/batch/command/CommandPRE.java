package pivotal.io.batch.command;

/**

 */
public class CommandPRE extends Command {

    public static Command instance=null;

    public static Command getInstance(){
        if(instance==null){
            instance= new CommandPRE();
        }
        return instance;
    }

    public CommandPRE(){
        this.name= type.PRE;

    }


    public boolean isMatching(byte[] v){
        return this.getBit(v, INDEX.CKE0.getBinaryIndex())==1 &&
               this.getBit(v, INDEX.CS0.getBinaryIndex())==0 &&
                this.getBit(v, INDEX.ACTN.getBinaryIndex())==1 &&
                this.getBit(v, INDEX.A16.getBinaryIndex())==0 &&
                this.getBit(v, INDEX.A15.getBinaryIndex())==1 &&
                this.getBit(v, INDEX.A14.getBinaryIndex())==0
                ;
    }


}
