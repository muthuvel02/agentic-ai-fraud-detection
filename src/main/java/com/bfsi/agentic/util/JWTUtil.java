package com.bfsi.agentic.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class JWTUtil {

    public static final String JTI = "jti";
    public static final String IAT = "iat";
    public static final String EXP = "exp";
    public static final String ISS = "iss";
    public static final String JWT_SECRET = "12345";
    public static final int JWT_EXP_MINUTES = 30;

    public String generateToken(Map<String, String> claimDetails){
        Date now = new Date();

        Date expiryDate = new Date(now.getTime() + JWT_EXP_MINUTES * 60000);

        String jwtId = UUID.randomUUID().toString();
        Claims claims = Jwts.claims();
        for (Map.Entry<String, String> entry : claimDetails.entrySet()) {
            claims.put(entry.getKey(), entry.getValue());
        }
        claims.put(JTI, jwtId);
        claims.put(ISS, "TraceGuardX");
        claims.put(IAT, now.getTime());
        claims.put(EXP, expiryDate.getTime());
        String token = Jwts.builder().setId(jwtId)
//                 .setPayload()
                .setIssuedAt(new Date()).setClaims(claims).setExpiration(expiryDate).signWith(SignatureAlgorithm.HS256, JWT_SECRET).compact();

        return token;
    }

    public Claims getClaims(String jwt) {
        try {
            return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt).getBody();
        } catch (SignatureException ex) {
            log.error("INVALID_JWT_SIGNATURE");
        } catch (MalformedJwtException ex) {
            log.error("INVALID_JWT_SIGNATURE");
        } catch (ExpiredJwtException ex) {
            log.error("INVALID_JWT_SIGNATURE");
        } catch (UnsupportedJwtException ex) {
            log.error("INVALID_JWT_SIGNATURE");
        } catch (IllegalArgumentException ex) {
            log.error("INVALID_JWT_SIGNATURE");
        }
        return null;
    }

}
