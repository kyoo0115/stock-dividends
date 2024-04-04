package zerobase.stockdividends.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.stockdividends.exception.AbstractException;

public class NoTickerException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 회사 ticker 입니다.";
    }
}
