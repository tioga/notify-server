package org.tiogasolutions.notify.engine.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.lib.jaxrs.providers.TiogaJaxRsExceptionMapper;
import org.tiogasolutions.notify.notifier.Notifier;

import javax.ws.rs.ext.Provider;

@Provider
public class NotifyExceptionMapper extends TiogaJaxRsExceptionMapper {

    private final Notifier notifier;

    @Autowired
    public NotifyExceptionMapper(Notifier notifier) {
        this.notifier = notifier;
    }

    @Override
    protected void log4xxException(String msg, Throwable throwable, int statusCode) {
        super.log4xxException(msg, throwable, statusCode);

        notifier.begin()
                .summary(msg)
                .exception(throwable)
                .topic("Unhandled 4xx")
                .send();
    }

    @Override
    protected void log5xxException(String msg, Throwable throwable, int statusCode) {
        super.log5xxException(msg, throwable, statusCode);

        notifier.begin()
                .summary(msg)
                .exception(throwable)
                .topic("Unhandled 5xx")
                .send();
    }
}