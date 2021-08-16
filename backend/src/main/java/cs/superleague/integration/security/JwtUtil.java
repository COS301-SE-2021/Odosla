package cs.superleague.integration.security;

import cs.superleague.user.dataclass.*;
import cs.superleague.user.dataclass.Shopper;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtUtil {

    private String SECRET_KEY = "uQmMa86HgOi6uweJ1JSftIN7TBHFDa3KVJh6kCyoJ9bwnLBqA0YoCAhMMk";

    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    public String extractUserType(String token){
        final Claims claims=extractAllClaims(token);
        return (String) claims.get("userType");
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public String generateJWTTokenShopper(Shopper shopper){
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,shopper.getShopperID(),shopper.getEmail(),UserType.SHOPPER);
    }

    public String generateJWTTokenDriver(Driver driver){
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,driver.getDriverID(),driver.getEmail(),UserType.DRIVER);
    }

    public String generateJWTTokenAdmin(Admin admin){
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,admin.getAdminID(),admin.getEmail(),UserType.ADMIN);
    }

    public String generateJWTTokenCustomer(Customer customer){
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,customer.getCustomerID(),customer.getEmail(),UserType.CUSTOMER);
    }

    private String createToken(Map<String, Object> claims, UUID userID, String email, UserType userType) {

        return Jwts.builder().setClaims(claims).setSubject(email).setId(userID.toString()).claim("userType",userType)
                .setIssuedAt(new Date(Calendar.getInstance().getTimeInMillis())).setExpiration(new Date(Calendar.getInstance().getTimeInMillis()+1000*60*60*10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, User user){
        final String email =extractEmail(token);
       final String userType =extractUserType(token);
        if(user.getAccountType()==UserType.DRIVER){
            return(email.equals(user.getEmail()) && !isTokenExpired(token) && userType.equals("DRIVER"));
        } else if(user.getAccountType()==UserType.SHOPPER){
            return(email.equals(user.getEmail()) && !isTokenExpired(token) && userType.equals("SHOPPER"));
        } else if(user.getAccountType()==UserType.ADMIN){
            return(email.equals(user.getEmail()) && !isTokenExpired(token) && userType.equals("ADMIN"));
        }else if (user.getAccountType()==UserType.CUSTOMER){
            return(email.equals(user.getEmail()) && !isTokenExpired(token) && userType.equals("CUSTOMER"));
        }
        return false;
    }

}
