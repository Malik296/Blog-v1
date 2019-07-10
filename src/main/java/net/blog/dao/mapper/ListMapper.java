package net.blog.dao.mapper;

import org.apache.commons.dbutils.handlers.AbstractListHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ListMapper<T> extends AbstractListHandler<T> {
    private AbstractMapper<T> handler;

    public ListMapper(AbstractMapper<T> handler) {
        this.handler = handler;
        this.handler.shouldBeIterateThroughResultSet = false;
    }

    @Override
    protected T handleRow(ResultSet rs) throws SQLException {
        return handler.handle(rs);
    }
}
