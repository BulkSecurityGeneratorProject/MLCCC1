package org.mlccc.cm.infrastructure.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Paths;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

public class MlcccCipher {
    private static String keyFilePath;
    private static String ivFilePath;

    private static SecretKey key;
    private static byte[] IV;

    public static void init(String keyPath, String ivPath) {
        keyFilePath = keyPath;
        ivFilePath = ivPath;
        if(!Paths.get(keyFilePath).toFile().exists() || !Paths.get(ivFilePath).toFile().exists()){
            try {
                generateKeyIVParameter();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        key = readKey();
        IV = readIV();
    }

    private static void generateKeyIVParameter() throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);

        // Generate Key
        SecretKey key = keyGenerator.generateKey();
        writeKey(key, keyFilePath, true);

        // Generating IV.
        byte[] IV = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);

        writeIV(IV, ivFilePath, true);
    }

    public static String encrypt(String inputString) throws Exception{
        byte[] cipherText = encrypt(inputString.getBytes(),key, IV);
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String inputString) throws Exception {
        return decrypt(Base64.getDecoder().decode(inputString.getBytes()), key,  IV);
    }

    private static byte[] encrypt (byte[] plaintext,SecretKey key, byte[] IV) throws Exception {
        //Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        //Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        //Create IvParameterSpec
        IvParameterSpec ivSpec = new IvParameterSpec(IV);

        //Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        //Perform Encryption
        byte[] cipherText = cipher.doFinal(plaintext);

        return cipherText;
    }

    private static String decrypt (byte[] cipherText, SecretKey key,byte[] IV) throws Exception {
        //Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        //Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        //Create IvParameterSpec
        IvParameterSpec ivSpec = new IvParameterSpec(IV);

        //Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        //Perform Decryption
        byte[] decryptedText = cipher.doFinal(cipherText);

        return new String(decryptedText);
    }

    private static void writeKey(Key key, String path, boolean overwrite){
        byte[] bytes = key.getEncoded();
        String output = Base64.getEncoder().withoutPadding().encodeToString(bytes);
        writeToFile(output.getBytes(), path, overwrite);
    }

    private static SecretKey readKey(){
        byte[] bytes = readFromFile(keyFilePath);

        byte[] decoded = Base64.getDecoder().decode(bytes);
        SecretKey aesKey = new SecretKeySpec(decoded, "AES");
        return aesKey;
    }

    private static void writeIV(byte[] IV, String path, boolean overwrite){
        writeToFile(Base64.getEncoder().encode(IV), path, overwrite);
    }

    private static byte[] readIV(){
        byte[] bytes = readFromFile(ivFilePath);

        byte[] decoded = Base64.getDecoder().decode(bytes);

        return decoded;
    }

    private static byte[] readFromFile(String path){
        File file = new File(path);
        if(file.isDirectory()){
            String msg = "Key file '"+file.getPath()+"' is not a file but a directory";
            throw new IllegalStateException(msg);
        }
        if(!file.exists()){
            String msg = "Key file '"+file.getPath()+"' does not exist";
            throw new IllegalStateException(msg);
        }
        if(!file.isFile()){
            String msg = "Key file '"+file.getPath()+"' is not a file";
            throw new IllegalStateException(msg);
        }
        if(!file.canRead()){
            String msg = "Cannot read from Key file '"+file.getPath()+"'";
            throw new IllegalStateException(msg);
        }
        byte[] bytes = new byte[(int)file.length()];
        InputStream in = null;
        try{
            in = new FileInputStream(file);
            in.read(bytes);
        } catch (FileNotFoundException e) {
            String msg = "The Key file '"+file.getPath()+"' does not exists";
            throw new IllegalStateException(msg, e);
        } catch (IOException e) {
            String msg = "An unknown input error occured while reading Key of "+bytes.length+" bytes from '"+file.getPath()+"'";
            throw new IllegalStateException(msg, e);
        }finally{
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    String msg = "Unable to gracefully close InputStream from Key file '"+file.getPath()+"'";
                    System.out.println("Unable to gracefully close OutputStream to Key file '"+file.getPath()+"'");
                }
            }
        }
        return bytes;
    }
    private static void writeToFile(byte[] input, String path, boolean overwrite){
        File file = new File(path);
        if(file.isDirectory()){
            String msg = "Key file '"+file.getPath()+"' is not a file but a directory";
            throw new IllegalStateException(msg);
        }
        if(file.exists()){
            if(overwrite && !file.delete()){
                String msg = "Unable to delete existing Key file '"+file.getPath()+"'";
                throw new IllegalStateException(msg);
            }else if(overwrite){
            }else{
                String msg = "Key file '"+file.getPath()+"' already exists";
                throw new IllegalStateException(msg);
            }
        }else{
            try {
                if(!file.createNewFile()){
                    throw new IOException();
                }else{
                }
            } catch (IOException e) {
                String msg = "Unable to create new Key file '"+file.getPath()+"'";
                throw new IllegalStateException(msg, e);
            }
        }
        if(!file.exists() && file.isFile() && file.canWrite()){
            String msg = "Cannot write to Key file '"+file.getPath()+"'";
            throw new IllegalStateException(msg);
        }

        OutputStream out = null;
        try{
            out = new FileOutputStream(file);
            out.write(input);
        } catch (FileNotFoundException e) {
            String msg = "The Key file '"+file.getPath()+"' does not exists";
            throw new IllegalStateException(msg, e);
        } catch (IOException e) {
            String msg = "An unknown output error occured while writing Key of "+input.length+" bytes to '"+file.getPath()+"'";
            throw new IllegalStateException(msg, e);
        }finally{
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    System.out.println("Unable to gracefully close OutputStream to Key file '"+file.getPath()+"'");
                }
            }
        }
    }
}
