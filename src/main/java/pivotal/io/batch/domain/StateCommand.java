package pivotal.io.batch.domain;

public abstract class StateCommand extends Command{

    type name;
    public  enum type{
        CLEL ;
    }

}
