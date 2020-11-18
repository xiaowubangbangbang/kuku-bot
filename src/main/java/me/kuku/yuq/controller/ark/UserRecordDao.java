package me.kuku.yuq.controller.ark;

import com.icecreamqaq.yudb.jpa.JPADao;
import com.icecreamqaq.yudb.jpa.annotation.Dao;

@Dao
public interface UserRecordDao extends JPADao<UserRecord, Integer> {
    UserRecord findByQqAndPool(Long qq, String pool);
}
