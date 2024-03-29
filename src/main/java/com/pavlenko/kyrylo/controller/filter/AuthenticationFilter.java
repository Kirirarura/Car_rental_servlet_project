package com.pavlenko.kyrylo.controller.filter;

import com.pavlenko.kyrylo.model.entity.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.pavlenko.kyrylo.controller.util.UriPath.*;

public class AuthenticationFilter implements Filter {

    private static final String ROLE_ATTRIBUTE = "role";
    private static final String USER_ID_ATTRIBUTE = "userId";
    private final Logger logger = LogManager.getLogger(AuthenticationFilter.class);


    /**
     * Does nothing if user has access.
     * Redirects to Login page if user is guest and access is denied.
     * Send 403 Error if user is [CUSTOMER, MANAGER, ADMIN] and access is denied.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        final String URI = request.getRequestURI().replaceAll(request.getContextPath(), "");

        if (session.getAttribute(ROLE_ATTRIBUTE) == null) {
            session.setAttribute(ROLE_ATTRIBUTE, Role.RoleEnum.GUEST.toString());
        }

        Role.RoleEnum role = Role.RoleEnum.valueOf(session.getAttribute(ROLE_ATTRIBUTE).toString());

        if (!checkResources(URI) && !checkAccess(URI, role)) {
            if (role.equals(Role.RoleEnum.GUEST)) {
                logger.info("Redirect guest to Login page from URI ({})", URI);
                response.sendRedirect(request.getContextPath() + LOGIN);
                return;
            }
            logger.warn("User (id = {}) Forbidden uri: {}", session.getAttribute(USER_ID_ATTRIBUTE), URI);
            response.sendError(403);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean checkAccess(String uri, Role.RoleEnum roleEnum) {
        switch (roleEnum) {
            case CUSTOMER:
                return checkCustomerAccess(uri);
            case MANAGER:
                return checkManagerAccess(uri);
            case ADMIN:
                return checkAdminAccess(uri);
            default:
                return checkGuestAccess(uri);
        }
    }

    private boolean checkCustomerAccess(String uri) {
        return checkCommonAccess(uri) || uri.startsWith(CUSTOMER_PREFIX) || uri.equals(LOGOUT);
    }

    private boolean checkManagerAccess(String uri) {
        return uri.startsWith(MANAGER_PREFIX) || uri.equals(LOGOUT) || uri.equals(INDEX);
    }

    private boolean checkAdminAccess(String uri) {
        return checkCommonAccess(uri) || uri.startsWith(ADMIN_PREFIX) || uri.equals(LOGOUT);
    }

    private boolean checkGuestAccess(String uri) {
        return checkCommonAccess(uri) || uri.equals(LOGIN) || uri.equals(REGISTRATION) || uri.equals(ACTIVATE_ACCOUNT);
    }

    private boolean checkCommonAccess(String uri) {
        return uri.equals(CATALOG) || uri.equals(INDEX) || uri.equals(ROOT);
    }

    private boolean checkResources(String uri) {
        return uri.startsWith(STATIC_RESOURCES_PREFIX);
    }
}
