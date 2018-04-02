package com.databps.bigdaf.kuber.algorithms;

/**
 * @author merlin
 * @create 2018-03-22 下午5:06
 */
public abstract class Cipher2
{
  /**
   * Default constructor.
   * @throws Exception
   */
  public Cipher2() throws Exception
  {
  }


  /**
   * Encrypt input data.
   * @param data Input data.
   * @return Encryption result.
   * @throws Exception
   */
  public abstract byte[] encrypt(byte[] data) throws Exception;


  /**
   * Decrypt input data.
   * @param data Input data.
   * @return Decryption result.
   * @throws Exception
   */
  public abstract byte[] decrypt(byte[] data) throws Exception;


  /**
   * Decrypt input data starting from index offset.
   * @param data Input data.
   * @param offset Starting index for decryption.
   * @return Decryption result.
   * @throws Exception
   */
  public abstract byte[] decrypt(byte[] data, int offset) throws Exception;
}
