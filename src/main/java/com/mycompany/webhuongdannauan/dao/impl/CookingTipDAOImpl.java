package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.dao.CookingTipDAO;
import com.mycompany.webhuongdannauan.model.CookingTip;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class CookingTipDAOImpl implements CookingTipDAO {

    @Override
    public List<CookingTip> getAllTips() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("FROM CookingTip", CookingTip.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public CookingTip getTipById(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(CookingTip.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public void saveTip(CookingTip tip) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(tip);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void updateTip(CookingTip tip) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tip);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteTip(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            CookingTip tip = em.find(CookingTip.class, id);
            if (tip != null) em.remove(tip);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
