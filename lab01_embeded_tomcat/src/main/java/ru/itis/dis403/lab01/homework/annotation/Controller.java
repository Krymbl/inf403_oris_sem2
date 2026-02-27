package ru.itis.dis403.lab01.homework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Говорит, что эта метка должна сохраниться до момента выполнения программы, чтобы контекст мог ее увидеть.
@Retention(value = RetentionPolicy.RUNTIME)
//Говорит, что эту метку можно вешать только на типы (классы и интерфейсы), а не на поля или методы.
@Target(value = ElementType.TYPE)
public @interface Controller {
}
