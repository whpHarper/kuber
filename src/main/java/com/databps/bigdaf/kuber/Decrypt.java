package com.databps.bigdaf.kuber;

import com.databps.bigdaf.kuber.algorithms.AesCtr;
import com.databps.bigdaf.kuber.algorithms.ChaCha20;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @author merlin
 * @create 2018-03-08 下午5:34
 */
public class Decrypt extends Configured implements Tool {

  public static final String FS_PARAM_NAME = "fs.defaultFS";

  public int run(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("HdfsWriter [hdfs input path] [local output path]");
      return 1;
    }

    // Get the filename out of the file path
    String filename = args[0].substring(args[0].lastIndexOf('/') + 1,args[0].length());

    String dest = args[1];
    // Create the destination path including the filename.
    if (dest.charAt(dest.length() - 1) != '/') {
      dest = dest + "/" + filename;
    } else {
      dest = dest + filename;
    }

    Path inputPath = new Path(args[0]);

    Configuration conf = getConf();
    conf.set(FS_PARAM_NAME,args[2]);
    System.out.println("configured filesystem = " + conf.get(FS_PARAM_NAME));

    FileSystem fs = FileSystem.get(conf);

    if (!fs.exists(inputPath)) {
      System.out.println("File " + filename + " does not exists!");
      return 1;
    }
    InputStream is = fs.open(inputPath);

    File outputPath = new File(dest+"_new");
    //IOUtils.copyBytes(is, new FileOutputStream(new File(destPath)),2048, true);
    OutputStream os= new BufferedOutputStream(new FileOutputStream(outputPath));
    if (outputPath.exists()) {
      System.err.println("Output path exists!");
//      is.close();
//      os.close();
//      return 1;

    }

    System.out.println("\nKuber decryption started!\n");


    byte[] b = new byte[1024*4];

    int numBytes = 0;

    while ((numBytes = is.read(b)) > 0) {
      byte[] block = Arrays.copyOfRange(b, 0, numBytes);
      byte[] decrypted = decrypt(block);
      os.write(decrypted);
    }
    is.close();
    os.close();


    return 0;
  }



  protected byte[] decryptAES(byte[] content) {
    Config conf = new Config();
    try {
      AesCtr cip = new AesCtr(conf.getKey());
      byte[] decData = cip.decrypt(content,"hahahahahahahaahahaha");
      return decData;
    } catch (Exception e) {
      throw new MaskRuntimeException(e);
    }
  }


  protected byte[] decrypt(byte[] cipherIn) {
    Config conf = new Config();

    byte[] decrypted = new byte[cipherIn.length];

    try {
      ChaCha20 cipher = new ChaCha20(conf.getKey(), conf.getNonce(), conf.getCounter());
      cipher.decrypt(decrypted, cipherIn, cipherIn.length);
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }

    return decrypted;
  }

  public static void main( String[] args ) throws Exception {
    int returnCode = ToolRunner.run(new Decrypt(), args);
    System.exit(returnCode);
  }
}
