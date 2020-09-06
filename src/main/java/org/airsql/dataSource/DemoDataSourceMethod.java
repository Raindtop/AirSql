package org.airsql.dataSource;

import org.airsql.domain.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p>
 * 自定义的DataSource接口类
 * 增加获取自定义的连接信息
 * 获取不同的sql之间的连接
 * </p>
 *
 * @author raindrop
 * @since 2020/8/4
 */
public interface DemoDataSourceMethod extends DataSource {

    /**
     * 初始化连接
     */
    void initConnection();

    /**
     * 初始化主服务器连接
     */
    void initMasterConnection();

    /**
     * 初始化从服务器连接
     */
    void initSlaveConnection();

    /**
     * 创建连接
     * @param configuration
     * @return
     */
    Connection createConnection(Configuration configuration) throws ClassNotFoundException, SQLException;

    /**
     * 获取指定url的连接
     * @param url
     * @param name
     * @param password
     * @return
     */
    Connection getSpecifyConnection(String url, String name, String password);

    /**
     * 执行sql
     * @param sql
     */
    void execuse(String sql);

    /**
     * 获取对应sql下的连接
     * @param sql
     * @return
     */
    Connection getConnection(String sql) throws InterruptedException;

    /**
     * 获取主服务器连接
     * @return
     */
    Connection getMasterConnection();

    /**
     * 获取从服务器连接
     * @return
     */
    Connection getSlaveConnection();

    /**
     * 关闭连接
     * @param connection
     * @param configId
     */
    void closeConnection(Connection connection, String configId);

}
