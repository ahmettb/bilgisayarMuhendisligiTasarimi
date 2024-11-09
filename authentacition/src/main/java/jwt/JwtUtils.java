package jwt;

import com.jobtrackingapp.authentacition.model.Role;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Log4j2
public class   JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;


    @Value("${jwt.expiration}")
    private int jwtExpirationMs;
 ;
    public String generateJwtToken(Authentication authentication, Set<Role> roles) {

            Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
            String username = loggedInUser.getName();

            List<String> roleNames = roles.stream()
                    .map(role -> role.getName().toString())
                    .collect(Collectors.toList());

            Map<String, Object> rolesClaim = new HashMap<>();
            rolesClaim.put("roles", roleNames);

            String token = Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                    .addClaims(rolesClaim)
                    .signWith(SignatureAlgorithm.HS256, jwtSecret)
                    .compact();

            return token;
        }




    public String getUserNameFromJwtToken(String token) {
        Claims claims= Jwts.parser().setSigningKey(jwtSecret)
                .parseClaimsJws(token).getBody();

        return  claims.getSubject();

    }



    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw new RuntimeException("Expired Jwt Token");
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
