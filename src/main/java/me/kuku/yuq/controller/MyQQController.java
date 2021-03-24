package me.kuku.yuq.controller;

import com.IceCreamQAQ.Yu.annotation.Action;
import com.IceCreamQAQ.Yu.annotation.Before;
import com.IceCreamQAQ.Yu.annotation.Synonym;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icecreamqaq.yuq.FunKt;
import com.icecreamqaq.yuq.annotation.GroupController;
import com.icecreamqaq.yuq.annotation.PathVar;
import com.icecreamqaq.yuq.annotation.QMsg;
import com.icecreamqaq.yuq.controller.BotActionContext;
import com.icecreamqaq.yuq.controller.ContextSession;
import com.icecreamqaq.yuq.controller.QQController;
import com.icecreamqaq.yuq.message.Message;
import me.kuku.yuq.entity.GroupEntity;
import me.kuku.yuq.entity.QQEntity;
import me.kuku.yuq.logic.MyApiLogic;
import me.kuku.yuq.pojo.TwitterPojo;
import me.kuku.yuq.service.GroupService;
import me.kuku.yuq.service.QQService;
import me.kuku.yuq.utils.BotUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@GroupController
@SuppressWarnings("unused")
public class MyQQController extends QQController {

    @Inject
    private QQService qqService;
    @Inject
    private GroupService groupService;
    @Inject
    private MyApiLogic myApiLogic;

    @Before
    public void before(long qq, long group, BotActionContext actionContext){
        QQEntity qqEntity = qqService.findByQQAndGroup(qq, group);
        if (qqEntity == null) {
            GroupEntity groupEntity = groupService.findByGroup(group);
            if (groupEntity == null) groupEntity = new GroupEntity(group);
            qqEntity = new QQEntity(qq, groupEntity);
        }
        actionContext.set("qqEntity", qqEntity);
    }

    @Action("查询违规")
    @QMsg(at = true)
    public String queryVio(QQEntity qqEntity){
        Integer num = qqEntity.getViolationCount();
        if (num == null) num = 0;
        return "您在本群违规次数为" + num + "次";
    }

    @Action("loc推送 {status}")
    @QMsg(at = true)
    public String locPush(QQEntity qqEntity, boolean status){
        qqEntity.setHostLocPush(status);
        qqService.save(qqEntity);
        if (status) return "hostLoc私聊推送已开启！！";
        else return "hostLoc私聊推送已关闭！！";
    }

    @Action("加推特监控 {content}")
    @QMsg(at = true)
    public String add(QQEntity qqEntity, @PathVar(0) String type, String content, ContextSession session, long qq) throws IOException {
        switch (type){
            case "加推特监控":
                List<TwitterPojo> twIdList = myApiLogic.findTwitterIdByName(content);
                if (twIdList == null) return "没有找到该用户，请重试";
                StringBuilder idMsg = new StringBuilder();
                for (int i = 0; i < twIdList.size(); i++){
                    TwitterPojo twitterPojo = twIdList.get(i);
                    idMsg.append(i + 1).append("、").append(twitterPojo.getUserId()).append("-").append(twitterPojo.getName()).append("-").append(twitterPojo.getScreenName()).append("\n");
                }
                reply(FunKt.getMif().at(qq).plus("请选择您需要监控的用户，输入序号\n").plus(
                        idMsg.deleteCharAt(idMsg.length() - 1).toString()
                ));
                Message twIdMessage = session.waitNextMessage();
                String numStr = Message.Companion.firstString(twIdMessage);
                int num = Integer.parseInt(numStr);
                if (num > twIdList.size()) return "你所选择的序号超过限制了！！";
                TwitterPojo twitterPojo = twIdList.get(num - 1);
                JSONObject twJsonObject = new JSONObject();
                twJsonObject.put("id", twitterPojo.getUserId());
                twJsonObject.put("name", twitterPojo.getName());
                twJsonObject.put("screenName", twitterPojo.getScreenName());
                qqEntity.setTwitterJsonArray(qqEntity.getTwitterJsonArray().fluentAdd(twJsonObject));
                break;
            default: return null;
        }
        qqService.save(qqEntity);
        return type + "成功！！";
    }

    @Action("删推特监控 {content}")
    @Synonym({"删ins监控 {content}"})
    public String del(QQEntity qqEntity, @PathVar(0) String type, String content){
        switch (type){
            case "删推特监控":
                JSONArray twitterJsonArray = qqEntity.getTwitterJsonArray();
                BotUtils.delMonitorList(twitterJsonArray, content);
                qqEntity.setTwitterJsonArray(twitterJsonArray);
                break;
            default: return null;
        }
        qqService.save(qqEntity);
        return type + "成功！！";
    }

    @Action("查推特监控")
    @QMsg(at = true)
    public String query(QQEntity qqEntity, @PathVar(0) String type){
        StringBuilder sb = new StringBuilder();
        switch (type){
            case "查推特监控":
                sb.append("您的推特监控列表如下：").append("\n");
                qqEntity.getTwitterJsonArray().forEach(obj -> {
                    JSONObject jsonObject = (JSONObject) obj;
                    sb.append(jsonObject.getString("id")).append("-")
                            .append(jsonObject.getString("name")).append("-")
                            .append(jsonObject.getString("screenName")).append("\n");
                });
                break;
            default: return null;
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
