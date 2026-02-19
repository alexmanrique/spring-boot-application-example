package com.myapp.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoggingInterceptor implements HandlerInterceptor
{

    private static final Logger LOG = LoggerFactory.getLogger(LoggingInterceptor.class);


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
    {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("method: ").append(request.getMethod()).append("\t");
        logMessage.append("uri: ").append(request.getRequestURI()).append("\t");
        logMessage.append("status: ").append(response.getStatus()).append("\t");
        logMessage.append("remoteAddress: ").append(request.getRemoteAddr()).append("\t");

        if (ex != null)
        {
            LoggingInterceptor.LOG.error(logMessage.toString(), ex);
        }
        else
        {
            LoggingInterceptor.LOG.info(logMessage.toString());
        }

    }

}
