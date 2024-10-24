package az.turingacademy.msauth.util;

import az.turingacademy.msauth.exception.InvalidTokenException;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    public static String extractToken(final String authorizationHeader) {
        validateAuthorizationHeader(authorizationHeader);
        return authorizationHeader.replace("Bearer ", "").trim();
    }

    public static void validateAuthorizationHeader(String authorizationHeader) {
        if (StringUtils.isBlank(authorizationHeader) || !authorizationHeader.startsWith("Bearer ") ||
                StringUtils.isBlank(authorizationHeader.replace("Bearer ", "")))
            throw new InvalidTokenException();
        System.out.println(authorizationHeader);
    }

}
