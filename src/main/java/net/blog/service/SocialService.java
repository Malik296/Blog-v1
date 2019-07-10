package net.blog.service;

import net.blog.model.SocialAccount;

public interface SocialService {
    SocialAccount getSocialAccount(String authToken);
}
