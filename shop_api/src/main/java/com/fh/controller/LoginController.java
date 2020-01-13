package com.fh.controller;

import com.fh.model.po.VipInfoPo;
import com.fh.model.po.VipPo;
import com.fh.service.LoginService;
import com.fh.util.*;
import com.fh.util.login.LoginAnnotation;
import com.fh.util.response.ResponseServer;
import com.fh.util.response.ServerEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("login")
////exposedHeaders 用户代理将允许客户端根据实际响应访问的响应标头列表，而不是“简单”标头
@CrossOrigin(maxAge = 3600/*,origins = "http://localhost:8083"*/)
public class LoginController {
      @Autowired
      private LoginService loginService;

      @GetMapping("{phone}")
      public ResponseServer sendCode(@PathVariable String phone)  {
            if(StringUtils.isBlank(phone) ){
                  return ResponseServer.error(ServerEnum.PHONE_NULL);
            }

            MessageUtil.getMessageCode(phone);
            return ResponseServer.success();
      }

      @PostMapping
      public ResponseServer login(String phone, String code, HttpSession request){
            if(StringUtils.isBlank(phone) || StringUtils.isBlank(code)){
                  return ResponseServer.error(ServerEnum.PHONE_OR_CODE_NULL);
            }
            //验证 验证码是否正确
            Jedis jedis = RedisPool.getJedis();
            String code1 = jedis.get("code");
            if(!code.equals(code1)){
                  return ResponseServer.error(ServerEnum.CODE_ERROR);
            }
            //删除该手机号的验证码
            jedis.del("code");
            //判断手机号是否存在，不存在就注册
            VipPo vipPo= loginService.isRegister(phone);
            //将用户信息通过JWT生成密钥
            String sign = JWT.sign(vipPo, 1000 * 60 * 60 * 24);
            System.out.println(sign);
            //将token和用户的一个唯一标识 放入redis  解决登录超时
            jedis.set(vipPo.getPhone(),sign);
            //30分钟不操作   就重新登陆验证
            jedis.expire(vipPo.getPhone(),60*30);
            //真正的Token  phone$token
            String token = vipPo.getPhone() + "!" + sign;
            //进行加密
            byte[] encode = Base64.getEncoder().encode(token.getBytes());
            String encodeToken = new String(encode);
            RedisPool.returnJedis(jedis);
            return ResponseServer.success(encodeToken);
      }

      public static void main(String[] arg){
            List<Integer> list= Arrays.asList(1,2,3,4,5,6,7,8,9,10);
            Integer count = list.stream().reduce(0,(x,y)->x+y);
            System.out.println(count);
      }
      @PostMapping("perfect")

      public ResponseServer perfect(HttpSession request){
            String phone =(String) request.getAttribute("phone");
            VipInfoPo vipInfoPo= loginService.queryVipByPhone(phone);
            Object phone1 = request.getAttribute("phone");
            return ResponseServer.success();
      }
      @PostMapping("remove")
      public ResponseServer remove(HttpServletRequest request){
            Object phone = request.getSession().getAttribute("phone");
            return ResponseServer.success();
      }
      @RequestMapping("/returnNologin")
      public ResponseServer returnNologin(){
            return ResponseServer.error(4000,"nologin");
      }
}
