package ru.itis.dis403.lab01.homework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Метка должна сохраниться до момента выполнения программы, чтобы контекст мог ее увидеть.
@Retention(value = RetentionPolicy.RUNTIME)
//Метку можно вешать только на типы (классы и интерфейсы)
@Target(value = ElementType.TYPE)
public @interface Controller {
}
