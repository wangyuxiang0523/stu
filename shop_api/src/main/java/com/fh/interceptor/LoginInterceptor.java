package com.fh.interceptor;

import com.fh.model.po.VipPo;
import com.fh.util.JWT;
import com.fh.util.JwtUtils;
import com.fh.util.RedisPool;
import com.fh.util.exception.AuthenticateException;
import com.fh.util.noLogin.NoLogin;
import com.fh.util.response.ServerEnum;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Base64;

@Component
public class LoginInterceptor  implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String methodType = request.getMethod();//获取请求的方式
        if(methodType.equalsIgnoreCase("options")){//equalsIgnoreCase 不区分大小写
         response.addHeader("Access-Control-Allow-Headers", "token");
          String origin = request.getHeader("Origin");//获取请求的域名信息
          response.setHeader("Access-Control-Allow-Origin",origin);

          return false;
        }
        HandlerMethod hm=(HandlerMethod) handler;
        Method method = hm.getMethod();
        boolean isLogin = method.isAnnotationPresent(NoLogin.class);
        if(isLogin == true){//有注解 需要登录验证
            String token = request.getHeader("token");//经过base64 加密后的  手机$token
            //解密
            byte[] decode = Base64.getDecoder().decode(token);
            String s = new String(decode);
            String[] split = s.split("!");
            if (split.length!=2){
                //token有误
            throw new Exception("非法");
            }
            String phone = split[0];
            String jwtToken = split[1];
            System.out.println(jwtToken);
            Jedis jedis = RedisPool.getJedis();
            String redisToken = jedis.get(phone);
            if(redisToken!=null){//验证登录是否超时
                //验证token是不是最新的
            if(redisToken.equals(jwtToken)){
               //token正确   解析用户信息  放入request
                VipPo vipPo = JWT.unsign(jwtToken, VipPo.class);
                request.setAttribute("login_vip",vipPo);
                jedis.expire(vipPo.getPhone(),60*30);
                RedisPool.returnJedis(jedis);
                return true;
            }else{
               throw new AuthenticateException(ServerEnum.VIP_LOGIN_ING);
            }
            }else {
            throw new AuthenticateException(ServerEnum.TOKEN_TIMEOUT);

              /*  request.getRequestDispatcher("/login/returnNologin").forward(request,response);*/
            /*  return false;*/
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


}
