package com.databps.bigdaf.kuber;

import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * @author whp 18-3-28
 */
public class StreamTest {

    private String filename="/srv/ppp.jpg";
    private String encryptPath="/mnt/ooo_encrypt.jpg";
    private String decryptPath="/mnt/ooo_decrypt.jpg";
    @Test
    public void testEncryptStream(){
        try {
            Encrypt encrypt=new Encrypt();
            File inputfile=new File(filename);
            File outputPath = new File(encryptPath);
            InputStream is = new BufferedInputStream(new FileInputStream(inputfile));
            OutputStream os= new BufferedOutputStream(new FileOutputStream(outputPath));

            byte[] read=new byte[1024*100];
            int len=0;
            while((len=is.read(read))>0)
            {
//                byte[] block= Arrays.copyOfRange(read, 0, len);
                byte[] encry=encrypt.encrypt(read);
                os.write(encry);
            }

//            is.close();
//            os.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Test
    public void testDecryptStream(){
        try{
            Decrypt decrypt=new Decrypt();
            File inputfile=new File(encryptPath);
            File outputFile=new File(decryptPath);
            InputStream is=new BufferedInputStream(new FileInputStream(inputfile));
            OutputStream os=new BufferedOutputStream(new FileOutputStream(outputFile));

            byte[] read=new byte[1024*100];
            int len=0;
            while((len=is.read(read))>0){
//                byte[] blocks=Arrays.copyOfRange(read,0,len);
                byte[] decryp=decrypt.decrypt(read);
                os.write(decryp);
            }
            is.close();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
