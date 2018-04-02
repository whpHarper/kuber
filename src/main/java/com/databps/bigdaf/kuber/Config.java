package com.databps.bigdaf.kuber;

import java.io.UnsupportedEncodingException;

/**
 * @author merlin
 * @create 2018-03-08 下午5:33
 */
public class Config{

  private String key = "abcdefghijklmnopqrstuvwxyzabcdef";	//Should be 32 bits. Update as required.

  //private String key="1234567890123456";

  private String nonce = "abcdefghijkl";	//Should be 12 bits. Update as required.

  private int counter = 1;

  protected byte[] getKey() {
    //return key.getBytes(StandardCharsets.UTF_8);
    try {
      return key.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

  protected byte[] getNonce(){
    //return nonce.getBytes(StandardCharsets.UTF_8);
    return nonce.getBytes();
  }

  protected int getCounter(){
    return this.counter;
  }

  protected void setCouter(int counter){
    if(counter>0)
      this.counter=counter;
  }

  /* ToDo Methods
   * Integrating with Ranger KMS
   * newKey()
   * updateKey()
   * updateNonce()
   */
}