package com.sampak.gameapp.service;

import com.sampak.gameapp.entity.UserEntity;
import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);
    Date extractExpiration(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    Boolean validateToken(String token, UserEntity userDetails);
    String generateToken(String id);
}
