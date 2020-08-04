package org.airsql.exception;

/**
 * <p>
 * AirSql工具类异常
 * </p>
 *
 * @author raindrop
 * @since 2020/8/4
 */
public class AirSqlUtilsException extends Exception{

    public AirSqlUtilsException() {
    }

    public AirSqlUtilsException(String msg) {
        super(msg);
    }

    public AirSqlUtilsException(Throwable cause) {
        super(cause);
    }

    public AirSqlUtilsException(String message, Throwable cause) {
        super(message, cause);
    }

}
