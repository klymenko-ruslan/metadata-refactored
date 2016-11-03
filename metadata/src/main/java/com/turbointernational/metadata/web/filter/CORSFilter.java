package com.turbointernational.metadata.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/**
 *
 * @author jrodriguez
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter extends GenericFilterBean implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.addHeader(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        httpResponse.addHeader(
                "Access-Control-Allow-Headers",
                httpRequest.getHeader("Access-Control-Request-Headers"));

        httpResponse.addHeader(
                "Access-Control-Allow-Origin",
                StringUtils.defaultIfBlank(httpRequest.getHeader("Origin"), "*"));

        httpResponse.addHeader(
                "Access-Control-Allow-Credentials",
                "true");

        chain.doFilter(request, response);
    }
}
