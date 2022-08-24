package com.pavlenko.kyrylo.controller.command.impl.admin;

import com.pavlenko.kyrylo.controller.command.Command;
import com.pavlenko.kyrylo.controller.util.JspFilePath;
import com.pavlenko.kyrylo.controller.util.UriPath;
import com.pavlenko.kyrylo.controller.validator.EditCarValidator;
import com.pavlenko.kyrylo.controller.validator.statuses.StatusesContainer;
import com.pavlenko.kyrylo.model.exeption.DataBaseException;
import com.pavlenko.kyrylo.model.service.CarService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import static com.pavlenko.kyrylo.controller.util.ConstantsContainer.*;

public class PostEditCarCommand implements Command {

    private final CarService carService;
    private static final String LABEL = "label";
    private static final String INPUT = "input";
    private static final String INPUT_ID = "inputID";

    private final Logger logger = LogManager.getLogger(PostEditCarCommand.class);
    public PostEditCarCommand(CarService carService) {
        this.carService = carService;
    }


    @Override
    public String execute(HttpServletRequest request) {
        String label = request.getParameter(LABEL);
        String input = request.getParameter(INPUT);
        Long id = Long.valueOf(request.getParameter(ID));
        Long inputID = Long.valueOf(request.getParameter(INPUT_ID));

        request.getSession().setAttribute(ID, id);

        boolean isValid = true;
        if (!input.equals("empty")){
            isValid = EditCarValidator.validate(label, input, request);
        }
        if (isValid){
            try {
                carService.editCarInfo(label, input, id, inputID);
                request.getSession().removeAttribute(ID);
                request.getSession().removeAttribute("qualityClassList");
                request.getSession().removeAttribute("statusList");
                return UriPath.REDIRECT + UriPath.CATALOG;
            } catch (DataBaseException e) {
                request.setAttribute(STATUS, StatusesContainer.FAILED_EDIT_CAR_EXCEPTION);
                logger.error("Failed to edit car details, because: {}", e.getMessage());
            }
        }
        return JspFilePath.ADMIN_CAR_MANAGEMENT;
    }
}
