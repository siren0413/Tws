package com.tws;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )  {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("storm-spring.xml");
        ctx.getBean("historyMessageDecoder");
    }
}
