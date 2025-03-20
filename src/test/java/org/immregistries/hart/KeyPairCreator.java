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
  public int keySize = 2048;
  public String algorithm = "RSA";
  private PrivateKey privateKey;
  private PublicKey publicKey;
  private KeyPairGenerator keyPairGen = null;

  public KeyPairCreator() {
    generateKeyPairs();
  }

  public KeyPairCreator(String algorithm, int keySize) throws NoSuchAlgorithmException {
    this.keySize = keySize;
    this.algorithm = algorithm;
    generateKeyPairs();
  }

  private void generateKeyPairs() {
    //Creating KeyPair generator object
    try {
      this.keyPairGen = KeyPairGenerator.getInstance(this.algorithm);
    } catch (NoSuchAlgorithmException nse) {
      return;
    }

    //Initializing the KeyPairGenerator
    this.keyPairGen.initialize(this.keySize);

    //Generating the pair of keys
    KeyPair pair = this.keyPairGen.generateKeyPair();

    //Getting the private key from the key pair
    this.privateKey = pair.getPrivate();

    //Getting the public key from the key pair
    this.publicKey = pair.getPublic();
  }

  // Static key creation method - requires output files on disk
  // algorithm, eg. "RSA"
  // keySize,   eg. 2048
  public static void create(String algorithm, int keySize, String privateKeyPath, String publicKeyPath)
      throws FileNotFoundException, IOException, NoSuchAlgorithmException {
    //Creating KeyPair generator object
    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);

    //Initializing the KeyPairGenerator
    keyPairGen.initialize(keySize);

    //Generating the pair of keys
    KeyPair pair = keyPairGen.generateKeyPair();

    //Getting the private key from the key pair
    PrivateKey privateKey = pair.getPrivate();

    //Getting the public key from the key pair
    PublicKey publicKey = pair.getPublic();
    {
      FileOutputStream out = new FileOutputStream(privateKeyPath);
      out.write(privateKey.getEncoded());
      out.close();
    }
    {
      FileOutputStream out = new FileOutputStream(publicKeyPath);
      out.write(publicKey.getEncoded());
      out.close();
    }
  }

  PrivateKey getPrivateKey() {
    return this.privateKey;
  }

  PublicKey getPublicKey() {
    return this.publicKey;
  }

  int getKeySize() {
    return this.keySize;
  }

  void setKeySize(int newKeySize) {
    this.keySize = newKeySize;
    generateKeyPairs();
  }

  String getAlgorithm() {
    return this.algorithm;
  }

  void setAlgorithm(String newAlgorithm) throws NoSuchAlgorithmException {
    this.keyPairGen = KeyPairGenerator.getInstance(newAlgorithm);
    this.algorithm = newAlgorithm;
    generateKeyPairs();
  }
}
