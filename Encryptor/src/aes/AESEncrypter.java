package aes;

import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESEncrypter {
	
    private static final byte[] SALT = {
        (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
        (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private Cipher ecipher;
    private Cipher dcipher;
   
    AESEncrypter(String passPhrase) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
 
        ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, secret);
       
        dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = ecipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
        dcipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
    }

    public byte[] encryptFileBuffered(byte[] encrypt) throws Exception {
        //byte[] bytes = encrypt.getBytes("UTF8");
        byte[] encrypted = encrypt(encrypt);
        return Base64.encodeBase64(encrypted);
    }
    
    private byte[] encrypt(byte[] plain) throws Exception {
        return ecipher.doFinal(plain);
    }
 
    public byte[] decryptFileBuffered(byte[] encrypt) throws Exception {
        byte[] bytes = Base64.decodeBase64(encrypt);
        return decrypt(bytes);
        //return new String(decrypted, "UTF8");
    }
 
    private byte[] decrypt(byte[] encrypt) throws Exception {
        return dcipher.doFinal(encrypt);
    }
    
    public static void main(String[] args) throws Exception {
    	if (args.length < 3) {
    		System.out.println("Missing parameters. Please execute the JAR file like 'java -jar <encryptor-file-name>.jar <encrypt/decrypt> <file-to-encrypt> <password>'");
    		return;
    	}

    	String mode = args[0];
        String fileName = args[1];
        String password = args[2];
        
    	FileManager fileManager = new FileManager();
    	byte[] file = fileManager.readFile(fileName);
 
        AESEncrypter encrypter = new AESEncrypter(password);

        switch (mode) {
		case "encrypt":
			// Encrypt
			byte[] encryptedFile = encrypter.encryptFileBuffered(file);
			fileManager.writeFile(encryptedFile, fileName + ".encrypted");
			
			// Decrypt
			byte[] testDecryptedFile = encrypter.decryptFileBuffered(encryptedFile);
			fileManager.writeFile(testDecryptedFile, fileName + ".decrypted");
			
			break;

		case "decrypt":
			try {
				byte[] decryptedFile = encrypter.decryptFileBuffered(file);
				fileManager.writeFile(decryptedFile, fileName + ".decrypted");
			} catch (BadPaddingException bpe) {
				System.out.println("Incorrect password");
			}
			break;
			
		default:
			System.out.println("The mode is not correct.");
			break;
		}

    }
    
}