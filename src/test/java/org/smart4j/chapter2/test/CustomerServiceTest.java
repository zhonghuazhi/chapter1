package org.smart4j.chapter2.test;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smart4j.chapter2.model.Customer;
import org.smart4j.chapter2.service.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhonghua on 16/1/3.
 */
public class CustomerServiceTest {

    private final CustomerService customerService;

    public CustomerServiceTest() {
        this.customerService = new CustomerService();
    }

    @Before
    public void init() {
        System.out.println("载入 数据库 链接");
    }

    @Test
    public void getCustomerList() {

        List<Customer> list = customerService.getCustomerList();
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void getCustomer() {

        Customer customer = customerService.getCustomer(2);
        Assert.assertNotNull(customer);
    }

//    @Test
//    public void createCustomer() {
//
//        Map<String, Object> fieldMap = new HashMap<String, Object>();
//        fieldMap.put("name", "customer100");
//        fieldMap.put("contact", "John");
//        fieldMap.put("telephone", "13333333333");
//        fieldMap.put("id", 1);
//
//        boolean bool = customerService.createCustomer(fieldMap);
//        Assert.assertTrue(bool);
//    }

    @Test
    public void updateCustomer() {

        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("telephone", "bbb");

        boolean bool = customerService.updateCustomer(2, fieldMap);
        Assert.assertTrue(bool);
    }

//    @Test
//    public void deleteCustomer(){
//
//        boolean bool = customerService.deleteCustomer(1);
//        org.junit.Assert.assertTrue(bool);
//    }
}