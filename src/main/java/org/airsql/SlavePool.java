package org.airsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * A Connection Pool Demo
 *
 * @author raindrop
 * @since 2020/7/23
 */
public class SlavePool {
    /**
     * Init Count
     */
    private final int initCount = 6;
    /**
     * Max Link Count
     */
    private final int maxCount = 12;
    /**
     * Connectioning Count
     */
    private int currentCount = 0;

    /**
     * Connection Pool
     */
    private LinkedList<Connection> pool = new LinkedList<Connection>();

    /**
     * Create a connection pool
     */
    public SlavePool() throws SQLException, ClassNotFoundException {
        for (int i=0;i<initCount;i++){
            currentCount++;
            Connection connection = createConnection();
            pool.addLast(connection);
        }
    }

    /**
     * Create a master mysql connection
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection createConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://120.26.187.19:3317/demo", "root", "123456");
        return connection;
    }

    /**
     * get Connection(保证线程安全)
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public synchronized Connection getConnection() throws SQLException, ClassNotFoundException {
        if (pool.size() > 0){
            //removeFirst删除第一个并且返回
            //现在你一定看懂了我说的为什要用LinkedList了吧，因为下面的这个
            //removeFirst()方法会将集合中的第一个元素删除，但是还会返回第一个元素
            //这样就省去了我们很多不必要的麻烦
            return pool.removeFirst();
        }
        if (currentCount < maxCount){
            //记录当前使用的连接数
            currentCount++;
            //创建链接
            return createConnection();
        }
        throw new RuntimeException("当前链接已经达到最大连接数");
    }

    /**
     * close Connection(保证线程安全）
     * @param connection
     */
    public synchronized void releaseConnection(Connection connection){
        if (pool.size() < initCount){
            pool.addLast(connection);
            currentCount--;
        }else {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
