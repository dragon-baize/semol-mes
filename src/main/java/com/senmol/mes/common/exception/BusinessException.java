package com.senmol.mes.common.exception;

/**
 * @author Administrator
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -2325298771220372190L;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
}
