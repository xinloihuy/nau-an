package com.mycompany.webhuongdannauan.service;

import com.mycompany.webhuongdannauan.dao.UserDAO;
import com.mycompany.webhuongdannauan.dao.impl.GenericDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.UserDAOImpl;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.model.AccountSetting;
import com.mycompany.webhuongdannauan.model.PremiumAccount;
import com.mycompany.webhuongdannauan.model.Role;
import com.mycompany.webhuongdannauan.utils.SecurityUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class UserService {

    private final UserDAO userDAO;
    private final GenericDAOImpl<Role, Long> roleDAO;
    private final GenericDAOImpl<AccountSetting, Long> accountSettingDAO;
    private final GenericDAOImpl<PremiumAccount, Long> premiumAccountDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
        this.roleDAO = new GenericDAOImpl<Role, Long>() {}; // Generic DAO cho Role
        this.accountSettingDAO = new GenericDAOImpl<AccountSetting, Long>() {}; // Generic DAO cho AccountSetting
        this.premiumAccountDAO = new GenericDAOImpl<PremiumAccount, Long>() {}; // Generic DAO cho PremiumAccount
    }

    // --- Phương thức tìm kiếm cơ bản ---
    public User findByEmailAndNotId(String email, Long excludeId) {
        if (email == null || excludeId == null || excludeId <= 0) {
            return null;
        }
        return userDAO.findByEmailAndNotId(email, excludeId);
    }

    public User findByEmail(String email) {
        if (email == null) {
            return null;
        }
        return userDAO.findByEmail(email);
    }

    public void saveUser(User user) {
        userDAO.save(user);
    }
    public User findUserById(Long userId) {
        return userDAO.findById(userId);
    }

    // --- 1. Đăng ký (Register) ---

    public User registerUser(User newUser) {
        // 1. Kiểm tra tồn tại
        if (userDAO.findByUsername(newUser.getUsername()) != null || userDAO.findByEmail(newUser.getEmail()) != null) {
            return null; // Username hoặc Email đã tồn tại
        }

        // 2. Mã hóa mật khẩu
        String hashedPassword = SecurityUtil.hashPassword(newUser.getPassword());
        newUser.setPassword(hashedPassword);

        // 3. Gán Role mặc định (USER)
        Role defaultRole = roleDAO.findById(3L); // Giả định ID 3 là 'USER'
        if (defaultRole == null) {
            // Xử lý nếu Role không tồn tại (cần khởi tạo dữ liệu gốc)
            throw new RuntimeException("Default 'USER' role not found in database.");
        }
        newUser.setRoles(new HashSet<>(Collections.singletonList(defaultRole)));

        // 4. Tạo AccountSetting mặc định (Composition)
        AccountSetting setting = new AccountSetting();
        setting.setUser(newUser);
        newUser.setAccountSetting(setting);

        // 5. Lưu User
        userDAO.save(newUser);
        
        return newUser;
    }

    // --- 2. Đăng nhập (Login) ---

    public User login(String usernameOrEmail, String password) {
        // Tìm User theo username hoặc email
        User user = userDAO.findByUsername(usernameOrEmail);
        if (user == null) {
            user = userDAO.findByEmail(usernameOrEmail);
        }

        if (user != null) {
            // Kiểm tra mật khẩu
            if (SecurityUtil.verifyPassword(password, user.getPassword())) {
                // Kiểm tra xem AccountSetting có bị đánh dấu xóa không
                AccountSetting setting = user.getAccountSetting();
                if (setting != null && setting.getIsDeleted()) {
                    return null; // Tài khoản đã bị xóa/vô hiệu hóa
                }
                return user; // Đăng nhập thành công
            }
        }
        return null; // Sai thông tin đăng nhập
    }

    // --- 3. Account Settings ---
    
    public boolean updateAccount(Long userId, String nickname, String email, Integer age) {
        User user = userDAO.findById(userId);
        if (user != null) {
            // Kiểm tra email trùng lặp (trừ chính email cũ của user)
            User existingEmailUser = userDAO.findByEmail(email);
            if (existingEmailUser != null && !existingEmailUser.getId().equals(userId)) {
                return false; // Email đã được sử dụng
            }
            
            user.setNickname(nickname);
            user.setEmail(email);
            user.setAge(age);
            userDAO.save(user); // Phương thức save() trong DAO đã xử lý cả UPDATE
            return true;
        }
        return false;
    }

    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userDAO.findById(userId);
        if (user != null && SecurityUtil.verifyPassword(oldPassword, user.getPassword())) {
            // Mã hóa và cập nhật mật khẩu mới
            String hashedPassword = SecurityUtil.hashPassword(newPassword);
            user.setPassword(hashedPassword);
            userDAO.save(user);
            return true;
        }
        return false;
    }
    
    public boolean deleteAccount(Long userId) {
        User user = userDAO.findById(userId);
        if (user != null) {
            // Xóa mềm bằng cách cập nhật AccountSetting
            AccountSetting setting = user.getAccountSetting();
            if (setting != null) {
                setting.setIsDeleted(true);
                accountSettingDAO.save(setting);
                // Lưu ý: User vẫn còn trong DB, chỉ bị vô hiệu hóa
                return true;
            }
        }
        return false;
    }

    // --- 4. Quản lý Premium Account (Dùng bởi TransactionService) ---

    public void savePremiumAccount(PremiumAccount account) {
        premiumAccountDAO.save(account);
    }
    
    public boolean isUserPremium(User user) {
        // Gọi PremiumAccountService để kiểm tra trạng thái Premium
        // (Giả định bạn tạo ra PremiumAccountService để xử lý logic này)
        // new PremiumAccountService().isUserPremium(user)
        
        // Hiện tại: Kiểm tra thủ công
        PremiumAccount account = user.getPremiumAccount();
        if (account == null || !account.getIsActive()) {
            return false;
        }
        // Kiểm tra ngày hết hạn
        return account.getEndDate().after(new java.util.Date());
    }

    // --- 5. Chức năng Follow ---
    
    public void followAuthor(Long followerId, Long followedId) {
        if (!followerId.equals(followedId)) {
            userDAO.saveFollow(followerId, followedId);
        }
    }
    
    public void unfollowAuthor(Long followerId, Long followedId) {
        userDAO.deleteFollow(followerId, followedId);
    }
    
    // --- 6. Quản lý Admin ---
    
    public List<User> findAllUsers() {
        return userDAO.findAll();
    }
    
    public boolean isAdmin(User user) {
    if (user == null || user.getRoles() == null) {
        return false;
    }
    // Giả định Role 'ADMIN' có ID 1 hoặc bạn tìm theo tên
    return user.getRoles().stream().anyMatch(role -> "ADMIN".equalsIgnoreCase(role.getName()));
}
    
    // ... các phương thức quản lý người dùng khác cho Admin
}
