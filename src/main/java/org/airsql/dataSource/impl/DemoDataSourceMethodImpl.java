package org.airsql.dataSource.impl;

import org.airsql.dataSource.DemoDataSourceMethod;
import org.airsql.domain.Configuration;
import org.airsql.exception.AirSqlException;
import org.airsql.exception.AirSqlUtilsException;
import org.airsql.utils.AirSqlCollectionUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据库连接池实现类
 * </p>
 *
 * @author raindrop
 * @since 2020/8/4
 */
public class DemoDataSourceMethodImpl implements DemoDataSourceMethod {
    /**
     * Init Count
     */
    private final int initCount = 3;
    /**
     * Max Link Count
     */
    private final int maxCount = 6;
    /**
     * Connectioning Count
     */
    private int currentCount = 0;

    /**
     * Master Connection Pool
     */
    private LinkedList<Connection> matserPool = new LinkedList<Connection>();

    /**
     * Slave Connection Pool
     */
    private LinkedList<Connection> slavePool = new LinkedList<Connection>();

    /**
     * Master Configtion List(Get From YML File)
     */
    private ArrayList<Configuration> masterConfig = new ArrayList<Configuration>(){{
        add(new Configuration("master", "jdbc:mysql://120.26.187.19:3307/demo", "root", "123456"));
    }};

    /**
     * Slave Configtion List(Get From YML File)
     */
    private ArrayList<Configuration> slaveConfig = new ArrayList<Configuration>(){{
        add(new Configuration("slave1", "jdbc:mysql://120.26.187.19:3317/demo", "root", "123456"));
        add(new Configuration("slave2", "jdbc:mysql://120.26.187.19:3327/demo", "root", "123456"));
    }};

    /**
     * 检查ID是否重复
     */
    {
        if (CollectionUtils.isNotEmpty(masterConfig)){
            try {
                throw new AirSqlException("Master is Empty. Check you configuration file");
            } catch (AirSqlException e) {
                e.printStackTrace();
            }
        }
        List<String> configId = new ArrayList<>();
        List<String> masterConfigId = masterConfig.stream().map(config -> config.getConfigId()).collect(Collectors.toList());
        List<String> slaveConfigId = slaveConfig.stream().map(config -> config.getConfigId()).collect(Collectors.toList());

        configId.addAll(masterConfigId);
        configId.addAll(slaveConfigId);

        try {
            AirSqlCollectionUtils.checkListCountOnlyOne(configId);
        } catch (AirSqlUtilsException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Connection getSpecifyConnection(String url, String name, String password) {
        return null;
    }

    @Override
    public Connection getConnection(String sql) {
        return null;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
