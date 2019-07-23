package com.task.db.core;

import com.task.model.Entity;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractEntityDao<T extends Entity> implements EntityDao<T> {
    protected Connection connection;
    protected String databaseName;
    protected String tableName;

    protected static final Logger log = Logger.getLogger(AbstractEntityDao.class);

    protected abstract String createInsertSqlQuery(T entity);

    protected abstract String createUpdateSqlQuery(T entity);

    protected abstract T createEntityFromDb(ResultSet rs) throws SQLException;

    @Override
    public boolean create(T entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        String sql = createInsertSqlQuery(entity);
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.execute();
            connection.commit();
            return true;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(T entity) {
        String sql = createUpdateSqlQuery(entity);
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            boolean result = stm.executeUpdate() > 0;
            connection.commit();
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(T... entities) {
        boolean result = true;
        for (T entity : entities) {
            result &= update(entity);
        }
        return result;
    }

    @Override
    public boolean delete(T entity) {
        String sql = String.format("DELETE FROM %s.%s WHERE id = '%s'",
                databaseName,
                tableName,
                entity.getId());
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            boolean result = stm.execute();
            connection.commit();
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public List<T> getAll() {
        String sql = String.format("SELECT * FROM %s.%s", databaseName, tableName);
        List<T> list = new ArrayList<>();
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                T entity = createEntityFromDb(rs);
                if (entity != null) {
                    list.add(entity);
                }
            }
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return list;
    }



    @Override
    public T getById(UUID id) {
        String sql = String.format("SELECT * FROM %s.%s where id = '%s'",
                databaseName, tableName, id);
        T entity = null;
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                entity = createEntityFromDb(rs);
            }
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return entity;
    }
}
