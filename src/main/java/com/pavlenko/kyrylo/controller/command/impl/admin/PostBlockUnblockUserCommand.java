package com.pavlenko.kyrylo.controller.command.impl.admin;

import com.pavlenko.kyrylo.controller.command.Command;
import com.pavlenko.kyrylo.controller.util.JspFilePath;
import com.pavlenko.kyrylo.controller.util.UriPath;
import com.pavlenko.kyrylo.model.exeption.DataBaseException;
import com.pavlenko.kyrylo.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

import static com.pavlenko.kyrylo.controller.util.ConstantsContainer.*;

/**
 * Process blocking and unblocking user.
 */
public class PostBlockUnblockUserCommand implements Command {

    private final UserService userService;

    public PostBlockUnblockUserCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getParameter(ID));
        boolean isBlocked = Boolean.parseBoolean(request.getParameter(IS_BLOCKED));

        if (!isBlocked){
            try {
                userService.blockById(userId);
            } catch (DataBaseException e) {
                return JspFilePath.ADMIN_USERS;
            }
        } else {
            try {
                userService.unblockById(userId);
            } catch (DataBaseException e) {
                return JspFilePath.ADMIN_USERS;
            }
        }
        return UriPath.REDIRECT + UriPath.ADMIN_USER_LIST;
    }
}
