package pivotal.io.batch;

import org.springframework.xd.module.options.spi.ModuleOption;

public class Options {

    private String infilepath=null;

    private String outdirpath=null;

    private String outfileextension=null;

    public String getInfilepath() {
        return infilepath;
    }

    public String getOutdirpath() {
        return outdirpath==null?"/tmp":outdirpath;
    }

    public String getOutfileextension() {
        return outfileextension==null?"out":outfileextension;
    }

    @ModuleOption("the file path to process")
    public void setInfilepath(String infilepath) {
        this.infilepath = infilepath;
        System.out.println("option:infilepath:"+infilepath);
    }

    @ModuleOption("the file path to process")
    public void setOutdirpath(String outdirpath) {
        this.outdirpath = outdirpath;
        System.out.println("option:outdirpath:"+outdirpath);
    }

    @ModuleOption("the file extension to use")
    public void setOutfileextension(String extension) {
        this.outfileextension = extension;
        System.out.println("option:outfileextension:"+outfileextension);
    }

}