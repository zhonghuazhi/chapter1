package org.smart4j.chapter2.test;

import org.smart4j.chapter2.util.PropsUtil;

import java.util.Properties;

/**
 * Created by zhonghua on 16/1/13.
 */
public class TestA {

    public static void main(String[] args){

        Properties props = PropsUtil.loadProps("config1.properties");
        System.out.println(props.getProperty("jdbc.driver"));
    }
}
