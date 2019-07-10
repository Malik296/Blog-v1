package net.blog.exception;

public class RedirectToValidUrlException extends Exception {

    private static final long serialVersionUID = -2658183321317838935L;

    private String requestUrl;


    public RedirectToValidUrlException(String url) {
        super("Should be redirect to " + url);
        this.requestUrl = url;
    }

    public String  getUrl() {
        return requestUrl;
    }
}
