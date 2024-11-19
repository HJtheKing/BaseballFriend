package com.teamjhw.sse.global.utils;

import com.teamjhw.sse.global.exception.ErrorCode;
import com.teamjhw.sse.global.exception.exceptionType.CustomJWTException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTUtil {

    @Value("${jwt.secret-key}")
    private String JWT_KEY;

    /**
     * 요청 헤더에서 accesstoken 가져옴
     * */
    public String getTokenFromHeader(HttpServletRequest request) {
        String authHeaderStr = request.getHeader("Authorization");

        // access token : Bearer(7자) + JWT 문자열
        if (authHeaderStr == null || authHeaderStr.length() < 7) {
            throw new CustomJWTException(ErrorCode.INVALID_TOKEN);
        }
        return authHeaderStr.substring(7);
    }

    /**
     * * 토큰 만료되었는지 확인
     * */
    public boolean checkTokenExpired(String token) {
            try {
                validateToken(token);
            } catch (ExpiredJwtException e) {
                return true;
            }
            return false;
    }

    /*
     * 토큰 유효성 검사 (expired 제외)
     *
     * - 토큰의 유효기간 만료는 다른 메소드로 관리함
     * */
    public Map<String, Object> validateToken(String token) {

        Map<String, Object> claim = null;

        try {
            SecretKey key = Keys.hmacShaKeyFor(JWT_KEY.getBytes("UTF-8"));

            claim = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
                        .getBody();
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (MalformedJwtException e) {
            throw new CustomJWTException(ErrorCode.MALFORMED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new CustomJWTException(ErrorCode.UNSUPPORTED_TOKEN);
        } catch (InvalidClaimException invalidClaimException) {
            throw new CustomJWTException(ErrorCode.INVALID_TOKEN);
        } catch (UnsupportedEncodingException e) {
            throw new CustomJWTException(ErrorCode.UNSUPPORTED_ENCODING);
        } catch (Exception e) {
            throw new CustomJWTException(ErrorCode.TOKEN_DEFAULT_ERROR);
        }
        return claim;
    }
}
