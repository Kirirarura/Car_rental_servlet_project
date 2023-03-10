package com.pavlenko.kyrylo.controller.command.impl.guest;

import com.pavlenko.kyrylo.controller.command.Command;
import com.pavlenko.kyrylo.controller.util.JspFilePath;
import com.pavlenko.kyrylo.controller.util.UriPath;
import com.pavlenko.kyrylo.controller.validator.statuses.StatusesContainer;
import com.pavlenko.kyrylo.model.entity.User;
import com.pavlenko.kyrylo.model.exeption.AuthenticationException;
import com.pavlenko.kyrylo.model.exeption.DataBaseException;
import com.pavlenko.kyrylo.model.exeption.NotActivatedAccountException;
import com.pavlenko.kyrylo.model.exeption.UserIsBlockedException;
import com.pavlenko.kyrylo.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

import static com.pavlenko.kyrylo.controller.util.ConstantsContainer.*;

/**
 * Process login.
 */
public class PostLoginCommand implements Command {


    private final UserService userService;

    public PostLoginCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String email = request.getParameter(EMAIL);
        String password = request.getParameter(PASSWORD);

        try {
            User user = userService.authentication(email, password);
            request.getSession().setAttribute(USER_ID, user.getId());
            request.getSession().setAttribute(ROLE, user.getRole().getValue().toString());

            if (user.getRole().getValue().toString().equals("MANAGER")){
                return UriPath.REDIRECT + UriPath.MANAGER_ALL_REQUESTS;
            }
            return UriPath.REDIRECT + UriPath.CATALOG;
        } catch (UserIsBlockedException e) {
            request.setAttribute(STATUS, StatusesContainer.USER_BLOCKED_EXCEPTION);
            return JspFilePath.LOGIN;
        } catch (AuthenticationException e) {
            request.setAttribute(STATUS, StatusesContainer.FAILED_LOGIN_EXCEPTION);
            return JspFilePath.LOGIN;
        } catch (DataBaseException e) {
            request.setAttribute(STATUS, StatusesContainer.AUTHENTICATION_EXCEPTION);
            return JspFilePath.LOGIN;
        } catch (NotActivatedAccountException e) {
            request.setAttribute(STATUS, StatusesContainer.NOT_VERIFIED_ACCOUNT_EXCEPTION);
            return JspFilePath.LOGIN;
        }

    }
}
