package pivotal.io.batch;

import org.springframework.xd.module.options.spi.ModuleOption;

public class Options {


    private String infile =null;

    private String outdir =null;

    private String fsUri =null;

    private String rollover=null;

    public String getInfile() {
        return infile;
    }

    public String getOutdir() {
        return outdir ==null?"/tmp": outdir;
    }

    public String getFsUri() {
        return fsUri ==null? "": fsUri;
    }

    public String getRollover() {
        return rollover==null? String.valueOf(1024*1024*1024):rollover;
    }


    @ModuleOption("hdfs uri")
    public void setFsUri(String fsUri) {
        this.fsUri = fsUri;
    }

    @ModuleOption("hdfs rollover")
    public void setRollover(String rollover) {
        this.rollover = rollover;
    }

    @ModuleOption("the file path to process")
    public void setInfile(String infile) {
        this.infile = infile;
    }

    @ModuleOption("the file path to process")
    public void setOutdir(String outdir) {
        this.outdir = outdir;
    }


}