package com.pavlenko.kyrylo.controller.listener;

import com.pavlenko.kyrylo.controller.command.Command;
import com.pavlenko.kyrylo.controller.command.impl.admin.*;
import com.pavlenko.kyrylo.controller.command.impl.common.GetCatalogCommand;
import com.pavlenko.kyrylo.controller.command.impl.common.GetHomeCommand;
import com.pavlenko.kyrylo.controller.command.impl.common.GetLogOutCommand;
import com.pavlenko.kyrylo.controller.command.impl.customer.GetRentCarCommand;
import com.pavlenko.kyrylo.controller.command.impl.customer.GetRentRequestsCommand;
import com.pavlenko.kyrylo.controller.command.impl.customer.PostRentCarCommand;
import com.pavlenko.kyrylo.controller.command.impl.customer.TerminateRequestCommand;
import com.pavlenko.kyrylo.controller.command.impl.guest.GetLoginCommand;
import com.pavlenko.kyrylo.controller.command.impl.guest.GetRegistrationCommand;
import com.pavlenko.kyrylo.controller.command.impl.guest.PostLoginCommand;
import com.pavlenko.kyrylo.controller.command.impl.guest.PostRegistrationCommand;
import com.pavlenko.kyrylo.controller.command.impl.manager.*;
import com.pavlenko.kyrylo.controller.util.UriPath;
import com.pavlenko.kyrylo.model.dao.BookingDao;
import com.pavlenko.kyrylo.model.dao.CarDao;
import com.pavlenko.kyrylo.model.dao.UserDao;
import com.pavlenko.kyrylo.model.dao.impl.BookingDaoImpl;
import com.pavlenko.kyrylo.model.dao.impl.CarDaoImpl;
import com.pavlenko.kyrylo.model.dao.impl.UserDaoImpl;
import com.pavlenko.kyrylo.model.service.BookingService;
import com.pavlenko.kyrylo.model.service.CarService;
import com.pavlenko.kyrylo.model.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionListener;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@WebListener
public class ContextListener implements ServletContextListener, HttpSessionListener {

    private static final Logger logger = LogManager.getLogger(ContextListener.class);

