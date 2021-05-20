package com.hitqz.scds.config;

import com.hitqz.scds.common.domain.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.beans.PropertyEditorSupport;
import java.util.Date;

@RestControllerAdvice
public class GlobalControllerConfig {

    private Logger log = LoggerFactory.getLogger(getClass());

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(new DateConverter().convert(text));
            }
        });
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse baseExceptionHandler(Exception e) {
        log.error("系统发生异常", e);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(500)
                .msg(e.getMessage())
                .build();
    }
}
