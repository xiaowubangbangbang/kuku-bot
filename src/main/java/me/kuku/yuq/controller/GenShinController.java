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
                return FunKt.getMif().imageByByteArray(OkHttpUtils.getBytes("https://img2.tapimg.com/bbcode/images/5b4d8430e4c3a16369fcf32624d70e75.jpg")).toMessage();
            case "旅行者天赋":
                return FunKt.getMif().imageByByteArray(OkHttpUtils.getBytes("https://img2.tapimg.com/bbcode/images/c23f87429b13f1d674bd103ba9f82d50.jpg")).toMessage();
            case "角色技能秘境":
                return FunKt.getMif().imageByByteArray(OkHttpUtils.getBytes("https://img2.tapimg.com/bbcode/images/47683f0e8a79c76f7a464517034571d9.jpg")).toMessage();
            case "角色技能周常":
                return FunKt.getMif().imageByByteArray(OkHttpUtils.getBytes("https://img2.tapimg.com/bbcode/images/20ae91208977827d64e5969e91f12d6d.jpg")).toMessage();
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
