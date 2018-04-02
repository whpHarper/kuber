package com.databps.bigdaf.kuber;

import com.databps.bigdaf.kuber.algorithms.AesCtr;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import javax.crypto.KeyGenerator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.crypto.CryptoCodec;
import org.apache.hadoop.crypto.Encryptor;
import org.apache.hadoop.crypto.JceAesCtrCryptoCodec;
import org.apache.hadoop.util.ToolRunner;
import org.junit.Test;

/**
 * 加密测试
 *
 * @author merlin
 * @create 2018-03-09 上午11:45
 */
public class EncryptTest {
//  private String filename="/srv/sfd.pdf";
  private String dest="/mmm";
  private String filename="/mnt/bfyy.mkv";
//  private String filename="/Users/merlin/Documents/14-1F516152034-j.jpg";
//  private String dest="/path/test";
  private String defaultFS="hdfs://192.168.1.221:9000";

  @Test
  public void testOne() throws Exception {
    String[] args={filename,dest,defaultFS};
    System.setProperty("hadoop.home.dir", "/Users/merlin/Documents/bug");
    long start=System.currentTimeMillis();
    int returnCode = ToolRunner.run(new Encrypt(), args);

    long end=System.currentTimeMillis();
    System.out.println("加密消耗时间="+(end-start)+"毫秒 returnCode=="+returnCode);

  }

  private final byte[] material=null;

  @Test
  public void startEncrypt() throws IOException, GeneralSecurityException {
//    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//
//    keyGenerator.init(128);
//    byte[] key = keyGenerator.generateKey().getEncoded();
//    CryptoCodec cc = new JceAesCtrCryptoCodec();
//    Configuration conf=new Configuration();
//    cc.setConf(conf);
//    final byte[] newKey = new byte[key.length];
//    cc.generateSecureRandom(newKey);
//    final byte[] iv = new byte[16];
//    cc.generateSecureRandom(iv);
//    // Encryption key IV is derived from new key's IV
//    final byte[] encryptionIV = deriveIV(iv);
//    Encryptor encryptor = cc.createEncryptor();
//    encryptor.init(key, encryptionIV);
//    int keyLen = newKey.length;
//    ByteBuffer bbIn = ByteBuffer.allocateDirect(keyLen);
//    ByteBuffer bbOut = ByteBuffer.allocateDirect(keyLen);
//    bbIn.put(newKey);
//    bbIn.flip();
//    encryptor.encrypt(bbIn, bbOut);
//    bbOut.flip();
//    byte[] encryptedKey = new byte[keyLen];
//    bbOut.get(encryptedKey);
//
//    Config conf1 = new Config();
//    try {
//      AesCtr cip = new AesCtr(conf1.getKey());
//      byte[] encData = cip.encrypt("safasdfasdfasdfasf".getBytes(),encryptionIV);
//      printArray("safasdfasdfasdfasf".getBytes());
//
//      AesCtr cip2 = new AesCtr(conf1.getKey());
//      byte[] result =cip2.decrypt(encData,deriveIV(encryptionIV));
//
//      printArray(result);
//    } catch (Exception e) {
//      throw new MaskRuntimeException(e);
//    }


  }

  protected byte[] deriveIV(byte[] encryptedKeyIV) {
    byte[] rIv = new byte[encryptedKeyIV.length];
    // Do a simple XOR transformation to flip all the bits
    for (int i = 0; i < encryptedKeyIV.length; i++) {
      rIv[i] = (byte) (encryptedKeyIV[i] ^ 0xff);
    }
    return rIv;
  }

  public  void printArray(byte[] arr)
  {
    for(int a = 0; a<arr.length; a++)
      System.out.print(arr[a] + " ");
    System.out.println();
  }

  @Test
  public void encrypt2() throws Exception {
    Config config=new Config();
    AesCtr ctr=new AesCtr(config.getKey());
    byte[] encData = ctr.encrypt("safasdfasdfasdfasf".getBytes(),"password");
    printArray("safasdfasdfasdfasf".getBytes());
    byte[] decData=ctr.decrypt(encData,"password");
    printArray(decData);
  }
}
