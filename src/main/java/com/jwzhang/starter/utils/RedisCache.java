package com.jwzhang.starter.utils;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis工具类
 * 
 * @author zjw
 *
 */
@Component
public class RedisCache {

	@Autowired
	private StringRedisTemplate redisTemplate;

	private static RedisCache redisCache;

	/**
	 * 初始化
	 */
	@PostConstruct
	public void init() {
		redisCache = this;
		redisCache.redisTemplate = this.redisTemplate;
	}

	/**
	 * 查询key，支持模糊查询
	 *
	 * @param key key
	 */
	public static Set<String> keys(String key) {
		return redisCache.redisTemplate.keys(key);
	}

	/**
	 * 获取值
	 * 
	 * @param key key
	 */
	public static String get(String key) {
		return redisCache.redisTemplate.opsForValue().get(key);
	}

	/**
	 * 设置值
	 * 
	 * @param key key
	 * @param value value
	 */
	public static void set(String key, String value) {
		redisCache.redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 设置值，并设置过期时间
	 * 
	 * @param key key
	 * @param value value
	 * @param expire 过期时间，单位秒
	 */
	public static void set(String key, String value, Integer expire) {
		redisCache.redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
	}

	/**
	 * 删出key
	 * 
	 * @param key key
	 */
	public static void deleteKeyForValue(String key) {
		redisCache.redisTemplate.opsForValue().getOperations().delete(key);
	}

	/**
	 * 设置对象
	 * 
	 * @param key     key
	 * @param hashKey hashKey
	 * @param object  对象
	 */
	public static void hset(String key, String hashKey, Object object) {
		redisCache.redisTemplate.opsForHash().put(key, hashKey, object);
	}

	/**
	 * 设置对象
	 * 
	 * @param key     key
	 * @param hashKey hashKey
	 * @param object  对象
	 * @param expire  过期时间，单位秒
	 */
	public static void hset(String key, String hashKey, Object object, Integer expire) {
		redisCache.redisTemplate.opsForHash().put(key, hashKey, object);
		redisCache.redisTemplate.expire(key, expire, TimeUnit.SECONDS);
	}

	/**
	 * 设置HashMap
	 *
	 * @param key key
	 * @param map map值
	 */
	public static void hset(String key, HashMap<String, Object> map) {
		redisCache.redisTemplate.opsForHash().putAll(key, map);
	}

	/**
	 * key不存在时设置值
	 * 
	 * @param key key
	 * @param hashKey hashKey
	 * @param object object
	 */
	public static void hsetAbsent(String key, String hashKey, Object object) {
		redisCache.redisTemplate.opsForHash().putIfAbsent(key, hashKey, object);
	}

	/**
	 * 获取Hash值
	 *
	 * @param key key
	 * @param hashKey hashKey
	 * @return Object
	 */
	public static Object hget(String key, String hashKey) {
		return redisCache.redisTemplate.opsForHash().get(key, hashKey);
	}

	/**
	 * 获取key的所有值
	 *
	 * @param key key
	 * @return Object
	 */
	public static Object hget(String key) {
		return redisCache.redisTemplate.opsForHash().entries(key);
	}

	/**
	 * 删除key的所有值
	 *
	 * @param key key
	 */
	public static void deleteKey(String key) {
		redisCache.redisTemplate.delete(key);
	}

	/**
	 * 判断key和hasKey下是否有值
	 *
	 * @param key key
	 * @param hasKey hasKey
	 */
	public static Boolean hasHashKey(String key, String hasKey) {
		return redisCache.redisTemplate.opsForHash().hasKey(key, hasKey);
	}

	/**
	 * 判断key下是否有值
	 *
	 * @param key key
	 */
	public static Boolean hasKey(String key) {
		return redisCache.redisTemplate.hasKey(key);
	}

}