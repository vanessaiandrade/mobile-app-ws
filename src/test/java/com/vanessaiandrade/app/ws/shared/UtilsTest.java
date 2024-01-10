package com.vanessaiandrade.app.ws.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

	@Autowired
	Utils utils;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGenerateUserId() {

		String userId = utils.generateUserId(30);
		String userIdTwo = utils.generateUserId(30);

		assertNotNull(userId);
		assertNotNull(userIdTwo);

		assertTrue(userId.length() == 30);
		assertTrue(!userId.equalsIgnoreCase(userIdTwo));
	}

	@Test
	void testHasTokenNotExpired() {

		String token = utils.generateEmailVerificationToken("d5sf4sdsadf");
		assertNotNull(token);

		boolean hasTokenExpired = Utils.hasTokenExpired(token);
		assertFalse(hasTokenExpired);

	}

	@Test
	void testHasTokenExpired() {

		String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2YW5lc3NhMkB0ZXN0LmNvbSIsImV4cCI6MTcwMDY5NTA2Mn0.ATo3Nvdivd4C4Oxo0BBp9lqE5CJ07FDEIND5zv0fS3tI3o_m6dZpXv6eeFeRUhtoIp9Y9UcVh0_O_q--k-hXfA";

		boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);
		assertTrue(hasTokenExpired);

	}
}
