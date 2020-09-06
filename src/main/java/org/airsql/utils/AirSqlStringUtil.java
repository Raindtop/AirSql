package org.airsql.utils;

public class AirSqlStringUtil {

    /**
     * 判断SQL是否需要Master
     * @param sql
     * @return
     */
    public static boolean isMasterSql(String sql){
        return !sql.trim().startsWith("select");
    }
}
