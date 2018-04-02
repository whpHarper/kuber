package com.databps.bigdaf.kuber;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.io.*;

/**
 * @author whp 18-3-28
 */
public class HadoopstreamTest extends Configured {

    public static final String FS_PARAM_NAME = "fs.defaultFS";
    private String filename="/srv/ppp.jpg";
    private String uploadPath="/mmm";
    private String dest="/mmm/ppp.jpg";
    private String decrptePath="/mnt/ppp_new.jpg";

    @Test
    public void testUpload(){
        try {
            File inputPath = new File(filename);
            Path outputPath = new Path(dest);

            if (!inputPath.exists()) {
                System.err.println("Input file does not exist!");
            }

            Configuration conf = getConf();
//            conf.set(FS_PARAM_NAME, uploadPath);
//
//            System.out.println("configured filesystem = " + conf.get(FS_PARAM_NAME));

            FileSystem fs = FileSystem.get(conf);

            if (fs.exists(outputPath)) {
                System.err.println("Output path exists!");
            }

            InputStream is = new BufferedInputStream(new FileInputStream(filename));
            OutputStream os = fs.create(outputPath);

            System.out.println("\nKuber encryption started!\n");

            byte[] b = new byte[1024 * 100];

            int numBytes = 0;

            while ((numBytes = is.read(b)) > 0) {
//      byte[] block = Arrays.copyOfRange(b, 0, numBytes);
//      byte[] encrypted = encrypt(b);
                os.write(b);
            }

            is.close();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
