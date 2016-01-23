package org.smart4j.chapter2.helper;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.util.PropsUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zhonghua on 16/1/13.
 */
public final class DataBaseHelper {

    private static final Logger logger = LoggerFactory.getLogger(DataBaseHelper.class);

    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private static final ThreadLocal<Connection> CONNECTION_THREAD_LOCAL = new ThreadLocal<Connection>();

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        Properties props = PropsUtil.loadProps("config.properties");

        DRIVER = props.getProperty("jdbc.driver");
        URL = props.getProperty("jdbc.url");
        USERNAME = props.getProperty("jdbc.username");
        PASSWORD = props.getProperty("jdbc.password");

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            logger.error("can not load jdbc driver. ", e);
        }
    }

    /**
     * 获取连接
     *
     * @return
     */
    public static Connection getConnection() {

        Connection connection = CONNECTION_THREAD_LOCAL.get();
        if (null == connection) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                logger.error("get Connection failure. ", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_THREAD_LOCAL.set(connection);
            }
        }
        return connection;
    }

    /**
     * 关闭连接
     *
     * @param connection
     */
    public static void closeConnection(Connection connection) {

        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("close connection failure. ", e);
            }
        }
    }

    /**
     * 关闭连接
     */
    public static void closeConnection() {

        Connection connection = CONNECTION_THREAD_LOCAL.get();
        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("close Connection failure. ", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_THREAD_LOCAL.remove();
            }
        }
    }

    public static <T> List<T> queryEntityList(Class<T> entityClass,
                                              Connection connection, String sql) {

        List<T> list = null;
        try {
            list = QUERY_RUNNER.query(connection, sql, new BeanListHandler<T>(entityClass));
        } catch (SQLException e) {
            logger.error("query entity list failure. ", e);
        } finally {
            closeConnection(connection);
        }
        return list;
    }

    /**
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass,
                                              String sql, Object... params) {

        List<T> list = null;
        Connection connection = getConnection();

        try {
            list = QUERY_RUNNER.query(connection, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            logger.error("query entity list failure. ", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection);
        }
        return list;
    }

    /**
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass,
                                    String sql, Object... params) {
        T entity = null;
        Connection connection = getConnection();

        try {
            entity = QUERY_RUNNER.query(connection, sql, new BeanHandler<T>(entityClass), params);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection);
        }
        return entity;
    }

    /**
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String, Object>> queryEntityMap(String sql, Object... params) {

        List<Map<String, Object>> listMap = null;
        Connection connection = getConnection();

        try {
            listMap = QUERY_RUNNER.query(connection, sql, new MapListHandler(), params);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            closeConnection(connection);
        }
        return listMap;
    }

    /**
     * @param entityClass
     * @param fieldMap
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass,
                                           Map<String, Object> fieldMap) {

        String sql = "INSERT INTO " + getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");

        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(", ");
            values.append("? ,");
        }

        columns = columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values = values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql += columns + " VALUES " + values;
        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql, params) == 1;
    }

    /**
     * @param entityClass
     * @param id
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id,
                                           Map<String, Object> fieldMap) {

        String sql = "UPDATE " + getTableName(entityClass) + " SET ";
        StringBuilder columns = new StringBuilder();

        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append("=?, ");
        }
        sql += columns.substring(0, columns.lastIndexOf(",")) + " WHERE id - ?";

        List<Object> params = new ArrayList<Object>();
        params.addAll(fieldMap.values());
        params.add(id);

        return executeUpdate(sql, params) == 1;
    }

    /**
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id) {

        String sql = "DELETE FROM " + getTableName(entityClass) + " WHERE id = ?";
        return executeUpdate(sql, id) == 1;
    }

    private static String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }

    /**
     * @param sql
     * @param params
     * @return
     */
    private static int executeUpdate(String sql, Object... params) {

        int rows = 0;
        Connection connection = getConnection();

        try {
            rows = QUERY_RUNNER.update(connection, sql, params);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException();
        }
        return rows;
    }
}