package com.pavlenko.kyrylo.controller.command.impl.manager;

import com.pavlenko.kyrylo.controller.command.Command;
import com.pavlenko.kyrylo.controller.util.JspFilePath;
import com.pavlenko.kyrylo.controller.validator.statuses.StatusesContainer;
import com.pavlenko.kyrylo.model.entity.Booking;
import com.pavlenko.kyrylo.model.exeption.DataBaseException;
import com.pavlenko.kyrylo.model.service.BookingService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.pavlenko.kyrylo.controller.util.ConstantsContainer.*;

/**
 * Return page with requests on which the logged-in manager is working.
 */
public class GetMyRequestsCommand implements Command {

    BookingService bookingService;

    public GetMyRequestsCommand(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        Long managerId = (Long) request.getSession().getAttribute(USER_ID);
        try {
            List<Booking> bookingList = bookingService.findAllRequestsByManagerId(managerId);
            request.getSession().setAttribute(BOOKING_LIST, bookingList);
        } catch (DataBaseException e) {
            request.setAttribute(STATUS, StatusesContainer.BOOKING_INFO_LOADING_EXCEPTION);
        }
        return JspFilePath.MANAGER_MY_REQUESTS;
    }
}
