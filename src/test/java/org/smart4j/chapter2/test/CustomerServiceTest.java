package org.smart4j.chapter2.test;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smart4j.chapter2.model.Customer;
import org.smart4j.chapter2.service.CustomerService;

import java.util.List;

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
        Assert.assertEquals(2 ,list.size());
    }

}
