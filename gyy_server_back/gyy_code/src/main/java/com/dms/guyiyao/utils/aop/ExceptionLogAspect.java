package com.dms.guyiyao.utils.aop;


import com.dms.guyiyao.mapper.LogMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

//@Component
//@Aspect
public class ExceptionLogAspect {
    @Autowired
    private LogMapper logMapper;

    @Pointcut("execution(* com.dms.guyiyao.controller.*.*(..))")
    public  void  commonExceptionPointCut_controller(){}
    @Pointcut("execution(* com.dms.guyiyao.service.Impl.*.*(..))")
    public  void  commonExceptionPointCut_service(){}

    @Pointcut("execution(* com.dms.guyiyao.utils.*.*(..))")
    public  void  commonExceptionPointCut_util(){}

//    @Pointcut("execution(* com.dms.guyiyao..*.*(..))")


    @Pointcut("execution(* com.dms.guyiyao.security.*..*.*(..))")
    public void loginFailPointCut(){}
//    @Pointcut("execution(* com.dms.guyiyao.filter.*.*(..))")
//    public void loginFailPointCut_filter(){}


    @AfterThrowing(pointcut = "commonExceptionPointCut_controller()||commonExceptionPointCut_service()||commonExceptionPointCut_util()",throwing = "ee")
    public void CommonException(JoinPoint joinPoint,Throwable ee){
        RequestAttributes requestAttributes= RequestContextHolder.getRequestAttributes();
        HttpServletRequest  request=(HttpServletRequest)requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method= signature.getMethod();
        System.out.println("123");
        String excp= ee.toString()+ee.getCause()+ "   In    "+Arrays.stream(ee.getStackTrace()).findFirst().toString().replace("Optional[","").replace("]","");
        if (excp.length()>200)excp=excp.substring(0,200);
//        System.out.println(excp);
//        System.out.println("ip"+request.getRemoteAddr());
//        System.out.println(request.getRequestURI());
//        System.out.println(request.getParameter("username"));
        System.out.println(123);
        String param="";
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            StringBuilder value=new StringBuilder();
            value.append(entry.getKey()+"=");
            for (String s : entry.getValue()){
                if (s.length()>100){
                    s="toLong";
                    continue;
                }
                value.append(s).append(",");
            }
            param+=value;
        }
        Date date=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(date);
        logMapper.CommonException(request.getRequestURI(),request.getRemoteUser(),request.getRemoteAddr(),excp,param,"CommonFail",time,"back");
    }

    @AfterThrowing(pointcut = "loginFailPointCut()",throwing = "e")
    public void loginFailException(JoinPoint joinPoint,Throwable e){
        RequestAttributes requestAttributes= RequestContextHolder.getRequestAttributes();
        HttpServletRequest  request=(HttpServletRequest)requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method= signature.getMethod();
        System.out.println("123");
        String excp= e.toString()+e.getCause()+ "  In  "+Arrays.stream(e.getStackTrace()).findFirst().toString().replace("Optional[","").replace("]","");

//        System.out.println(e);
//        System.out.println(e.getMessage());
//        System.out.println(e.getCause());

//        System.out.println("ip"+request.getRemoteAddr());
//        System.out.println(request.getRequestURI());
//        System.out.println(request.getParameter("username"));
        String param="";
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            StringBuilder value=new StringBuilder();
            value.append(entry.getKey()+"=");
            for (String s : entry.getValue())value.append(s).append(",");
            param+=value;
        }
        Date date=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(date);
        logMapper.loginFailException(request.getRequestURI(),request.getRemoteUser(),request.getRemoteAddr(),excp,param,"loginFail",time,"back");
//        System.out.println("错误位置"+Arrays.stream(e.getStackTrace()).findFirst());
//        System.out.println("参数"+Arrays.stream(method.getParameters()).findAny());
//        Optional<Parameter> any = Arrays.stream(method.getParameters()).findAny();

//        System.out.println(method.getDeclaringClass());
//        System.out.println(method.getExceptionTypes());
    }
}
