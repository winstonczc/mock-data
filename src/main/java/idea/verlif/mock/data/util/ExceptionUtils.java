package idea.verlif.mock.data.util;

import idea.verlif.mock.data.exception.MockDataException;

/**
 * @Description
 * @Company 广州致景科技有限公司
 * @Author chenzhicong
 * @Date 2023/2/15 12:22
 * @Version 1.0.0
 */
public class ExceptionUtils {

    /**
     * 返回一个新的异常，统一构建，方便统一处理
     *
     * @param msg 消息
     * @param t   异常信息
     */
    public static MockDataException mde(String msg, Throwable t, Object... params) {
        return new MockDataException(String.format(msg, params), t);
    }

    /**
     * 重载的方法
     *
     * @param msg 消息
     */
    public static MockDataException mde(String msg, Object... params) {
        return new MockDataException(String.format(msg, params));
    }

    /**
     * 重载的方法
     *
     * @param t 异常
     * @return 返回异常
     */
    public static MockDataException mde(Throwable t) {
        return new MockDataException(t);
    }
}
