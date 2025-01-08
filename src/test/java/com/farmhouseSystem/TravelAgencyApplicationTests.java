package com.farmhouseSystem;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.HashMap;

@SpringBootTest
class TravelAgencyApplicationTests {

@Test
	void contextLoads() {
//
//	HashMap<String, Object> map = new HashMap<>();
//
//	//获取日历对象
//	Calendar instance = Calendar.getInstance();
//	//默认30S过期
//	instance.add(Calendar.SECOND, 30);
//
//	String token = JWT.create()
//			.withHeader(map)     //header,可以不写
//			.withClaim("userId", 21)    //payload
//			.withClaim("username", "Garry")   //payload
//			.withExpiresAt(instance.getTime())    //设置过期时间
//			.sign(Algorithm.HMAC256("!ISN!@#￥%"));   //签名
//
//	System.out.println(token);
	}


}
