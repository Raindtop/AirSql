package org.airsql.dataSource;

import javax.sql.DataSource;
import java.sql.Connection;

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
