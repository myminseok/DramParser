package pivotal.io.batch.command;

/**

 */
public class CommandMRS extends Command {

    public static Command instance=null;

    public static Command getInstance(){
        if(instance==null){
            instance= new CommandMRS();
        }
        return instance;
    }

    public CommandMRS(){
        this.name= type.MRS;

    }


    public boolean isMatching(byte[] v){

        return  this.getBit(v, INDEX.CKE0.getBinaryIndex())==1 &&
                this.getBit(v, INDEX.CS0.getBinaryIndex())==0 &&
                this.getBit(v, INDEX.ACTN.getBinaryIndex())==1 &&
                this.getBit(v, INDEX.A16.getBinaryIndex())==0 &&
                this.getBit(v, INDEX.A15.getBinaryIndex())==0 &&
                this.getBit(v, INDEX.A14.getBinaryIndex())==0
                ;
    }


}
