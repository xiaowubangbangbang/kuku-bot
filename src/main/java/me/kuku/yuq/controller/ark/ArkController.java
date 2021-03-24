package me.kuku.yuq.controller.ark;

import com.IceCreamQAQ.Yu.annotation.Action;
import com.icecreamqaq.yuq.annotation.GroupController;
import com.icecreamqaq.yuq.annotation.QMsg;
import com.icecreamqaq.yuq.entity.Contact;

import javax.inject.Inject;
import java.util.List;

@GroupController
public class ArkController {
    @Inject
    ArkService ark;

    @Action("{pool}单抽")
    @QMsg(at = true)
    public String cardOne(Contact qq, String pool) {
        ArkNightsPool arkNightsPool = new ArkPools().getHashMap().get(pool);
        UserRecord record = ark.getUserRecord(qq.getId(), arkNightsPool);
        List<String> list = ark.card(record, 1);
        StringBuilder sb = new StringBuilder("您的单抽抽卡结果为：");
        for (String s : list) {
            sb.append("\n").append(s);
        }
        if (arkNightsPool.description() != null) {
            sb.append("\n").append(arkNightsPool.description());
        }
        return sb.toString();
    }

    @Action("{pool}十连")
    @QMsg(at = true)
    public String cardTen(Contact qq, String pool) {
        ArkNightsPool arkNightsPool = new ArkPools().getHashMap().get(pool);
        if (arkNightsPool == null) {
            return "池子不存在";
        }
        UserRecord record = ark.getUserRecord(qq.getId(), arkNightsPool);
        List<String> list = ark.card(record, 10);
        StringBuilder sb = new StringBuilder("您的十连抽卡结果为：");
        for (String s : list) {
            sb.append("\n").append(s);
        }
        if (arkNightsPool.description() != null) {
            sb.append("\n").append(arkNightsPool.description());
        }
        return sb.toString();
    }

}
