package com.pavlenko.kyrylo.model.dao.mapper;

import com.pavlenko.kyrylo.model.dao.mapper.field.Fields;
import com.pavlenko.kyrylo.model.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    private final RoleMapper roleMapper = new RoleMapper();

    public User extractFromResultSet(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong(Fields.USER_ID))
                .firstname(rs.getString(Fields.FIRST_NAME))
                .lastName((rs.getString(Fields.LAST_NAME)))
                .email(rs.getString(Fields.EMAIL))
                .role(roleMapper.extractFromResultSet(rs, Fields.USER_ROLE_ID))
                .isBlocked(rs.getInt(Fields.IS_BLOCKED))
                .isActivated(rs.getInt(Fields.IS_ACTIVATED))
                .build();
    }
    public User extractFromResultSet(ResultSet rs, String id) throws SQLException {
        return User.builder()
                .id(rs.getLong(id))
                .firstname(rs.getString(Fields.FIRST_NAME))
                .lastName((rs.getString(Fields.LAST_NAME)))
                .email(rs.getString(Fields.EMAIL))
                .role(roleMapper.extractFromResultSet(rs, Fields.USER_ROLE_ID))
                .isBlocked(rs.getInt(Fields.IS_BLOCKED))
                .isActivated(rs.getInt(Fields.IS_ACTIVATED))
                .build();
    }
}
