package model.service;

import com.pavlenko.kyrylo.model.dao.UserDao;
import com.pavlenko.kyrylo.model.dto.UserDto;
import com.pavlenko.kyrylo.model.entity.Role;
import com.pavlenko.kyrylo.model.entity.User;
import com.pavlenko.kyrylo.model.entity.util.PasswordEncoder;
import com.pavlenko.kyrylo.model.entity.util.Pbkdf2PasswordEncoder;
import com.pavlenko.kyrylo.model.exeption.AuthenticationException;
import com.pavlenko.kyrylo.model.exeption.DataBaseException;
import com.pavlenko.kyrylo.model.exeption.EmailIsAlreadyRegisteredException;
import com.pavlenko.kyrylo.model.exeption.UserIsBlockedException;
import com.pavlenko.kyrylo.model.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class UserServiceTest {

    UserDao userDao = mock(UserDao.class);
    PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();
    UserService userService = new UserService(passwordEncoder, userDao);


    private static final String FIRSTNAME = "FIRSTNAME";
    private static final String LASTNAME = "LASTNAME";
    private static final String EMAIL = "random.mail@gmail.com";
    private static final String PASSWORD = "Strongest_PASSWORD_31";

    private static final User UNBLOCKED_ACTIVATED_USER = User.builder()
            .id(null)
            .firstname(FIRSTNAME)
            .lastName(LASTNAME)
            .email(EMAIL)
            .password(PASSWORD)
            .role(new Role(Role.RoleEnum.CUSTOMER))
            .isBlocked(0)
            .isActivated(1)
            .build();

    private static final User UNBLOCKED_MANAGER = User.builder()
            .id(null)
            .firstname(FIRSTNAME)
            .lastName(LASTNAME)
            .email(EMAIL)
            .password(PASSWORD)
            .role(new Role(Role.RoleEnum.MANAGER))
            .isBlocked(0)
            .isActivated(1)
            .build();


    private static final User BLOCKED_USER = User.builder()
            .id(1L)
            .email(EMAIL)
            .password(PASSWORD)
            .isBlocked(1)
            .isActivated(1)
            .build();

    private static final UserDto USER_DTO = new UserDto(
            FIRSTNAME,
            LASTNAME,
            EMAIL,
            PASSWORD,
            PASSWORD
    );

    @Test
    void testAuthenticationShouldNotThrowException() throws DataBaseException {
        String password = passwordEncoder.encode(PASSWORD);
        when(userDao.findByUsernameAndPassword(EMAIL, password)).thenReturn(Optional.of(UNBLOCKED_ACTIVATED_USER));

        assertDoesNotThrow(() -> userService.authentication(EMAIL, PASSWORD));
    }

    @Test
    void testAuthenticationShouldThrowIsBlockedException() throws DataBaseException {
        String password = passwordEncoder.encode(PASSWORD);
        when(userDao.findByUsernameAndPassword(EMAIL, password)).thenReturn(Optional.of(BLOCKED_USER));

        assertThrows(
                UserIsBlockedException.class,
                () -> userService.authentication(EMAIL, PASSWORD)
        );
    }

    @Test
    void testAuthenticationShouldThrowAuthenticationException() throws DataBaseException {
        when(userDao.findByUsernameAndPassword(EMAIL, PASSWORD)).thenReturn(Optional.empty());

        assertThrows(
                AuthenticationException.class,
                () -> userService.authentication(EMAIL, PASSWORD)
        );
    }

    @Test
    void testRegisterNewAccountShouldWorkWithoutException() throws DataBaseException {
        when(userDao.emailAlreadyExists(EMAIL)).thenReturn(false);
        assertDoesNotThrow(() -> userService.registerNewAccount(USER_DTO));
    }

    @Test
    void testRegisterNewAccountShouldThrowUsernameIsReservedException() throws DataBaseException {
        when(userDao.emailAlreadyExists(EMAIL)).thenReturn(true);

        assertThrows(
                EmailIsAlreadyRegisteredException.class,
                () -> userService.registerNewAccount(USER_DTO)
        );
    }

    @Test
    void testRegisterNewManagerAccountShouldWorkWithoutException() throws DataBaseException {
        when(userDao.emailAlreadyExists(EMAIL)).thenReturn(false);
        assertDoesNotThrow(() -> userService.registerNewManagerAccount(USER_DTO));
    }

    @Test
    void testBlockUserById() throws DataBaseException {
        userService.blockById(1);
        verify(userDao, times(1)).blockById(1);
    }

    @Test
    void testUnBlockUserById() throws DataBaseException {
        userService.unblockById(1);
        verify(userDao, times(1)).unblockById(1);
    }

    @Test
    void testFindUserById() throws DataBaseException {
        userService.findById(1L);
        verify(userDao, times(1)).findById(1L);
    }

    @Test
    void testFindAllUsers() throws DataBaseException {
        userService.findAllUsers();
        verify(userDao, times(1)).findAll();
    }



}
