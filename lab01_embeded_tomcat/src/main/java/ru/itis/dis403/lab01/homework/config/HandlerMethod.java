package ru.itis.dis403.lab01.homework.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerMethod {
    private Object controller;
    private Method method;

    public HandlerMethod(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public void invoke(HttpServletRequest req, HttpServletResponse resp) throws InvocationTargetException, IllegalAccessException {
        method.invoke(controller, req, resp);
    }
}
