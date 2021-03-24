package me.kuku.yuq.controller;

import com.IceCreamQAQ.Yu.annotation.Action;
import com.icecreamqaq.yuq.FunKt;
import com.icecreamqaq.yuq.annotation.GroupController;
import com.icecreamqaq.yuq.message.Message;
import me.kuku.yuq.utils.OkHttpUtils;

import java.io.IOException;

@GroupController
public class GenShinController {

    @Action("原神 {str}")
    public Message genShinMaterial(String str) throws IOException {
        switch (str) {
            case "武器突破材料":
                return FunKt.getMif().imageByByteArray(OkHttpUtils.getBytes("https://uploadstatic.mihoyo.com/ys-obc/2021/03/07/75833613/419429c2ca0c6c1fed40c5dd1157c726_7022282023404270507.png")).toMessage();
            case "旅行者天赋":
                return FunKt.getMif().imageByByteArray(OkHttpUtils.getBytes("https://img2.tapimg.com/bbcode/images/c23f87429b13f1d674bd103ba9f82d50.jpg")).toMessage();
            case "角色技能秘境":
                return FunKt.getMif().imageByByteArray(OkHttpUtils.getBytes("https://uploadstatic.mihoyo.com/ys-obc/2021/03/02/75833613/523ad8dcc59cb3d4202a84640b1415ea_532698898772765398.png")).toMessage();
            case "角色技能周常":
                return FunKt.getMif().imageByByteArray(OkHttpUtils.getBytes("https://uploadstatic.mihoyo.com/ys-obc/2021/03/02/75833613/a1d245cb355dc0bc50532f89ba0d9d92_9194285928067476338.png")).toMessage();
            default:
                String stringBuilder = "请发送:\n" +
                        "原神 武器突破材料\n" +
                        "原神 旅行者天赋\n" +
                        "原神 角色技能秘境\n" +
                        "原神 角色技能周常\n";
                return FunKt.getMif().text(stringBuilder).toMessage();
        }
    }
}
