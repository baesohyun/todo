package com.inzent.todo.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inzent.todo.dto.TokenDto;
import com.inzent.todo.vo.UserVo;

@Service
public class JwtService {

	private static final String ENCRYPT_STRING = "todosecret";
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);
	private static final String DATA_KEY = "user";

	// 토큰 발급
	// public String createLoginToken(UserVo user) {
	public String createLoginToken(TokenDto user) {
		// System.out.println();
		long curTime = System.currentTimeMillis();
		return Jwts.builder().setHeaderParam("typ", "JWT").setExpiration(new Date(curTime + (1000 * 60 * 60 * 24)))
				.setIssuedAt(new Date(curTime)).claim(DATA_KEY, user)
				.signWith(SignatureAlgorithm.HS256, this.generateKey()).compact();
	}

	// key 설정
	private byte[] generateKey() {
		byte[] key = null;
		try {
			key = ENCRYPT_STRING.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Making secret Key Error :: ", e);
		}

		return key;
	}

	// 토큰이 있냐 없냐가 아니라,
	// 토큰이 유효한가 아닌가!!
	// 토큰 분해
	public UserVo getUser(String jwt) {
		Jws<Claims> claims = null;
		try {
			claims = Jwts.parser().setSigningKey(this.generateKey()).parseClaimsJws(jwt);
		} catch (Exception e) {
			LOGGER.debug(e.getMessage(), e);
		}
		return new ObjectMapper().convertValue(claims.getBody().get(DATA_KEY), UserVo.class);
	}

	// 토큰 유효
	public boolean isValidToken(String jwt) {
		try {
			Jwts.parser().setSigningKey(this.generateKey()).parseClaimsJws(jwt);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}