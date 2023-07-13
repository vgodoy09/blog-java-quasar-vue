package com.blog.security;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.security.web.firewall.StrictHttpFirewall;

public final class LoggingHttpFirewall extends StrictHttpFirewall {

    @Override
    public FirewalledRequest getFirewalledRequest(final HttpServletRequest request) throws RequestRejectedException {
        try {
        	super.setUnsafeAllowAnyHttpMethod(true);
        	super.setAllowUrlEncodedSlash(true);
        	super.setAllowUrlEncodedDoubleSlash(true);
            return super.getFirewalledRequest(request);
        } catch (Exception ex) {
            throw new RequestRejectedException(ex.getMessage() + ".\n Remote Host: " + request.getRemoteHost() + "\n User Agent: " + request.getHeader("User-Agent") + "\n Request URL: " + request.getRequestURL().toString()) {
                private static final long serialVersionUID = 1L;

                @Override
                public synchronized Throwable fillInStackTrace() {
                    return this; // suppress the stack trace.
                }
            };
        }
    }
}
