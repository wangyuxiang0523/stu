package com.fh.util.login;

import com.fh.util.JwtUtils;
import com.fh.util.exception.AuthenticateException;
import com.fh.util.response.ResponseServer;
import com.fh.util.response.ServerEnum;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
//@Component泛指组件，当组件不好归类的时候，我们可以使用这个注解进行标注。
public class LoginAuthorization {
    @Around(value="execution(* com.fh.controller.*.*(..))&&@annotation(loginAnnotation)")
    public Object loginAround(ProceedingJoinPoint joinPoint, LoginAnnotation loginAnnotation){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String token =request.getHeader("token");
        System.out.println(token);
        if(StringUtils.isBlank(token)){
            //自定义异常
            throw new AuthenticateException(ServerEnum.TOKEN_TIMEOUT);
        }
        ResponseServer responseServer = JwtUtils.resolverToken(token);
        if(responseServer.getCode() != 200){
            throw new AuthenticateException(ServerEnum.TOKEN_TIMEOUT);
            
        }
        Claims claims= (Claims) responseServer.getData();
        String phone= (String) claims.get("phone");
        request.setAttribute("phone",phone);
        Object obj=null;
        try {
            obj=joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return obj;
    }
}
