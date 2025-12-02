package com.simplesoft.config;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Order(10)
@Component
@RequiredArgsConstructor
public class LoggingAspect {

  @Pointcut("(within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *)) && !@annotation(com.hisco.spowise.config.aop.NoLogging)")
  public void appPackagePointcut() {
  }


  @AfterThrowing(pointcut = "appPackagePointcut()", throwing = "e")
  public void logAfterThrowing(JoinPoint joinPoint, Exception e) {
	  log.info("::InvalidException::{}", generateExceptionMessage(joinPoint, e));
  }


  @Around("appPackagePointcut()")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    String requestFrom = requestFrom();
    Object endResponse = null;

    loggingIn(requestFrom, joinPoint);

    try {
      endResponse = joinPoint.proceed();
      return endResponse;

    } catch (Exception e) {
      endResponse = "Exception:" + e.getMessage();
      loggingError(joinPoint, e);
      throw e;

    } finally {
      loggingOut(requestFrom, joinPoint, endResponse);
    }
  }


  private String requestFrom() {
    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (servletRequestAttributes != null) {
      HttpServletRequest request = servletRequestAttributes.getRequest();
      String requestUrl = request.getMethod() + " " + request.getRequestURI();
      return requestUrl;
    }
    return "";
  }

  private void loggingIn(String requestFrom, JoinPoint joinPoint) {
    log.info("### IN ## {} => {}() ## Args: {}", requestFrom, joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
  }

  private void loggingOut(String requestFrom, JoinPoint joinPoint, Object endResponse) {
	  if(!"getMenuList".equals(joinPoint.getSignature().getName())){
		  log.info("### OUT ## {} => {}() ## 결과: {}", requestFrom, joinPoint.getSignature().getName(), endResponse);
	  }
  }

  private void loggingError(JoinPoint joinPoint, Exception e) {
    log.error("### 예외 발생: {}.{}() ## 메세지:{} ## 원인: {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
              e.getMessage(), (e.getCause() != null) ? e.getCause().getMessage() : "알 수 없음", e);
  }

  private String generateExceptionMessage(JoinPoint joinPoint, Exception e) {
    String requestFrom = requestFrom();
    return "*- Point:* " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature()
                                                                                            .getName() + "\n*- Exception:* `" + e.getClass() + "`" + "\n*- Message:* " + e.getMessage() + "\n*- Request:* " + requestFrom + "\n*- Params:* " + Arrays.toString(
        joinPoint.getArgs());
  }
}
