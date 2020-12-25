package me.kuku.yuq.controller.genshin;

import com.IceCreamQAQ.Yu.annotation.Action;
import com.icecreamqaq.yuq.annotation.QMsg;
import com.icecreamqaq.yuq.entity.Contact;
import me.kuku.yuq.controller.ark.ArkService;

import javax.inject.Inject;

public class GenShinController {
    @Inject
    GenShinService genShinService;

    @Action("{pool}单抽")
    @QMsg(at = true)
    public String cardOne(Contact qq, String pool) {
        //根据QQ号查询池子

        //根据得到的池子信息抽卡
        //返回信息

        return null;
    }

    @Action("{pool}十连")
    @QMsg(at = true)
    public String cardTen(Contact qq, String pool) {
        return null;
    }
}
