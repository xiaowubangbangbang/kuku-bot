package me.kuku.yuq.controller;

import com.IceCreamQAQ.Yu.annotation.*;
import com.alibaba.fastjson.JSONArray;
import com.icecreamqaq.yuq.FunKt;
import com.icecreamqaq.yuq.annotation.GroupController;
import com.icecreamqaq.yuq.annotation.PathVar;
import com.icecreamqaq.yuq.annotation.QMsg;
import com.icecreamqaq.yuq.controller.BotActionContext;
import com.icecreamqaq.yuq.controller.ContextSession;
import com.icecreamqaq.yuq.controller.QQController;
import com.icecreamqaq.yuq.entity.Member;
import com.icecreamqaq.yuq.message.Image;
import com.icecreamqaq.yuq.message.Message;
import com.icecreamqaq.yuq.message.MessageItem;
import me.kuku.yuq.entity.GroupEntity;
import me.kuku.yuq.entity.QQLoginEntity;
import me.kuku.yuq.logic.BotLogic;
import me.kuku.yuq.logic.QQGroupLogic;
import me.kuku.yuq.logic.QQLoginLogic;
import me.kuku.yuq.logic.ToolLogic;
import me.kuku.yuq.pojo.GroupMember;
import me.kuku.yuq.pojo.Result;
import me.kuku.yuq.service.GroupService;
import net.mamoe.mirai.contact.PermissionDeniedException;

import javax.inject.Inject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@GroupController
public class BotController extends QQController {
    @Config("YuQ.Mirai.user.qq")
    private String qq;
    @Inject
    private QQLoginLogic qqLoginLogic;
    @Inject
    private ToolLogic toolLogic;
    @Inject
    private QQGroupLogic qqGroupLogic;
    @Inject
    private GroupService groupService;
    @Inject
    private BotLogic botLogic;

    @Before
    public void before(BotActionContext actionContext){
        QQLoginEntity qqLoginEntity = botLogic.getQQLoginEntity();
        actionContext.set("qqLoginEntity", qqLoginEntity);
    }

