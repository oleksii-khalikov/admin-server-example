package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.boot.actuate.web.mappings.MappingsEndpoint;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletMappingDescription;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletMappingDetails;
import org.springframework.boot.actuate.web.mappings.servlet.RequestMappingConditionsDescription;

/**
 * A filter that modifies the request URI before passing it to the next filter in the chain.
 */
public class WebFilter implements Filter {

    private final MappingsEndpoint mappingsEndpoint;

    public WebFilter(MappingsEndpoint mappingsEndpoint) {
        this.mappingsEndpoint = mappingsEndpoint;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String requestURI = req.getRequestURI();

        if(getRequestedPaths().noneMatch(requestURI::startsWith)) {
            req = new HttpServletRequestWrapper((HttpServletRequest) servletRequest) {
                @Override
                public String getRequestURI() {
                    return "/config" + requestURI;
                }
            };
        }

        filterChain.doFilter(req, servletResponse);
    }


    private Stream<String> getRequestedPaths() {
        return mappingsEndpoint.mappings().getContexts()
                .values().stream()
                .filter(e -> Objects.nonNull(e.getMappings()) && e.getMappings().containsKey("dispatcherServlets"))
                .map(e -> (HashMap<String, ArrayList<DispatcherServletMappingDescription>>) e.getMappings().get("dispatcherServlets"))
                .flatMap(o -> o.getOrDefault("dispatcherServlet", new ArrayList<>()).stream())
                .map(DispatcherServletMappingDescription::getDetails).filter(Objects::nonNull)
                .map(DispatcherServletMappingDetails::getRequestMappingConditions).filter(Objects::nonNull)
                .map(RequestMappingConditionsDescription::getPatterns).filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(e -> e.contains("{") ? e.substring(0, e.indexOf("{")) : e)
                .map(e -> e.contains("*") ? e.substring(0, e.indexOf("*")) : e)
                .distinct();
    }
}
