package com.pavlenko.kyrylo.controller.command.impl.guest;

import com.pavlenko.kyrylo.controller.command.Command;
import com.pavlenko.kyrylo.controller.command.impl.mapper.UserDetailsMapper;
import com.pavlenko.kyrylo.controller.util.JspFilePath;
import com.pavlenko.kyrylo.controller.util.UriPath;
import com.pavlenko.kyrylo.controller.validator.UserValidator;
import com.pavlenko.kyrylo.controller.validator.statuses.StatusesContainer;
import com.pavlenko.kyrylo.model.dto.UserDto;
import com.pavlenko.kyrylo.model.exeption.EmailIsAlreadyRegisteredException;
import com.pavlenko.kyrylo.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class PostRegistrationCommand implements Command {
    private final UserDetailsMapper userDetailsMapper = new UserDetailsMapper();
    private final UserService userService;

    public PostRegistrationCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {

        UserDto userDto = userDetailsMapper.fetchUserDtoFromRequest(request);
        boolean userValid = UserValidator.validate(userDto, request);

        if (userValid) {
            try {
                userService.registerNewAccount(userDto);
                return UriPath.REDIRECT + UriPath.LOGIN;
            } catch (EmailIsAlreadyRegisteredException e) {
                request.setAttribute("status", StatusesContainer.EMAIL_IS_RESERVED_EXCEPTION);
            }
        }
        return JspFilePath.REGISTRATION;
    }
}
