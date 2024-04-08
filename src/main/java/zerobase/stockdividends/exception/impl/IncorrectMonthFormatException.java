package zerobase.stockdividends.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.stockdividends.exception.AbstractException;

public class IncorrectMonthFormatException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "날짜 형식이 잘못 되었습니다. 새로고침 해주세요";
    }
}
