package az.turingacademy.msauth.util;

import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Component
public class PublicPrivateKeyUtils {

    public static KeyPairGenerator GENERATOR;
    public static KeyPair KEY_PAIR;

    static {
        try {
            GENERATOR = KeyPairGenerator.getInstance("RSA");
            GENERATOR.initialize(2048);
            KEY_PAIR = GENERATOR.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

//    private PrivateKey preparePrivateKey() {
//        try {
//            KeyFactory kf = KeyFactory.getInstance("RSA");
//            System.out.println(KEY_PAIR.getPrivate());
//            String privateKey = Base64.getEncoder().encodeToString(KEY_PAIR.getPrivate().getEncoded());
//            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().
//                    decode(privateKey));
//            return kf.generatePrivate(keySpecPKCS8);
//        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
//
//    private PublicKey preparePublicKey() {
//        try {
//            KeyFactory kf = KeyFactory.getInstance("RSA");
//            System.out.println(KEY_PAIR.getPublic());
//            String publicKey = Base64.getEncoder().encodeToString(KEY_PAIR.getPublic().getEncoded());
//            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().
//                    decode(publicKey));
//            return kf.generatePublic(keySpecX509);
//        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }

}