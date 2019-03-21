package org.openchat.domain.repository;

import org.openchat.domain.User;

public interface UserRepository {


    public boolean isUsernameUsed(String username);

    public String add(User newUser);
}
