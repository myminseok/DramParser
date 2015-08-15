package com.pivotal.pxf.plugins.dram;

import org.apache.hadoop.io.BytesWritable;

import java.io.DataOutput;
import java.io.UnsupportedEncodingException;

/**
 * Created by kimm5 on 8/16/15.
 */
public class Test {

    public static void main (String[] args) throws UnsupportedEncodingException {
        String str = "Created by kimm5 on 8/16/15.";
        BytesWritable bw = new BytesWritable();
        bw.set(str.getBytes("UTF8"), 0, str.getBytes("UTF8").length);
        System.out.println(str);
        System.out.println(new String(bw.getBytes(), "UTF8"));

        BytesWritable bw2 = new BytesWritable(str.getBytes());
        System.out.println(new String(bw2.getBytes(),"UTF8"));
        System.out.println(bw2);
    }
}
