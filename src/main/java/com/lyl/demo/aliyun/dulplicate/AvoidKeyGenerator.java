//package com.lyl.demo.aliyun.dulplicate;
//
//
//import com.lyl.demo.aliyun.lock.CacheKeyGenerator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Component
//public class AvoidKeyGenerator {
//
//    public static final int EXPIRE_TIME = 30;
//
//
//    private final StringRedisTemplate lockRedisTemplate;
//
//    @Autowired
//    public AvoidKeyGenerator(StringRedisTemplate lockRedisTemplate, CacheKeyGenerator cacheKeyGenerator){
//        this.lockRedisTemplate = lockRedisTemplate;
//    }
//
//
//    /**
//     * avoid删除 从redis
//     */
//    public static void removeRedisAvoidKey(HttpServletRequest request) {
//        String tokenKey = generateKey(request);
//    }
//
//    public static Boolean checkToken(HttpServletRequest request){
//        String key = generateKey(request);
//        synchronized (key.intern()){
//            if (RedisToken.TokenType.AVOID_DULPLICATE_SUMISSION.isExist(key)){
//                return false;
//            }
//            System.out.println("token:" + RedisToken.TokenType.AVOID_DULPLICATE_SUMISSION.get(key));
//            setRedisAvoidKey(request);
//        }
//        return true;
//    }
//
//    public static String generateKey(HttpServletRequest request){
//        return new StringBuffer()
//                .append(request.getRequestURI())
//                .append("?")
//                .append(request.getParameter("token"))
//                .toString();
//    }
//
//
//}
