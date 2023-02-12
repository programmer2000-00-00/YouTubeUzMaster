package com.company.YouTubeUz.util;

import com.company.YouTubeUz.dto.JwtDTO;
import com.company.YouTubeUz.dto.ProfileJwtDTO;
import com.company.YouTubeUz.enums.ProfileRole;
import com.company.YouTubeUz.exp.AppForbiddenException;
import com.company.YouTubeUz.exp.TokenNotValidException;
import io.jsonwebtoken.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtil {
    private final static String secretKey = "kalitso'z";

    public static String encode(Integer id, ProfileRole role) { // 3 -> dsasdasda.asdasdasd.asdasdsa
        return doEncode(id, role, 600);
    }

    public static String encode(Integer id) { // 3 -> dsasdasda.asdasdasd.asdasdsa
        return doEncode(id, null, 300);
    }

    public static String doEncode(Integer id, ProfileRole role, long minute) { // 3 -> dsasdasda.asdasdasd.asdasdsa
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setSubject(String.valueOf(id));
        jwtBuilder.setIssuedAt(new Date()); // 10:15
        jwtBuilder.signWith(SignatureAlgorithm.HS256, secretKey);
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + (minute * 60 * 1000)));
        jwtBuilder.setIssuer("mazgi production");

        if (role != null) {
            jwtBuilder.claim("role", role);
        }
        String jwt = jwtBuilder.compact();
        return jwt;
    }

    public static JwtDTO decodeJwtDto(String jwt) {
        JwtParser jwtParser = Jwts.parser();

        jwtParser.setSigningKey(secretKey);
        Jws<Claims> jws = jwtParser.parseClaimsJws(jwt);

        Claims claims = jws.getBody();
        String id = claims.getSubject();
        String email = String.valueOf(claims.get("email"));
        return new JwtDTO( email,Integer.parseInt(id));
    }

    public static String doEncode(Integer id, long minute,String email) { // 3 -> dsasdasda.asdasdasd.asdasdsa

        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setSubject(String.valueOf(id));
        jwtBuilder.setIssuedAt(new Date()); // 10:15
        jwtBuilder.signWith(SignatureAlgorithm.HS256, secretKey);
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + (minute * 60 * 1000)));
        jwtBuilder.setIssuer("mazgi production");

        if (email != null) jwtBuilder.claim("email", email);

        return jwtBuilder.compact();
    }


    public static ProfileJwtDTO decode(String jwt) {
        JwtParser jwtParser = Jwts.parser();

        jwtParser.setSigningKey(secretKey);
        Jws<Claims> jws = jwtParser.parseClaimsJws(jwt);

        Claims claims = jws.getBody();
        String id = claims.getSubject();
        String role = String.valueOf(claims.get("role"));

        return new ProfileJwtDTO(Integer.parseInt(id), ProfileRole.valueOf(role));
    }

    public static Integer decodeAndGetId(String jwt) {
        JwtParser jwtParser = Jwts.parser();

        jwtParser.setSigningKey(secretKey);
        Jws<Claims> jws = jwtParser.parseClaimsJws(jwt);

        Claims claims = jws.getBody();
        String id = claims.getSubject();

        return Integer.parseInt(id);
    }

    public static Integer getIdFromHeader(HttpServletRequest request, ProfileRole... requiredRoles) {
        try {
            ProfileJwtDTO dto = (ProfileJwtDTO) request.getAttribute("profileJwtDto");

            if (requiredRoles == null || requiredRoles.length == 0) {
                return dto.getId();
            }
            for (ProfileRole role : requiredRoles) {
                if (role.equals(dto.getRole())) {
                    return dto.getId();
                }
            }
        } catch (RuntimeException e) {

            throw new TokenNotValidException("Not Authorized");
        }
        throw new AppForbiddenException("Not Access");
    }

    public static ProfileJwtDTO getProfileFromHeader(HttpServletRequest request, ProfileRole... requiredRoles) {
        try {
            ProfileJwtDTO dto = (ProfileJwtDTO) request.getAttribute("profileJwtDto");
            if (requiredRoles == null || requiredRoles.length == 0) {
                return dto;
            }
            for (ProfileRole role : requiredRoles) {
                if (role.equals(dto.getRole())) {
                    return dto;
                }
            }
        } catch (RuntimeException e) {
            throw new TokenNotValidException("Not Authorized");
        }
        throw new AppForbiddenException("Not Access");
    }
}
