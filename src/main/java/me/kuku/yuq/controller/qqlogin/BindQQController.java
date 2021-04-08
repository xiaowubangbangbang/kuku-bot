package me.kuku.yuq.controller.qqlogin;

import com.IceCreamQAQ.Yu.annotation.Action;
import com.icecreamqaq.yuq.FunKt;
import com.icecreamqaq.yuq.annotation.GroupController;
import com.icecreamqaq.yuq.annotation.PathVar;
import com.icecreamqaq.yuq.annotation.PrivateController;
import com.icecreamqaq.yuq.annotation.QMsg;
import com.icecreamqaq.yuq.controller.ContextSession;
import com.icecreamqaq.yuq.entity.Contact;
import com.icecreamqaq.yuq.entity.Group;
import com.icecreamqaq.yuq.entity.Member;
import com.icecreamqaq.yuq.message.Message;
import me.kuku.yuq.entity.QQLoginEntity;
import me.kuku.yuq.pojo.Result;
import me.kuku.yuq.service.QQLoginService;
import me.kuku.yuq.utils.*;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@GroupController
@PrivateController
@SuppressWarnings("unused")
public class BindQQController {
    @Inject
    private QQLoginService qqLoginService;

    @Action("qqlogin qr")
    @QMsg(at = true)
    public Message bindQQ(Group group, long qq){
        InputStream is = null;
        try {
            Map<String, Object> map = QQQrCodeLoginUtils.getQrCode();
            is = (InputStream) map.get("qrCode");
            ExecutorUtils.execute(() -> {
                String msg;
                try {
                    Result<Map<String, String>> result = QQUtils.qrCodeLoginVerify(map.get("sig").toString());
                    if (result.getCode() == 200) {
                        QQUtils.saveOrUpdate(qqLoginService, result.getData(), qq, null, group.getId());
                        msg = "绑定或更新QQ成功！！";
                    } else msg = result.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                    msg = "";
                }
                group.sendMessage(FunKt.getMif().at(qq).plus(msg));
            });
            group.sendMessage(FunKt.getMif().at(qq).plus("QQ8.4.8版本以上的不支持直接图片或者相册识别，\n解决方法：用tim或QQhd扫码或使用旧版本QQ（https://wwx.lanzoux.com/igkqMhpj5gh）"));
            return FunKt.getMif().imageByInputStream(is).plus("qzone.qq.com的扫码登录");
        } catch (Exception e) {
            e.printStackTrace();
            return BotUtils.toMessage("出现异常了，请重试。");
        } finally {
            IOUtils.close(is);
        }
    }

    @Action("qqlogin pwd")
    public String bindQQByPwd(@PathVar(2) String password, Contact qq, ContextSession session) throws IOException {
        QQLoginEntity qqLoginEntity = qqLoginService.findByQQ(qq.getId());
        Long group = null;
        if (qq instanceof Member) group = ((Member) qq).getGroup().getId();
        String pwd = null;
        if (password != null){
            pwd = password;
        }else if (qqLoginEntity != null) pwd = qqLoginEntity.getPassword();
        if (pwd == null) return "在您的指令中没有发现密码！！";
        Result<Map<String, String>> result = QQPasswordLoginUtils.login(qq.getId(), pwd);
        switch (result.getCode()){
            case 200:
                Map<String, String> map = result.getData();
                QQUtils.saveOrUpdate(qqLoginService, map, qq.getId(), pwd, group);
                return "绑定或者更新QQ成功！！";
            case 10009:
                qq.sendMessage(Message.Companion.toMessage(result.getMessage()));
                Map<String, String> smsMap = result.getData();
                Message codeMessage = session.waitNextMessage(1000 * 60 * 2);
                String code = Message.Companion.firstString(codeMessage);
                smsMap.put("smsCode", code);
                Result<Map<String, String>> loginResult = QQPasswordLoginUtils.loginBySms(qq.getId(), password, smsMap);
                if (loginResult.getCode() != 200) return "验证码输入错误，请重新登录！！";
                QQUtils.saveOrUpdate(qqLoginService, loginResult.getData(), qq.getId(), pwd, group);
                return "绑定或者更新QQ成功！！";
            default: return result.getMessage();

        }
    }
}
