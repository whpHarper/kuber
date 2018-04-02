package com.databps.bigdaf.kuber.algorithms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author merlin
 * @create 2018-03-22 下午5:06
 */
public class AesCtr extends JavaxCipher
{
  /**
   * Secure random generator.
   */
  protected SecureRandom m_secureRandom;

  /**
   * Number of bytes in a block, which is constant for AES.
   */
  protected static int BLOCK_SIZE_BYTES = 16;

  /**
   * Number of bits in a block, which is constant for AES.
   */
  protected static int BLOCK_SIZE_BITS = 128;

  /**
   * Number of bytes in key.
   */
  protected int KEY_SIZE_BYTES;


  /**
   * Class constructor. Creates a Javax.Crypto.Cipher instance with AES in CTR<br>
   * mode, without any padding.
   * @param key Input key for the cipher. Should be 16, 24, or 32 bytes long
   * @throws Exception Throws exception if key length is not 16, 24, or 32 bytes.<br>
   *       May throw exception based on Javax.Crypto classes.
   */
  public AesCtr(byte[] key) throws Exception
  {
    //use default constructor for cipher.Cipher
    super();

    //check if input key is ok
    if(key.length != 16 && key.length != 24 && key.length != 32)
    {
      throw new Exception("Key length should be 16, 24, or 32 bytes long");
    }

    //set key length
    KEY_SIZE_BYTES = key.length;

    //create secret key spec instance
    m_keySpec = new SecretKeySpec(key, "AES");

    //create cipher instance
    m_cipher = javax.crypto.Cipher.getInstance("AES/CTR/NoPadding");

    //create secure random number generator instance
    m_secureRandom = new SecureRandom();
  }




  /**
   * Encrypts input data with AES CTR mode.
   * @param data Input byte array.
   * @return Encryption result.
   * @throws Exception Throws exception if there is no data to encrypt.<br>
   *       May throw exception based on Javax.Crypto.Cipher class
   */
  public byte[] encrypt(byte[] data) throws Exception
  {
    //check if there is data to encrypt
    if(data.length == 0)
    {
      throw new Exception("No data to encrypt");
    }

    //create iv
    byte[] iv = new byte[BLOCK_SIZE_BYTES];
    byte[] randomNumber = (new BigInteger(BLOCK_SIZE_BITS, m_secureRandom)).toByteArray();
    int a;
    for(a = 0; a<randomNumber.length && a<BLOCK_SIZE_BYTES; a++)
      iv[a] = randomNumber[a];
    for(; a<BLOCK_SIZE_BYTES; a++)
      iv[a] = 0;

    //init cipher instance
    m_cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, m_keySpec, new IvParameterSpec(iv));

