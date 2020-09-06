package org.airsql.dataSource.impl;

import org.airsql.dataSource.DemoDataSourceMethod;
import org.airsql.dataSource.common.ConfigType;
import org.airsql.domain.Configuration;
import org.airsql.exception.AirSqlException;
import org.airsql.exception.AirSqlUtilsException;
import org.airsql.utils.AirSqlCollectionUtil;
import org.airsql.utils.AirSqlStringUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
     * Slave Connection Count is double initCount
     */
    private final int initCount = 3;
    /**
     * Max Link Count
     * Slave Connection Count is double initCount
     */
    private final int maxCount = 6;

    /**
     * Current Master Connection Count
     */
    private Map<String, Integer> currentMasterCount = new ConcurrentHashMap<>();

    /**
     * Current Slace Connection Count
     */
    private Map<String, Integer> currentSlaveCount = new ConcurrentHashMap<>();

    /**
     * Current Connection Count
     */
    private Map<String, Integer> connectionCount = new ConcurrentHashMap<>();

    /**
     * Master Connection Pool
     */
    private Map<String, LinkedList<Connection>> masterPool = new ConcurrentHashMap<>();

    /**
     * Slave Connection Pool
     */
    private Map<String, LinkedList<Connection>> slavePool = new ConcurrentHashMap();

    /**
     * Master Configtion List(Get From YML File)
     */
    private static ArrayList<Configuration> masterConfig = new ArrayList<Configuration>(){{
        add(new Configuration("master", ConfigType.MASTER, "jdbc:mysql://120.26.187.19:3307/demo", "root", "123456", "com.mysql.cj.jdbc.Driver"));
    }};

    /**
     * Slave Configtion List(Get From YML File)
     */
    private static ArrayList<Configuration> slaveConfig = new ArrayList<Configuration>(){{
        add(new Configuration("slave1", ConfigType.SLAVE, "jdbc:mysql://120.26.187.19:3317/demo", "root", "123456", "com.mysql.cj.jdbc.Driver"));
        add(new Configuration("slave2", ConfigType.SLAVE, "jdbc:mysql://120.26.187.19:3327/demo", "root", "123456", "com.mysql.cj.jdbc.Driver"));
    }};

    /**
     * 启动自检
     */
    static {
        try {
            if (CollectionUtils.isEmpty(masterConfig)){
                throw new AirSqlException("Master is Empty. Check you configuration file");
            }
            List<String> configId = new ArrayList<>();
            List<String> masterConfigId = masterConfig.stream().map(config -> config.getConfigId()).collect(Collectors.toList());
            List<String> slaveConfigId = slaveConfig.stream().map(config -> config.getConfigId()).collect(Collectors.toList());

            configId.addAll(masterConfigId);
            configId.addAll(slaveConfigId);

            try {
                AirSqlCollectionUtil.checkListCountOnlyOne(configId);
            } catch (AirSqlUtilsException e) {
                e.printStackTrace();
            }

            System.out.println("AirSql Self-Check is OK.");
        } catch (AirSqlException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws AirSqlException {
        DemoDataSourceMethod demoDataSourceMethod = new DemoDataSourceMethodImpl();
        demoDataSourceMethod.execuse("select * from demo");
        System.out.println("11");
    }

    /**
     * 初始化连接数
     */
    public DemoDataSourceMethodImpl(){
        initConnection();
    }

    @Override
    public void initConnection() {
        initMasterConnection();
        initSlaveConnection();
    }

    @Override
    public void initMasterConnection() {
        for(Configuration configuration: masterConfig){
            LinkedList<Connection> configurations = new LinkedList<>();
            for (int i = 0; i < initCount; i++){
                Connection connection = createConnection(configuration);
                configurations.add(connection);
            }
            masterPool.put(configuration.getConfigId(), configurations);
            connectionCount.put(configuration.getConfigId(), initCount);
        }
    }

    @Override
    public void initSlaveConnection() {
        for(Configuration configuration: slaveConfig){
            LinkedList<Connection> configurations = new LinkedList<>();
            for (int i = 0; i < initCount; i++){
                Connection connection = createConnection(configuration);
                configurations.add(connection);
            }
            slavePool.put(configuration.getConfigId(), configurations);
            connectionCount.put(configuration.getConfigId(), initCount);
        }
    }

    @Override
    public synchronized Connection createConnection(Configuration configuration){
        try{
            Class.forName(configuration.getDriverClass());
            Connection connection = DriverManager.getConnection(configuration.getUrl(), configuration.getUser(), configuration.getPassword());
            return connection;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    @Override
    public Connection getSpecifyConnection(String url, String name, String password) {
        return null;
    }

    @Override
    public void execuse(String sql) {
        try{
            Connection connection = getConnection(sql);
            Statement statement = connection.createStatement();
            if (AirSqlStringUtil.isMasterSql(sql)){
                Boolean result = statement.execute(sql);
            }else{
                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    System.out.println(resultSet.getInt(1) + "," + resultSet.getString(2));
                }
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public synchronized Connection getConnection(String sql){
        Connection connection = null;
        if (AirSqlStringUtil.isMasterSql(sql)){
            connection = getMasterConnection();
        }else{
            connection = getSlaveConnection();
        }
        return connection;
    }

    @Override
    public Connection getMasterConnection() {

        /**
         * 获取最小连接数的连接ID
         */
        String configurationId = AirSqlCollectionUtil.findLessUseConnection(currentMasterCount);
        Integer count = currentMasterCount.get(configurationId);

        if (configurationId == null){
            configurationId = masterConfig.get(0).getConfigId();
            count = 0;
        }

        LinkedList<Connection> connections = masterPool.get(configurationId);
        if (connections.size() > 0){
            return connections.removeFirst();
        }

        if (count  < maxCount){
            Configuration configuration = AirSqlCollectionUtil.getConfigById(configurationId, masterConfig);
            //记录当前使用的连接数
            currentMasterCount.put(configurationId, count + 1);
            //创建链接
            return createConnection(configuration);
        }
        throw new RuntimeException("Connection count is max");
    }

    @Override
    public Connection getSlaveConnection() {
        /**
         * 获取最小连接数的连接ID
         */
        String configurationId = AirSqlCollectionUtil.findLessUseConnection(currentSlaveCount);
        Integer count = 0;

        if (configurationId == null){
            configurationId = slaveConfig.get(0).getConfigId();
            count = 0;
        }else{
            count = currentSlaveCount.get(configurationId);
        }

        LinkedList<Connection> connections = slavePool.get(configurationId);
        if (connections.size() > 0){
            return connections.removeFirst();
        }

        if (count  < maxCount){
            Configuration configuration = AirSqlCollectionUtil.getConfigById(configurationId, slaveConfig);
            //记录当前使用的连接数
            currentSlaveCount.put(configurationId, count + 1);
            //创建链接
            return createConnection(configuration);
        }
        throw new RuntimeException("Connection count is max");
    }

    @Override
    public void closeConnection(Connection connection, String configId) {

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
