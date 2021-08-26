package tech.crm.crmserver.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.crm.crmserver.service.TokenKeyService;

import javax.xml.bind.DatatypeConverter;
import java.security.Key;

@Component
public class JwtSigningKeyResolver extends SigningKeyResolverAdapter {

    @Autowired
    public TokenKeyService tokenKeyService;

    //search the key of this token
    @Override
    public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {

        Integer id = Integer.parseInt(claims.getId());
        String key = tokenKeyService.getById(id).getJwtKey();

        return Keys.hmacShaKeyFor(DatatypeConverter.parseBase64Binary(key));

    }

}
