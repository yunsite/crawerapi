package com.elwg.crawer.weibo;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Hex;

public class BigIntegerRSA {
    public static String SINA_PK = "EB2A38568661887FA180BDDB5CABD5F21C7BFD59C090CB2D24"
        + "5A87AC253062882729293E5506350508E7F9AA3BB77F4333231490F915F6D63C55FE2F08A49B353F444AD39"
        + "93CACC02DB784ABBB8E42A9B1BBFFFB38BE18D78E87A0E41B9B8F73A928EE0CCEE"
        + "1F6739884B9777E4FE9E88A1BBE495927AC4A799B3181D6442443";

    public String servertime = "1363072762";
    public String nonce = "YD8ET5";
    public String passwd = "yys19910527";
    public String rsaCrypt(String modeHex, String exponentHex, String messageg) throws IllegalBlockSizeException,
           BadPaddingException, NoSuchAlgorithmException,
           InvalidKeySpecException, NoSuchPaddingException,
           InvalidKeyException, UnsupportedEncodingException {
               KeyFactory factory = KeyFactory.getInstance("RSA");

               BigInteger m = new BigInteger(modeHex, 16); /* public exponent */
               BigInteger e = new BigInteger(exponentHex, 16); /* modulus */
               RSAPublicKeySpec spec = new RSAPublicKeySpec(m, e);

               RSAPublicKey pub = (RSAPublicKey) factory.generatePublic(spec);
               Cipher enc = Cipher.getInstance("RSA");
               enc.init(Cipher.ENCRYPT_MODE, pub);

               byte[] encryptedContentKey = enc.doFinal(messageg.getBytes("GB2312"));

               return new String(Hex.encodeHex(encryptedContentKey));
    }

    /*
     * For testing
	public static void main(String args[]){
        try{
        BigIntegerRSA b = new BigIntegerRSA();
        System.out.println(b.rsaCrypt(b.SINA_PK, "10001", b.servertime + "\t" + b.nonce + "\n" + b.passwd));
        System.out.println(b.rsaCrypt(b.SINA_PK, "10001", b.servertime + "\t" + b.nonce + "\n" + b.passwd));
        }catch(Exception e){
        }
    }*/
}
