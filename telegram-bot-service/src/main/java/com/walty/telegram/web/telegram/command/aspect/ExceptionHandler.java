package com.walty.telegram.web.telegram.command.aspect;

import com.walty.telegram.web.telegram.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExceptionHandler {

    @Pointcut("@annotation(com.walty.telegram.web.telegram.command.aspect.HandleExceptions)")
    public void handleExceptionMethods() {}

    @Around("handleExceptionMethods()")
    public Object handleException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            log.error(String.format(Command.EXCEPTION_MESSAGE_LOG, joinPoint.getTarget().getClass().getSimpleName()), e);
            return Command.EXCEPTION_MESSAGE_RESPONSE;
        }
    }
}
