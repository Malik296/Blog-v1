package net.blog.dao.mapper;

import net.blog.entity.Category;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MapCategoryMapper implements ResultSetHandler<Map<Integer, Category>> {
    private RowProcessor convert = new BasicRowProcessor();

    @Override
    public Map<Integer, Category> handle(ResultSet resultSet) throws SQLException {
        Map<Integer, Category> map = new HashMap<>();
        while (resultSet.next()){
            Category category = convert.toBean(resultSet, Category.class);
            map.put(category.getId(), category);
        }
        return map;
    }
}
