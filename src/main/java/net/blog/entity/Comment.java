package net.blog.entity;

import java.sql.Timestamp;

public class Comment extends AbstractEntity<Long> {
    private static final long serialVersionUID = 4420833259305973122L;

    private Account account;
    private long idArticle;
    private String content;
    private Timestamp created;


    public Comment() {
    }

    public Comment(Account account, long idArticle, String content, Timestamp created) {
        this.account = account;
        this.idArticle = idArticle;
        this.content = content;
        this.created = created;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public long getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(long idArticle) {
        this.idArticle = idArticle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
}
