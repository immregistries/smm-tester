package org.immregistries.hart;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyPairCreator {
  // algorithm, eg. "RSA"
  // keySize,   eg. 2048
  public static void create(String algorithm, int keySize, String privateKeyPath, String publicKeyPath) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
    //Creating KeyPair generator object
    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);

    //Initializing the KeyPairGenerator
    keyPairGen.initialize(keySize);

    //Generating the pair of keys
    KeyPair pair = keyPairGen.generateKeyPair();

    //Getting the private key from the key pair
    PrivateKey privKey = pair.getPrivate();

    //Getting the public key from the key pair
    PublicKey publicKey = pair.getPublic();

    {
      FileOutputStream out = new FileOutputStream(privateKeyPath);
      out.write(privKey.getEncoded());
      out.close();
    }
    {
      FileOutputStream out = new FileOutputStream(publicKeyPath);
      out.write(publicKey.getEncoded());
      out.close();
    }
  }
}
