package com.crmservice.crmservice.infrastructure.drivens.externalservices.token;

import com.crmservice.crmservice.domain.interfaces.ITokenService;
import com.crmservice.crmservice.domain.responses.DomainClientException;
import com.crmservice.crmservice.domain.responses.DomainErrorResponse;
import dev.paseto.jpaseto.Paseto;
import dev.paseto.jpaseto.PasetoException;
import dev.paseto.jpaseto.PasetoParserBuilder;
import dev.paseto.jpaseto.PasetoV2PublicBuilder;
import dev.paseto.jpaseto.Pasetos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class PasetoTokenService implements ITokenService {

    private final Logger logger = LoggerFactory.getLogger(PasetoTokenService.class);
    private KeyPair keyPair;

    public PasetoTokenService(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    @Override
    public String create(Map<String, Object> claims) {
        Instant now = Instant.now();

        PasetoV2PublicBuilder tokenBuilder = Pasetos.V2.PUBLIC.builder()
                .setPrivateKey(this.keyPair.getPrivate())
                .setIssuedAt(now)
                .setExpiration(now.plus(3, ChronoUnit.MINUTES));

        claims.forEach(tokenBuilder::claim);

        return tokenBuilder.compact();
    }


    public Paseto validate(String token, Map<String, String> claimsRequired) throws DomainClientException {
        Paseto result;

        try {
            PasetoParserBuilder parserBuilder = Pasetos.parserBuilder()
                    .setPublicKey(this.keyPair.getPublic());

            claimsRequired.forEach(parserBuilder::require);

            result = parserBuilder.build()
                    .parse(token);
        } catch (PasetoException ex) {
            logger.error(ex.getMessage());
            throw new DomainClientException(DomainErrorResponse.INVALID_ACCESS_TOKEN);
        }

        return result;
    }
}