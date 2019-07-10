package net.blog.dao.mapper;

import net.blog.entity.Article;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticleMapper extends AbstractMapper<Article> {


    @Override
    public Article handleItem(ResultSet rs) throws SQLException {
        Article a = convert.toBean(rs, Article.class);
        a.setIdCategory(rs.getInt("id_category"));
        return a;
    }
}
