package zerobase.stockdividends.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.stockdividends.exception.AbstractException;

public class ScrapingException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value(); // 500
    }

    @Override
    public String getMessage() {
        return "웹 스크래핑 중 문제가 발생했습니다.";
    }
}
