package az.turingacademy.msauth.config;

import az.turingacademy.msauth.security.JwtProperties;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Getter
@Configuration
public class KeyPair {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    //@Autowired
    public KeyPair(JwtProperties jwtProperties) {
        this.privateKey = generatePrivateKey(jwtProperties.getPrivateKey());
        this.publicKey = generatePublicKey(jwtProperties.getPublicKey());
    }

    private PrivateKey generatePrivateKey(String privateKeyStr) {
        return (PrivateKey) generateKey(privateKeyStr, PKCS8EncodedKeySpec.class, true);
    }

    private PublicKey generatePublicKey(String publicKeyStr) {
        return (PublicKey) generateKey(publicKeyStr, X509EncodedKeySpec.class, false);
    }

    private <T extends KeySpec> Key generateKey(String keyStr, Class<T> keySpecClass, boolean isPrivate) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] decodedKey = Base64.getDecoder().decode(keyStr);
            T keySpec = keySpecClass.getConstructor(byte[].class).newInstance(decodedKey);
            return isPrivate ? keyFactory.generatePrivate(keySpec) : keyFactory.generatePublic(keySpec);
        } catch (Exception ex) {
            throw new RuntimeException("Error generating RSA key", ex);
        }
    }

}
