package com.pavlenko.kyrylo.model.dao.impl;

import com.pavlenko.kyrylo.model.dao.UserDao;
import com.pavlenko.kyrylo.model.dao.impl.query.UserQueries;
import com.pavlenko.kyrylo.model.dao.mapper.UserMapper;
import com.pavlenko.kyrylo.model.entity.Role;
import com.pavlenko.kyrylo.model.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UserDaoImpl implements UserDao {

    private final Logger LOG = LogManager.getLogger(UserDaoImpl.class);

    private final UserMapper userMapper = new UserMapper();

    @Override
    public void create(User entity) {

        try (Connection con = ConnectionDB.getConnection();
             PreparedStatement statement = con.prepareStatement(UserQueries.CREATE_USER)) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPassword());
            statement.setString(5, entity.getRole().getValue().name());

            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("{}, when trying to create new User", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public void update(User entity) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Optional<User> findByUsernameAndPassword(String email, String password) {
        try (Connection con = ConnectionDB.getConnection();
             PreparedStatement statement = con.prepareStatement(UserQueries.FIND_USER_BY_EMAIL_AND_PASSWORD)) {
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return Optional.of(userMapper.extractFromResultSet(rs));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAllUsers() {
        try (Connection con = ConnectionDB.getConnection();
             PreparedStatement statement = con.prepareStatement(UserQueries.FIND_ALL_USERS)) {
            List<User> userList = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = userMapper.extractFromResultSet(resultSet);
                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void blockById(Long id) {

    }

    @Override
    public void unblockById(Long id) {

    }

    @Override
    public boolean uniqueEmail(String email) {
        try (Connection con = ConnectionDB.getConnection();
             PreparedStatement statement = con.prepareStatement(UserQueries.FIND_BY_EMAIL)) {
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            LOG.error("{}, when trying to find User by username={}", e.getMessage(), email);
            throw new RuntimeException(e);
        }
    }
}