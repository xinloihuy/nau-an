package com.mycompany.webhuongdannauan.service;

import com.mycompany.webhuongdannauan.dao.impl.GenericDAOImpl;
import com.mycompany.webhuongdannauan.model.PremiumAccount;
import com.mycompany.webhuongdannauan.model.PremiumPackage;
import com.mycompany.webhuongdannauan.model.User;
import java.util.Calendar;
import java.util.Date;

public class PremiumAccountService {
    
    private final GenericDAOImpl<PremiumAccount, Long> premiumAccountDAO;

    public PremiumAccountService() {
        this.premiumAccountDAO = new GenericDAOImpl<PremiumAccount, Long>() {};
    }
    
    public void updateUserPremiumStatus(User user, PremiumPackage pkg) {
        // Lấy PremiumAccount hiện tại (nếu có)
        PremiumAccount currentAccount = user.getPremiumAccount();
        Date newStartDate;
        
        // Tính ngày bắt đầu mới
        if (currentAccount != null && currentAccount.getEndDate().after(new Date())) {
            // Tiếp tục từ ngày hết hạn của gói cũ
            newStartDate = currentAccount.getEndDate();
        } else {
            newStartDate = new Date(); // Bắt đầu từ hôm nay
        }

        // Tính ngày kết thúc mới
        Calendar cal = Calendar.getInstance();
        cal.setTime(newStartDate);
        cal.add(Calendar.DAY_OF_MONTH, pkg.getDurationInDays());
        Date newEndDate = cal.getTime();

        // Tạo hoặc cập nhật
        if (currentAccount == null) {
            currentAccount = new PremiumAccount();
            currentAccount.setUser(user);
        }
        
        currentAccount.setStartDate(newStartDate);
        currentAccount.setEndDate(newEndDate);
        currentAccount.setIsActive(true);
        
        // Lưu/Cập nhật PremiumAccount qua Generic DAO
        premiumAccountDAO.save(currentAccount);
    }
    
    public boolean isUserPremium(User user) {
        PremiumAccount account = user.getPremiumAccount();
        return account != null && account.getIsActive() && account.getEndDate().after(new Date());
    }
}