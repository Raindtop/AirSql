package org.airsql.exception;

/**
 * <p>
 * 空气sql自定义异常
 * </p>
 *
 * @author raindrop
 * @since 2020/8/4
 */
public class AirSqlException extends Exception{

    public AirSqlException() {
    }

    public AirSqlException(String msg) {
        super(msg);
    }

    public AirSqlException(Throwable cause) {
        super(cause);
    }

    public AirSqlException(String message, Throwable cause) {
        super(message, cause);
    }

}
