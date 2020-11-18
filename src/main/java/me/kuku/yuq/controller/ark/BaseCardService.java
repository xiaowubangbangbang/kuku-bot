package me.kuku.yuq.controller.ark;

import com.icecreamqaq.yudb.jpa.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseCardService {
    @Inject
    private UserRecordDao ud;

    protected abstract CardPool getPool(String pool);

    protected abstract CardSettle invoke(UserRecord userRecord);


    @Transactional
    public UserRecord getUserRecord(Long qq, CardPool cardPool) {
        UserRecord record = ud.findByQqAndPool(qq, cardPool.getName());
        if (record == null) {
            record = new UserRecord();
            record.setQq(qq);
            record.setPool(cardPool.getName());
            ud.save(record);
        }
        return record;
    }

    @Transactional
    public List<String> card(UserRecord record, int num) {
        CardPool pool = getPool(record.getPool());
        ArrayList<String> list = new ArrayList<>(10);
        for (int i = 0; i < num; i++) {
            list.add(pool.invoke(invoke(record)));
        }
        ud.saveOrUpdate(record);
        return list;
    }
}
