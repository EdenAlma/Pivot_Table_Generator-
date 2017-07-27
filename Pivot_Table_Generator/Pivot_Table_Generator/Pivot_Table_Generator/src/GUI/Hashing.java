// Created by Rodrigo
// 02/04/2017
package GUI;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class Hashing {

  public static String hashPwd(String password) {

    try {
    MessageDigest sha = MessageDigest.getInstance("SHA-256");
    String text = password;
    sha.update(text.getBytes(StandardCharsets.UTF_8));

    byte[] digest = sha.digest();

    // System.out.println(digest);
    String hashedPwd = String.format("%064x", new java.math.BigInteger(1, digest));

    return hashedPwd;

    }
    catch(Exception e){
        return "null";  // Always must return something
    }

  }

}
