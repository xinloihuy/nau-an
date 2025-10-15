package com.mycompany.webhuongdannauan.dao;

import com.mycompany.webhuongdannauan.model.CookingTip;
import java.util.List;

public interface CookingTipDAO {
    List<CookingTip> getAllTips();
    CookingTip getTipById(Long id);
    void saveTip(CookingTip tip);
    void updateTip(CookingTip tip);
    void deleteTip(Long id);
}
