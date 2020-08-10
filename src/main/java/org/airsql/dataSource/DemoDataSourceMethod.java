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
     * 获取对应sql下的连接
     * @param sql
     * @return
     */
    Connection getConnection(String sql);

}
