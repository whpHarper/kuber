package com.databps.bigdaf.kuber;

import org.apache.hadoop.util.ToolRunner;
import org.junit.Test;

/**
 * 加密测试
 *
 * @author merlin
 * @create 2018-03-09 上午11:45
 */
public class EncryptKmsTest {
  private String filename="/home/payment.csv";
  private String dest="/wwr";
  private String defaultFS="hdfs://192.168.1.221:9000";

  @Test
  public void testOne() throws Exception {
    String[] args={filename,dest,defaultFS};
//    System.setProperty("hadoop.home.dir", "/Users/merlin/Documents/bug");
    long start=System.currentTimeMillis();
    int returnCode = ToolRunner.run(new EncryptKms(), args);

    long end=System.currentTimeMillis();
    System.out.println("加密消耗时间="+(end-start)+"毫秒 returnCode=="+returnCode);

  }

}
