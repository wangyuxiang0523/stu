package com.fh.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class MessageUtil {

    /**
     * 发送短信验证码
     * @param phone
     */
    public static void getMessageCode(String phone){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FeutHAZeZ3smRcCcdCD", "rqjwhdFNwhjSyACrNCpkzxPEfxjYJH");
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "飞狐");
        request.putQueryParameter("TemplateCode", "SMS_180946088");
        String code = createMessageCode();
        request.putQueryParameter("TemplateParam", "{\"code\":"+code+"}");


        /*将验证码存放redis中*/
        Jedis jedis = RedisPool.getJedis();
        jedis.set("code", code);
        RedisPool.returnJedis(jedis);


        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成六位随机验证码
     * @return
     * String.valueOf((int)((Math.random()*9+1)*100000));
     */
    public static String createMessageCode(){
        Map<String,Object> map = new HashMap<String, Object>();
        String code = String.valueOf(123456);
        return code;
    }

}
