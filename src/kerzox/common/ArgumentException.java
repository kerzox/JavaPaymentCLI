package kerzox.common;

import java.util.Formatter;

public class ArgumentException extends Exception {

    public ArgumentException(String msg) {
        super(msg);
    }

    public ArgumentException(String msg, Object... args) {
        super(new Formatter().format(msg, args).toString());
    }

}
