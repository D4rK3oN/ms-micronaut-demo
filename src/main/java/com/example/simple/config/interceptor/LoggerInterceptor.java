package com.example.simple.config.interceptor;

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.Arrays;

@Singleton
@Slf4j
public class LoggerInterceptor implements MethodInterceptor<Object, Object> {

    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {
        if (log.isDebugEnabled())
            log.debug("Invoked class [{}.{}] | Input Args -> {}",
                    context.getDeclaringType().getSimpleName(),
                    context.getMethodName(),
                    getArgs(context.getParameterValues())
            );

        final Object proceed = context.proceed();

        if (log.isDebugEnabled())
            log.debug("Invoked class [{}.{}] | Output Response -> {}",
                    context.getDeclaringType().getSimpleName(),
                    context.getMethodName(),
                    proceed
            );

        return proceed;
    }

    private String getArgs(Object[] parameterValues) {
        return parameterValues.length == 0 ? "[EMPTY]" : Arrays.deepToString(parameterValues);
    }
}
