package com.mycompany.webhuongdannauan.dao;

import com.mycompany.webhuongdannauan.model.User;
import java.util.List;
import java.util.Map;

public interface UserDAO extends GenericDAO<User, Long> {

    // Phương thức đặc thù cho User
    User findByUsername(String username);
    User findByEmail(String email);
    User findByEmailAndNotId(String email, Long excludeId);
    // Thao tác Follow
    void saveFollow(Long followerId, Long followedId);
    void deleteFollow(Long followerId, Long followedId);
    
    boolean isFollowing(Long followerId, Long followedId); 
    
    long countAllUsers();
    long countPremiumUsers();
    Map<String, Long> countUsersByMonth();
    List<User> getAllUsers();
    void addUser(String username, String email, String password, String role);
    void deleteUser(int id);
}