    private static final String DATASOURCE = "dataSource";
    private static final String USER_SERVICE = "userService";
    private static final String CAR_SERVICE = "carService";
    private static final String BOOKING_SERVICE = "bookingService";


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Start context initialization...");
        ServletContext context = sce.getServletContext();
        initDatasource(context);
        logger.info("DataSource initialized...");
        initServices(context);
        logger.info("Services initialized...");
        initCommands(context);
        logger.info("Commands initialized...");
    }

    private void initDatasource(ServletContext context) {
        String dataSourceName = context.getInitParameter(DATASOURCE);
        Context jndiContext = null;
        try {
            jndiContext = (Context) new InitialContext().lookup("java:/comp/env");
            DataSource dataSource = (DataSource) jndiContext.lookup(dataSourceName);
            context.setAttribute(DATASOURCE, dataSource);
        } catch (NamingException e) {
            throw new IllegalStateException("Cannot initialize dataSource", e);
        }
    }

    private void initServices(ServletContext context) {
        UserDao userDao = new UserDaoImpl((DataSource) context.getAttribute(DATASOURCE));
        UserService userService = new UserService(userDao);
        context.setAttribute(USER_SERVICE, userService);

        CarDao carDao = new CarDaoImpl((DataSource) context.getAttribute(DATASOURCE));
        CarService carService = new CarService(carDao);
        context.setAttribute(CAR_SERVICE, carService);

        BookingDao bookingDao = new BookingDaoImpl((DataSource) context.getAttribute(DATASOURCE));
        BookingService bookingService = new BookingService(bookingDao);
        context.setAttribute(BOOKING_SERVICE, bookingService);
    }

    private void initCommands(ServletContext context) {
        Map<String, Command> getCommands = new HashMap<>();
        Map<String, Command> postCommands = new HashMap<>();

        putGetCommands(getCommands, context);
        putPostCommands(postCommands, context);

        context.setAttribute("getCommands", getCommands);
        context.setAttribute("postCommands", postCommands);
    }

    private void putGetCommands(Map<String, Command> commands, ServletContext context) {
        commands.put(UriPath.LOGIN, new GetLoginCommand());
        commands.put(UriPath.LOGOUT, new GetLogOutCommand());
        commands.put(UriPath.REGISTRATION, new GetRegistrationCommand());
        commands.put(UriPath.CATALOG, new GetCatalogCommand((CarService) context.getAttribute(CAR_SERVICE)));
        commands.put(UriPath.INDEX, new GetHomeCommand());
        commands.put(UriPath.ADMIN_USER_LIST, new GetAllUsersCommand((UserService) context.getAttribute(USER_SERVICE)));
        commands.put(UriPath.ADMIN_REGISTER_MANAGER, new GetManagerRegistrationCommand());
        commands.put(UriPath.ADMIN_CAR_EDIT, new GetEditCarCommand((CarService) context.getAttribute(CAR_SERVICE)));
        commands.put(UriPath.ADMIN_ADD_NEW_CAR, new GetAddNewCarCommand((CarService) context.getAttribute(CAR_SERVICE)));
        commands.put(UriPath.CUSTOMER_RENT_CAR, new GetRentCarCommand((CarService) context.getAttribute(CAR_SERVICE)));
        commands.put(UriPath.CUSTOMER_REQUESTS, new GetRentRequestsCommand((BookingService) context.getAttribute(BOOKING_SERVICE), (CarService) context.getAttribute(CAR_SERVICE)));
        commands.put(UriPath.MANAGER_ALL_REQUESTS, new GetAllRequestsCommand((BookingService) context.getAttribute(BOOKING_SERVICE)));
        commands.put(UriPath.MANAGER_MY_REQUESTS, new GetMyRequestsCommand((BookingService) context.getAttribute(BOOKING_SERVICE)));
        commands.put(UriPath.MANAGER_REGISTER_RETURN, new GetRegisterReturnCommand());
    }

    private void putPostCommands(Map<String, Command> commands, ServletContext context) {
        commands.put(UriPath.LOGIN,
                new PostLoginCommand((UserService) context.getAttribute(USER_SERVICE)));
        commands.put(UriPath.REGISTRATION,
                new PostRegistrationCommand((UserService) context.getAttribute(USER_SERVICE)));
        commands.put(UriPath.ADMIN_BLOCK_UNBLOCK_USER,
                new PostBlockUnblockUserCommand((UserService) context.getAttribute(USER_SERVICE)));
        commands.put(UriPath.ADMIN_REGISTER_MANAGER,
                new PostManagerRegistrationCommand((UserService) context.getAttribute(USER_SERVICE)));
        commands.put(UriPath.ADMIN_CAR_EDIT,
                new PostEditCarCommand((CarService) context.getAttribute(CAR_SERVICE)));
        commands.put(UriPath.ADMIN_CAR_DELETE,
                new PostDeleteCarCommand((CarService) context.getAttribute(CAR_SERVICE)));
        commands.put(UriPath.ADMIN_ADD_NEW_CAR,
                new PostAddNewCarCommand((CarService) context.getAttribute(CAR_SERVICE)));
        commands.put(UriPath.CUSTOMER_RENT_CAR,
                new PostRentCarCommand(
                        (BookingService) context.getAttribute(BOOKING_SERVICE),
                        (UserService) context.getAttribute(USER_SERVICE),
                        (CarService) context.getAttribute(CAR_SERVICE)));
        commands.put(UriPath.CUSTOMER_TERMINATE_REQUEST,
                new TerminateRequestCommand((BookingService) context.getAttribute(BOOKING_SERVICE)));
        commands.put(UriPath.MANAGER_TAKE_ON_REVIEW,
                new PostTakeOnReviewCommand((BookingService) context.getAttribute(BOOKING_SERVICE)));
        commands.put(UriPath.MANAGER_ACCEPT_REQUEST,
                new PostAcceptRequestCommand((BookingService) context.getAttribute(BOOKING_SERVICE)));
        commands.put(UriPath.MANAGER_REGISTER_RETURN,
                new PostRegisterReturnCommand((BookingService) context.getAttribute(BOOKING_SERVICE)));
        commands.put(UriPath.MANAGER_DECLINE_REQUEST,
                new PostDeclineRequestCommand((BookingService) context.getAttribute(BOOKING_SERVICE)));
    }
}
