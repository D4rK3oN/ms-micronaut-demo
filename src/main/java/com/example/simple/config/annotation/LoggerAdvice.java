package com.example.simple.config.annotation;

import com.example.simple.config.interceptor.LoggerInterceptor;
import io.micronaut.aop.Around;
import io.micronaut.context.annotation.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Around
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Type(LoggerInterceptor.class)
public @interface LoggerAdvice {
}
