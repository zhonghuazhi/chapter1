package org.smart4j.chapter2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.helper.DataBaseHelper;
import org.smart4j.chapter2.model.Customer;

import java.util.List;
import java.util.Map;

/**
 * 提供客户数据服务
 * Created by zhonghua on 16/1/3.
 */
public class CustomerService {

    private static Logger logger = LoggerFactory.getLogger(CustomerService.class);

    /**
     * 获取客户列表
     *
     * @return
     */
    public List<Customer> getCustomerList() {

        String sql = "SELECT * FROM CUSTOMER";
        return DataBaseHelper.queryEntityList(Customer.class, sql);
    }

    /**
     * 获取客户
     *
     * @param id
     * @return
     */
    public Customer getCustomer(long id) {

        String sql = "SELECT * FROM CUSTOMER WHERE ID = ?";
        return DataBaseHelper.queryEntity(Customer.class, sql, id);
    }

    /**
     * 创建客户
     *
     * @param fieldMap
     * @return
     */
    public boolean createCustomer(Map<String, Object> fieldMap) {

        return DataBaseHelper.insertEntity(Customer.class, fieldMap);
    }

    /**
     * 更新客户
     *
     * @param id
     * @param fieldMap
     * @return
     */
    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {

        return DataBaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    /**
     * 删除客户
     *
     * @param id
     * @return
     */
    public boolean deleteCustomer(long id) {

        return DataBaseHelper.deleteEntity(Customer.class ,id);
    }
}