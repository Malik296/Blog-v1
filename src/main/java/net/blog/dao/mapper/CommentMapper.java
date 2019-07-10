package net.blog.dao.mapper;

import net.blog.entity.Account;
import net.blog.entity.Comment;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentMapper extends AbstractMapper<Comment> {
    private final boolean hasAccountData;

    public CommentMapper(boolean hasAccountData) {
        this.hasAccountData = hasAccountData;
    }

    @Override
    public Comment handleItem(ResultSet rs) throws SQLException {
        Comment comment = convert.toBean(rs, Comment.class);
        comment.setIdArticle(rs.getLong("id_article"));
        if (hasAccountData) {
            Account account = convert.toBean(rs, Account.class);
            account.setId(rs.getLong("id_account"));
            account.setCreated(rs.getTimestamp("accountCreated"));
            comment.setAccount(account);
        }
        return comment;
    }
}
