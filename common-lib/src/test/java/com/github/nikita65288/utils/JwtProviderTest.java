package com.github.nikita65288.utils;

import com.github.nikita65288.exception.FFIllegalStateException;
import com.github.nikita65288.jwt.JwtClaimNames;
import com.github.nikita65288.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtProviderTest {

    private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    private static final long EXPIRATION_MS = 3600000; // 1 час

    private JwtProvider fullProvider;
    private JwtProvider gatewayProvider;

    @BeforeEach
    void setUp() {
        fullProvider = new JwtProvider(SECRET, EXPIRATION_MS);
        gatewayProvider = new JwtProvider(SECRET);
    }

    @Test
    void shouldGenerateAndValidateTokenSuccessfully() {
        Map<String, Object> claims = Map.of(JwtClaimNames.USER_ID, 12345L);
        String subject = "testUser";

        String token = fullProvider.generateToken(claims, subject);

        assertNotNull(token);
        assertTrue(fullProvider.validateToken(token), "Токен должен быть валидным");
    }

    @Test
    void shouldExtractGenericClaimsCorrectly() {
        Map<String, Object> claims = Map.of(JwtClaimNames.USER_ID, 999L);
        String token = fullProvider.generateToken(claims, "adminUser");

        Long extractedId = gatewayProvider.getClaim(token, JwtClaimNames.USER_ID, Long.class);
        String extractedSubject = gatewayProvider.getClaim(token, Claims.SUBJECT, String.class);

        assertEquals(999L, extractedId, "Извлеченный ID должен совпадать и быть типа Long");
        assertEquals("adminUser", extractedSubject, "Subject должен совпадать");
    }

    @Test
    void gatewayProviderShouldThrowExceptionOnGenerate() {
        FFIllegalStateException exception = assertThrows(
                FFIllegalStateException.class,
                () -> gatewayProvider.generateToken(Map.of(), "user")
        );

        assertTrue(exception.getMessage().contains("Default expiration is not configured"));
    }

    @Test
    void shouldFailValidationForInvalidToken() {
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.invalid.token";

        assertFalse(fullProvider.validateToken(invalidToken), "Некорректный токен не должен проходить валидацию");
    }

    @Test
    void shouldFailValidationForExpiredToken() {
        JwtProvider expiredProvider = new JwtProvider(SECRET, -1000L);
        String expiredToken = expiredProvider.generateToken("user");

        assertFalse(expiredProvider.validateToken(expiredToken), "Истекший токен не должен проходить валидацию");
    }
}