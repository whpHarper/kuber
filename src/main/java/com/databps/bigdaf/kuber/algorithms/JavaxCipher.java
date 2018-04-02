package com.databps.bigdaf.kuber.algorithms;


/**
 * @author merlin
 * @create 2018-03-22 下午5:05
 */
public abstract class JavaxCipher extends Cipher2
{
  /**
   * Javax crypto instance.
   */
  protected javax.crypto.Cipher m_cipher;

  /**
   * Javax secret key spec instance.
   */
  protected javax.crypto.spec.SecretKeySpec m_keySpec;


  /**
   * Default constructor.
   * @throws Exception
   */
  public JavaxCipher() throws Exception
  {

  }
}