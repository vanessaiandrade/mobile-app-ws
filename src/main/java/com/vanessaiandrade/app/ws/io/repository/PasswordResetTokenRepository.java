package com.vanessaiandrade.app.ws.io.repository;

import org.springframework.data.repository.CrudRepository;

import com.vanessaiandrade.app.ws.io.entity.PasswordResetTokenEntity;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long> {

	PasswordResetTokenEntity findByToken(String token);

}
