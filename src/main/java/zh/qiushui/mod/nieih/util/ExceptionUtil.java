package zh.qiushui.mod.nieih.util;

public class ExceptionUtil {
    public static RuntimeException unexpected(String info, Throwable e) {
        return new RuntimeException(
            "Unexpected " + info + ". Please report to the author.",
            e
        );
    }
    public static RuntimeException cannot(String info, Throwable e) {
        return new RuntimeException(
            "Cannot " + info + ". Please report to the author.",
            e
        );
    }
}