    @Action("qq上传")
    public String groupUpload(QQLoginEntity qqLoginEntity, Long qq, ContextSession session) throws IOException {
        reply(FunKt.getMif().at(qq).plus("请发送您需要上传的图片！！"));
        Message message = session.waitNextMessage();
        ArrayList<MessageItem> body = message.getBody();
        StringBuilder sb = new StringBuilder().append("您上传的图片链接如下：").append("\n");
        int i = 1;
        for (MessageItem item: body){
            if (item instanceof Image){
                Image image = (Image) item;
                Result<Map<String, String>> result = qqLoginLogic.groupUploadImage(qqLoginEntity, image.getUrl());
                String url;
                Map<String, String> map = result.getData();
                if (map == null) url = result.getMessage();
                else url = map.get("picUrl");
                sb.append(i++).append("、").append(url).append("\n");
            }
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    @Action("查业务 {qqNo}")
    @QMsg(at = true, atNewLine = true)
    public String queryVip(Long qqNo, QQLoginEntity qqLoginEntity) throws IOException {
        return qqLoginLogic.queryFriendVip(qqLoginEntity, qqNo, null);
    }

    @Action("列出{day}天未发言")
    @QMsg(at = true, atNewLine = true)
    public String notSpeak(Long group, String day, QQLoginEntity qqLoginEntity) throws IOException {
        Result<List<GroupMember>> result = qqLoginLogic.groupMemberInfo(qqLoginEntity, group);
        if (result.getCode() == 200){
            List<GroupMember> list = result.getData();
//            List<Long> qqList = new ArrayList<>();
            StringBuilder sb = new StringBuilder().append("本群").append(day).append("天未发言的成员如下：").append("\n");
            for (GroupMember groupMember : list) {
                if ((new Date().getTime() - groupMember.getLastTime()) / (1000 * 60 * 60 * 24) > Integer.parseInt(day)){
                    sb.append(groupMember.getQq()).append("\n");
//                    qqList.add(groupMember.getQq());
                }
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        }else return result.getMessage();
    }

    @Action("列出从未发言")
    public String neverSpeak(Long group, QQLoginEntity qqLoginEntity) throws IOException {
        Result<List<GroupMember>> result = qqLoginLogic.groupMemberInfo(qqLoginEntity, group);
        if (result.getCode() == 200){
            List<GroupMember> list = result.getData();
//            List<Long> qqList = new ArrayList<>();
            StringBuilder sb = new StringBuilder().append("本群从未发言的成员如下：").append("\n");
            for (GroupMember groupMember : list) {
                if ((groupMember.getLastTime().equals(groupMember.getJoinTime()) || groupMember.getIntegral() <= 1)
                && new Date().getTime() - groupMember.getJoinTime() > 1000 * 60 * 60 * 24){
                    sb.append(groupMember.getQq()).append("\n");
//                    qqList.add(groupMember.getQq());
                }
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        }else return result.getMessage();
    }

    @Action("查询 {qqNo}")
    @QMsg(at = true, atNewLine = true)
    public String query(Long group, Long qqNo){
        Result<GroupMember> result = qqGroupLogic.queryMemberInfo(group, qqNo);
        GroupMember groupMember = result.getData();
        if (groupMember == null) return result.getMessage();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("群名片：").append(groupMember.getGroupCard()).append("\n");
        sb.append("Q龄：").append(groupMember.getAge()).append("\n");
        sb.append("入群时间：").append(sdf.format(new Date(groupMember.getJoinTime()))).append("\n");
        sb.append("最后发言时间：").append(sdf.format(new Date(groupMember.getLastTime())));
        return sb.toString();
    }

    @QMsg(at = true)
    @Action("群链接")
    public String groupLink(long group, QQLoginEntity qqLoginEntity) throws IOException {
        return qqLoginLogic.getGroupLink(qqLoginEntity, group);
    }

    @Action("天气 {local}")
    public Message weather(String local, QQLoginEntity qqLoginEntity, Long qq) throws IOException {
        Result<String> result = toolLogic.weather(local, qqLoginEntity.getCookie());
        if (result.getCode() == 200){
            return FunKt.getMif().xmlEx(146, result.getData()).toMessage();
        }else return FunKt.getMif().at(qq).plus(result.getMessage());
    }

    @Action("龙王")
    public Message dragonKing(Long group, Member qq, @PathVar(value = 1, type = PathVar.Type.Integer) Integer num){
        GroupEntity groupEntity = groupService.findByGroup(group);
        List<Map<String, String>> list = qqGroupLogic.groupHonor(group, "talkAtIve");
        if (list.size() == 0) return FunKt.getMif().at(qq.getId()).plus("昨天没有龙王！！");
        if (num == null) num = 1;
        if (num > list.size()){
            return FunKt.getMif().at(qq.getId()).plus("历史龙王只有" + list.size() + "位哦，超过范围了！！");
        }
        Map<String, String> map = list.get(num - 1);
        long resultQQ = Long.parseLong(map.get("qq"));
        if (Long.parseLong(this.qq) == resultQQ){
            String[] arr = {"呼风唤雨", "84消毒", "巨星排面"};
            return Message.Companion.toMessage(arr[(int) (Math.random() * arr.length)]);
        }
        if (groupEntity != null) {
            JSONArray whiteJsonArray = groupEntity.getWhiteJsonArray();
            if (whiteJsonArray.contains(String.valueOf(resultQQ))){
                try {
                    qq.ban(60 * 5);
                    return FunKt.getMif().at(qq.getId()).plus("迫害白名单用户，您已被禁言！！");
                }catch (PermissionDeniedException e){
                    return FunKt.getMif().at(qq.getId()).plus("禁止迫害白名单用户，禁言迫害者失败，权限不足！！");
                }
            }
        }
        String[] urlArr = {
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3b8h.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3dnz.gif",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3mwy.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3WJw.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3g3Y.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3iNJ.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/31I9.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/38ot.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3LPD.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3MF5.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3QLr.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3Zz2.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3cwP.png",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3rWQ.png",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3u3F.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3VNx.png",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3t68.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/33oC.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3GPq.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3XHg.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3fLU.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3qzG.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/3n7d.jpg",
                "https://backblaze.kuku.me/file/kukume/2020/11/24/30WN.jpg"
        };
        String url = urlArr[(int) (Math.random() * urlArr.length)];
        return FunKt.getMif().at(resultQQ).plus(FunKt.getMif().imageByUrl(url)).plus("龙王，已蝉联" + map.get("desc") + "，快喷水！！");
    }

    @Action("群聊炽焰")
    @Synonym({"群聊之火", "冒尖小春笋", "快乐源泉"})
    public Message legend(Long group, Long qq, @PathVar(0) String str, @PathVar(value = 1, type = PathVar.Type.Integer) Integer num){
        String msg;
        List<Map<String, String>> list;
        switch (str){
            case "群聊炽焰":
                list = qqGroupLogic.groupHonor(group, "legend");
                msg = "快续火！！";
                break;
            case "群聊之火":
                list = qqGroupLogic.groupHonor(group, "actor");
                msg = "快续火！！";
                break;
            case "冒尖小春笋":
                list = qqGroupLogic.groupHonor(group, "strongNewBie");
                msg = "快......我也不知道快啥了！！";
                break;
            case "快乐源泉":
                list = qqGroupLogic.groupHonor(group, "emotion");
                msg = "快发表情包！！";
                break;
            default: return FunKt.getMif().at(qq).plus("类型不匹配，查询失败！！");
        }
        if (list.size() == 0) return FunKt.getMif().at(qq).plus("该群还没有" + str + "用户！！");
        if (num == null) num = 1;
        if (num > list.size()) return FunKt.getMif().at(qq).plus(str + "只有" + list.size() + "位哦，超过范围了！！");
        Map<String, String> map = list.get(num - 1);
        return FunKt.getMif().at(Long.parseLong(map.get("qq"))).plus(FunKt.getMif().imageByUrl(map.get("image"))).plus(str + "，" + map.get("desc") + "，" + msg);
    }

    @Action("群精华")
    public String essenceMessage(Long group){
        Result<List<String>> result = qqGroupLogic.essenceMessage(group);
        List<String> list = result.getData();
        if (list == null) return result.getMessage();
        return list.get((int) (Math.random() * list.size()));
    }
}