    //return concatenation of iv + encrypted data
    return ArrayUtils.addAll(iv, m_cipher.doFinal(data));
  }


  public byte[] encrypt(byte[] data,byte[] iv) throws Exception
  {
    //check if there is data to encrypt
    if(data.length == 0)
    {
      throw new Exception("No data to encrypt");
    }


    //init cipher instance
    m_cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, m_keySpec, new IvParameterSpec(iv));

    //return concatenation of iv + encrypted data
    return ArrayUtils.addAll(iv, m_cipher.doFinal(data));
  }


  /**
   * Decrypts input data with AES CTR mode
   * @param data Input byte array.
   * @return Decryption result.
   * @throws Exception Throws exception if there is no data to decrypt.<br>
   *       May throw exception based on Javax.Crypto.Cipher class.
   */
  public byte[] decrypt(byte[] data) throws Exception
  {
    //call overriden function with offset = 0
    return decrypt(data, 0);
  }


  /**
   * Decrypts input data starting and including the offset index position<br>
   * with AES CTR mode.
   * @param data Input byte array.
   * @param offset Offset to start decryption.
   * @return Decryption result.
   * @throws Exception Throws exception if there is no data to decrypt.<br>
   *       Throws exception if offset is invalid.<br>
   *       May throw exception based on Javax.Crypto.Cipher class.
   */
  public byte[] decrypt(byte[] data, int offset) throws Exception
  {
    //check if there is data to decrypt after the offset and iv
    if(data.length <= BLOCK_SIZE_BYTES + offset)
    {
      throw new Exception("No data to decrypt");
    }

    //get iv value from the beggining of data
    byte[] iv = new byte[BLOCK_SIZE_BYTES];
    System.arraycopy(data, offset, iv, 0, BLOCK_SIZE_BYTES);

    //init cipher instance
    m_cipher.init(javax.crypto.Cipher.DECRYPT_MODE, m_keySpec, new IvParameterSpec(iv));

    //return decrypted value
    return m_cipher.doFinal(data, (BLOCK_SIZE_BYTES + offset), data.length - (BLOCK_SIZE_BYTES + offset));
  }

  public byte[] decrypt(byte[] data, int offset,byte[] iv) throws Exception
  {
    //check if there is data to decrypt after the offset and iv
    if(data.length <= BLOCK_SIZE_BYTES + offset)
    {
      throw new Exception("No data to decrypt");
    }

    //init cipher instance
    m_cipher.init(javax.crypto.Cipher.DECRYPT_MODE, m_keySpec, new IvParameterSpec(iv));

    //return decrypted value
    return m_cipher.doFinal(data, (BLOCK_SIZE_BYTES + offset), data.length - (BLOCK_SIZE_BYTES + offset));
  }


  public byte[] encrypt(byte[] input, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
    SecureRandom rando = SecureRandom.getInstance("SHA1PRNG");
    //generate IV for AES-CTR mode sets the counter to random value from secure random
    byte[] IV = new byte[16];
    rando.nextBytes(IV);

    //generate eSalt for use with password derived key AES, to be used with PBKDF2WithHmacSHA1
    byte[] eSalt = new byte[20];
    rando.nextBytes(eSalt);

    //generate hSalt for use with password derived key for HMAC, to be used with PBKDF2WithHmacSHA1
    byte[] hSalt = new byte[20];
    rando.nextBytes(hSalt);

    SecretKeyFactory fact = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

    //generate eKey from eSalt and password, key to be used for AES-128-CTR mode, computed with PBKDF2WithHmacSHA1
    KeySpec eKS = new PBEKeySpec(password.toCharArray(), eSalt, 10000, 128);
    SecretKey eS = fact.generateSecret(eKS);
    Key eK = new SecretKeySpec(eS.getEncoded(), "AES");
    //encrypt plaintext bytes in AES-128-CTR with eKey and IV
    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, eK, new IvParameterSpec(IV));
    byte[] eMessage = cipher.doFinal(input);

    // we compute the HMACSHA256 on the ciphertext according to RFC7366 (Encrypt then MAC)
    //generate hKey from hSalt and password, key to be used for HMACSHA256, computed with PBKDF2WithHmacSHA1

    KeySpec hKS = new PBEKeySpec(password.toCharArray(), hSalt, 10000, 160);
    SecretKey hS = fact.generateSecret(hKS);
    Key hK = new SecretKeySpec(hS.getEncoded(), "HMACSHA256");
    Mac mac = Mac.getInstance("HMACSHA256");
    mac.init(hK);
    byte[] hMac = mac.doFinal(eMessage);

    //now we put all into new byte[]

    byte[] output = new byte[16 + 40 + eMessage.length + 32];
    System.arraycopy(IV, 0, output, 0, 16);
    System.arraycopy(eSalt, 0, output, 16, 20);
    System.arraycopy(hSalt, 0, output, 36, 20);
    System.arraycopy(eMessage, 0, output, 56, eMessage.length);
    System.arraycopy(hMac, 0, output, 56 + eMessage.length, 32);

    return output;
  }

  public byte[] decrypt(byte[] input, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException{
    //recover IV
    byte[] IV = Arrays.copyOfRange(input, 0, 16);

    //recover eSalt
    byte[] eSalt = Arrays.copyOfRange(input, 16, 36);

    //recover hSalt
    byte[] hSalt = Arrays.copyOfRange(input, 36, 56);

    //recover eMessage
    byte[] eMessage = Arrays.copyOfRange(input, 56, input.length - 32);

    //recover hmac
    byte[] hMac = Arrays.copyOfRange(input, input.length -32, input.length);

    SecretKeyFactory fact = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

    //first we check to see if the HMAC of the input matches the recomputed HMAC
    KeySpec rhKS = new PBEKeySpec(password.toCharArray(),hSalt, 10000, 160);
    SecretKey rHS = fact.generateSecret(rhKS);
    Key rhK = new SecretKeySpec(rHS.getEncoded(), "HMACSHA256");
    Mac rMac = Mac.getInstance("HMACSHA256");
    rMac.init(rhK);
    byte [] rhMac = rMac.doFinal(eMessage);
    byte [] pMessage = null;

    boolean sameHash = true;
    for(int i =0; i < rhMac.length && sameHash == true; i++){
      if(hMac[i] != rhMac[i]){
        sameHash = false;
      }
    }
    if(sameHash == true){
      //generate eKey from eSalt and password, key to be used for AES-128-CTR mode, computed with PBKDF2WithHmacSHA1
      KeySpec eKS = new PBEKeySpec(password.toCharArray(), eSalt, 10000, 128);
      SecretKey eS = fact.generateSecret(eKS);
      Key eK = new SecretKeySpec(eS.getEncoded(), "AES");
      //decrypt plaintext bytes in AES-128-CTR with eKey and IV
      Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
      cipher.init(Cipher.DECRYPT_MODE, eK, new IvParameterSpec(IV));
      pMessage = cipher.doFinal(eMessage);
      return pMessage;
    }else {
      //lame error message but didn't want to write custom errors just yet.
      System.err.println("Error cannot decrypt, SHA256 of Ciphertext and Recomputed Ciphertext do not match, Data has been modified or wrong password supplied.");
    }
    return pMessage;


  }


  public  byte[] decrypt2(byte[] input, byte[] keyBytes) throws Exception{
    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

    ByteArrayOutputStream byteArrayOutputStream;

    byte[] ivByte = new byte[16];
    System.arraycopy(input, 0, ivByte, 0, 16);
    input = Arrays.copyOfRange(input, 16, input.length);

    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivByte));
    byteArrayOutputStream = new ByteArrayOutputStream();
    CipherOutputStream cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, cipher);
    cipherOutputStream.write(input);
    cipherOutputStream.close();

    byte[] decryptedByte = removePadding(byteArrayOutputStream.toByteArray());


    return decryptedByte;
  }

  public static byte[] removePadding(byte[] input) {
    int res = input[input.length - 1];
    byte[] result = new byte[input.length - res];

    System.arraycopy(input, 0, result, 0, result.length);
    return result;
  }

  public static byte[] addPadding(byte[] input) {
    int res = 16 - input.length % 16;
    byte[] result = new byte[res];

    Arrays.fill(result,(byte) res);
    return ArrayUtils.addAll(input, result);
  }

  public static byte[] generateIV() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] newSeed = secureRandom.generateSeed(16);
    secureRandom.setSeed(newSeed);

    byte[] byteIV = new byte[16];
    secureRandom.nextBytes(byteIV);

    return byteIV;
  }


}