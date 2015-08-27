package pivotal.io.batch.command;

/**

 */
public class CommandZQC extends Command {

    public static Command instance=null;

    public static Command getInstance(){
        if(instance==null){
            instance= new CommandZQC();
        }
        return instance;
    }

    public CommandZQC(){
        this.name= type.ZQC;

    }


    public boolean isMatching(byte[] v){

        return  this.getBit(v, INDEX.CS0.getBinaryIndex())==0 &&
                this.getBit(v, INDEX.ACTN.getBinaryIndex())==1 &&
                this.getBit(v, INDEX.A16.getBinaryIndex())==1 &&
                this.getBit(v, INDEX.A15.getBinaryIndex())==1 &&
                this.getBit(v, INDEX.A14.getBinaryIndex())==0
                ;
    }


}
