package com.vaka.practice.dao;

import com.vaka.practice.domain.Entity;
import com.vaka.practice.domain.EntityTable;
import com.vaka.practice.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.vaka.practice.dao.JdbcUtils.getConnection;

@Slf4j
public class JdbcEntityDao implements EntityDao {
    @Override
    public void create(Entity entity) {
        String sql = "INSERT INTO Entity (name, description, createdAt, updatedAt) VALUES (?, ?, ?, ?);";

        try (var con = getConnection();
             var pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, entity.getName());
            pstmt.setString(2, entity.getDescription());

            Date createdAt = Date.valueOf(LocalDate.now());
            pstmt.setDate(3, createdAt);

            Date updatedAt = Date.valueOf(LocalDate.now());
            pstmt.setDate(4, updatedAt);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.info("state: {}", e.getSQLState());
            log.info("errorCode: {}", e.getErrorCode());
            log.info("message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Entity findById(Integer id) throws EntityNotFoundException {
        String sql = "SELECT * FROM Entity WHERE id = ?;";
        Entity entity = null;

        try (var con = getConnection();
             var pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    entity = mapEntity(rs);
                } else {
                    throw new EntityNotFoundException(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return entity;
    }

    @Override
    public List<Entity> findAll() {
        String sql = "SELECT * FROM Entity";
         List<Entity> entities = new ArrayList<>();

        try (var con = getConnection();
             var pstmt = con.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Entity entity = mapEntity(rs);
                    entities.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return entities;
    }

    @Override
    public void update(Entity entity) throws EntityNotFoundException {
        String sql = "UPDATE Entity SET name = ?, description = ?, createdAt = ?, updatedAt = ? WHERE id = ?;";

        try (var con = getConnection();
             var pstmt = con.prepareStatement(sql)) {
            setEntityToPstmtUpdate(pstmt, entity);

            int executed = pstmt.executeUpdate();
            if (executed == 0) {
                throw new EntityNotFoundException(entity.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setEntityToPstmtUpdate(PreparedStatement pstmt, Entity entity) {
        try {
            pstmt.setInt(5, entity.getId());
            pstmt.setString(1, entity.getName());
            pstmt.setString(2, entity.getDescription());

            pstmt.setDate(3, Date.valueOf(entity.getCreatedAt()));

            Date updatedAt = Date.valueOf(LocalDate.now());
            pstmt.setDate(4, updatedAt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) throws EntityNotFoundException {
        String sql = "DELETE FROM Entity WHERE id = ?;";

        try (var con = getConnection();
             var pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int executed = pstmt.executeUpdate();
            if (executed == 0) {
                throw new EntityNotFoundException(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int count() {
        String sql = "SELECT COUNT(*) as cnt FROM Entity ";

        try (var con = getConnection();
             var pstmt = con.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt");
                }
            };
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    private LocalDate mapDate(ResultSet rs, String columnName) throws SQLException {
        long timestamp = rs.getLong(columnName);
        Instant instant = Instant.ofEpochMilli(timestamp);
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Entity mapEntity(ResultSet rs) {
        try {
            Integer entityId = rs.getInt("id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            LocalDate createdAt = mapDate(rs, "createdAt");
            LocalDate updatedAt = mapDate(rs, "updatedAt");

            return new Entity(entityId, name, description, createdAt, updatedAt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
