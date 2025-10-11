package com.mycompany.webhuongdannauan.dao;

import com.mycompany.webhuongdannauan.model.User;

public interface UserDAO extends GenericDAO<User, Long> {

    // Phương thức đặc thù cho User
    User findByUsername(String username);
    User findByEmail(String email);
    
    // Thao tác Follow
    void saveFollow(Long followerId, Long followedId);
    void deleteFollow(Long followerId, Long followedId);
}