package me.kuku.yuq.controller;

import com.IceCreamQAQ.Yu.annotation.Action;
import com.IceCreamQAQ.Yu.annotation.Before;
import com.IceCreamQAQ.Yu.annotation.Config;
import com.IceCreamQAQ.Yu.annotation.Synonym;
import com.IceCreamQAQ.Yu.util.OkHttpWebImpl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icecreamqaq.yuq.FunKt;
import com.icecreamqaq.yuq.annotation.PathVar;
import com.icecreamqaq.yuq.annotation.PrivateController;
import com.icecreamqaq.yuq.controller.BotActionContext;
import com.icecreamqaq.yuq.controller.ContextSession;
import com.icecreamqaq.yuq.controller.QQController;
import com.icecreamqaq.yuq.entity.Contact;
import com.icecreamqaq.yuq.entity.Group;
import com.icecreamqaq.yuq.message.Message;
import com.icecreamqaq.yuq.mirai.MiraiBot;
import me.kuku.yuq.entity.ConfigEntity;
import me.kuku.yuq.entity.GroupEntity;
import me.kuku.yuq.entity.QQLoginEntity;
import me.kuku.yuq.logic.*;
import me.kuku.yuq.pojo.ConfigType;
import me.kuku.yuq.pojo.DCloudPojo;
import me.kuku.yuq.pojo.Result;
import me.kuku.yuq.pojo.TeambitionPojo;
import me.kuku.yuq.service.ConfigService;
import me.kuku.yuq.service.GroupService;
import me.kuku.yuq.utils.BotUtils;
import me.kuku.yuq.utils.OkHttpUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PrivateController
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class SettingController extends QQController {
    @Inject
    private GroupService groupService;
    @Config("YuQ.Mirai.bot.master")
    private String master;
    @Inject
    private QQLoginLogic qqLoginLogic;
    @Inject
    private OkHttpWebImpl web;
    @Inject
    private MiraiBot miraiBot;
    @Inject
    private ConfigService configService;
    @Inject
    private TeambitionLogic teambitionLogic;
    @Inject
    private DCloudLogic dCloudLogic;
    @Inject
    private CodeLogic codeLogic;
    @Inject
    @Named("fateAdm")
    private CodeLogic fateAdmCodeLogic;

    @Before
    public void before(long qq, BotActionContext actionContext){
        if (qq != Long.parseLong(master))
            throw Message.Companion.toMessage("您不是机器人主人，无法执行！！").toThrowable();
        actionContext.set("qqLoginEntity", BotUtils.toQQLoginEntity(web, miraiBot));
    }

    @Action("群开启 {groupNo}")
    @Synonym({"群关闭 {groupNo}"})
    public String groupOpen(long groupNo, @PathVar(0) String str){
        Map<Long, Group> groups = FunKt.getYuq().getGroups();
        if (groups.containsKey(groupNo)){
            GroupEntity groupEntity = groupService.findByGroup(groupNo);
            if (groupEntity == null) groupEntity = new GroupEntity(groupNo);
            groupEntity.setStatus(str.contains("开启"));
            groupService.save(groupEntity);
            return "机器人开启或者关闭成功！！";
        }else return "机器人并没有加入这个群！！";
    }

    @Action("同意入群 {groupNo}")
    public String agreeAddGroup(QQLoginEntity qqLoginEntity, long groupNo) throws IOException {
        List<Map<String, String>> groupMsgList = qqLoginLogic.getGroupMsgList(qqLoginEntity);
        Map<String, String> resultMap = null;
        for (Map<String, String> map: groupMsgList){
            if (String.valueOf(groupNo).equals(map.get("group"))){
                resultMap = map;
                break;
            }
        }
        if (resultMap == null) return "没有找到这个群号";
        return qqLoginLogic.operatingGroupMsg(qqLoginEntity, "agree", resultMap, null);
    }

    @Action("退群 {groupNo}")
    public String leaveGroup(long groupNo){
        Map<Long, Group> groups = FunKt.getYuq().getGroups();
        if (groups.containsKey(groupNo)){
            groups.get(groupNo).leave();
            return "退出群聊成功！！";
        }else return "机器人并没有加入这个群！！";
    }

    @Action("qqai")
    public String settingQQAI(ContextSession session, Contact qq){
        qq.sendMessage(Message.Companion.toMessage("将设置QQAI的信息，请确保您的应用赋予了图片鉴黄、智能闲聊、通用OCR能力"));
        qq.sendMessage(Message.Companion.toMessage("请输入ai.qq.com/v1的appId"));
        Message firstMessage = session.waitNextMessage();
        String appId = Message.Companion.firstString(firstMessage);
        qq.sendMessage(Message.Companion.toMessage("请输入ai.qq.com/v1的appKey"));
        Message secondMessage = session.waitNextMessage();
        String appKey = Message.Companion.firstString(secondMessage);
        ConfigEntity configEntity1 = configService.findByType("qqAIAppId");
        if (configEntity1 == null) configEntity1 = new ConfigEntity("qqAIAppId");
        ConfigEntity configEntity2 = configService.findByType("qqAIAppId");
        if (configEntity2 == null) configEntity2 = new ConfigEntity("qqAIAppKey");
        configEntity1.setContent(appId);
        configEntity2.setContent(appKey);
        configService.save(configEntity1);
        configService.save(configEntity2);
        return "绑定qqAI的信息成功！！";
    }

    @Action("baiduai ocr")
    @Synonym({"baiduai contentCensor", "baiduai speech"})
    public String settingBaiduAI(ContextSession session, Contact qq, @PathVar(1) String type){
        qq.sendMessage(BotUtils.toMessage("请输入appId"));
        Message firstMessage = session.waitNextMessage();
        String appId = BotUtils.firstString(firstMessage);
        qq.sendMessage(BotUtils.toMessage("请输入appKey"));
        Message secondMessage = session.waitNextMessage();
        String appKey = BotUtils.firstString(secondMessage);
        qq.sendMessage(BotUtils.toMessage("请输入secretKey"));
        Message thirdMessage = session.waitNextMessage();
        String secretKey = BotUtils.firstString(thirdMessage);
        String appIdType;
        String appKeyType;
        String secretKeyType;
        switch (type){
            case "ocr":
                appIdType = "baiduAIOcrAppId";
                appKeyType = "baiduAIOcrAppKey";
                secretKeyType = "baiduAIOcrSecretKey";
                break;
            case "contentCensor":
                appIdType = "baiduAIContentCensorAppId";
                appKeyType = "baiduAIContentCensorAppKey";
                secretKeyType = "baiduAIContentCensorSecretKey";
                break;
            case "speech":
                appIdType = "baiduAISpeechAppId";
                appKeyType = "baiduAISpeechAppKey";
                secretKeyType = "baiduAISpeechSecretKey";
                break;
            default: return null;
        }
        ConfigEntity appIdConfigEntity = configService.findByType(appIdType);
        if (appIdConfigEntity == null) appIdConfigEntity = new ConfigEntity(appIdType);
        ConfigEntity appKeyConfigEntity = configService.findByType(appKeyType);
        if (appKeyConfigEntity == null) appKeyConfigEntity = new ConfigEntity(appKeyType);
        ConfigEntity secretKeyConfigEntity = configService.findByType(secretKeyType);
        if (secretKeyConfigEntity == null) secretKeyConfigEntity = new ConfigEntity(secretKeyType);
        appIdConfigEntity.setContent(appId);
        appKeyConfigEntity.setContent(appKey);
        secretKeyConfigEntity.setContent(secretKey);
        configService.save(appIdConfigEntity);
        configService.save(appKeyConfigEntity);
        configService.save(secretKeyConfigEntity);
        return "绑定百度AI的信息成功！！";
    }

    @Action("lolicon {apiKey}")
    public String settingLoLiCon(String apiKey){
        ConfigEntity configEntity = configService.findByType("loLiCon");
        if (configEntity == null) configEntity = new ConfigEntity("loLiCon");
        configEntity.setContent(apiKey);
        configService.save(configEntity);
        return "绑定loLiCon的apiKey成功！！";
    }

    @Action("HaiZhiAi {apiKey}")
    public String settingChatAi(String apiKey){
        ConfigEntity configEntity = configService.findByType("ChatAi");
        if (configEntity == null) configEntity = new ConfigEntity("ChatAi");
        configEntity.setContent(apiKey);
        configService.save(configEntity);
        return "绑定海知智能机器人的apiKey成功！！";
    }

    @Action("加超管 {groupNum} {qqNum}")
    @Synonym({"删超管 {groupNum} {qqNum}"})
    public String addSuperAdmin(long groupNum, Long qqNum, @PathVar(0) String str){
        Map<Long, Group> groups = FunKt.getYuq().getGroups();
        if (groups.containsKey(groupNum)) {
            GroupEntity groupEntity = groupService.findByGroup(groupNum);
            if (groupEntity == null) groupEntity = new GroupEntity(groupNum);
            if (str.startsWith("加"))
                groupEntity.setSuperAdminJsonArray(groupEntity.getSuperAdminJsonArray().fluentAdd(qqNum.toString()));
            else if (str.startsWith("删")){
                JSONArray superAdminJsonArray = groupEntity.getSuperAdminJsonArray();
                BotUtils.delManager(superAdminJsonArray, qqNum.toString());
                groupEntity.setSuperAdminJsonArray(superAdminJsonArray);
            }else return null;
            groupService.save(groupEntity);
            return String.format("添加{%s}群的{%s}为超管成功！！", groupNum, qqNum);
        }else return "机器人并没有加入这个群！！";
    }

    @Action("teambition {phone} {password}")
    public String addTeam(String phone, String password, ContextSession session) throws IOException {
        reply("请输入需要绑定的项目名称");
        Message projectMessage = session.waitNextMessage();
        String project = BotUtils.firstString(projectMessage);
        reply("请输入api中添加的名称（由于teambition的下载链接只能动态获取，所以需要使用api来解析下载链接，名称为api中的唯一标识）");
        Message nameMessage = session.waitNextMessage();
        String name = BotUtils.firstString(nameMessage);
        Result<TeambitionPojo> loginResult = teambitionLogic.login(phone, password);
        if (loginResult.isFailure()){
            return loginResult.getMessage();
        }
        TeambitionPojo teambitionPojo = loginResult.getData();
        Result<TeambitionPojo> projectResult = teambitionLogic.project(teambitionPojo, project);
        if (projectResult.isFailure()) return projectResult.getMessage();
        teambitionPojo = projectResult.getData();
        ConfigEntity configEntity = configService.findByType(ConfigType.Teambition.getType());
        if (configEntity == null) configEntity = new ConfigEntity(ConfigType.Teambition.getType());
        JSONObject jsonObject = configEntity.getContentJsonObject();
        jsonObject.put("phone", phone);
        jsonObject.put("password", password);
        jsonObject.put("cookie", teambitionPojo.getCookie());
        jsonObject.put("auth", teambitionPojo.getStrikerAuth());
        jsonObject.put("name", name);
        jsonObject.put("project", project);
        jsonObject.put("projectId", teambitionPojo.getProjectId());
        jsonObject.put("rootId", teambitionPojo.getRootId());
        Result<TeambitionPojo> panResult = teambitionLogic.getPanInfo(teambitionPojo);
        if (panResult.isFailure()){
            reply("尝试获取teambition网盘信息失败，原因：" + panResult.getMessage());
        }else {
            TeambitionPojo pojo = panResult.getData();
            jsonObject.put("panOrgId", pojo.getPanOrgId());
            jsonObject.put("panSpaceId", pojo.getPanSpaceId());
            jsonObject.put("panRootId", pojo.getPanRootId());
            jsonObject.put("panDriveId", pojo.getPanDriveId());
            jsonObject.put("userId", pojo.getUserId());
        }
        configEntity.setContentJsonObject(jsonObject);
        configService.save(configEntity);
        return "绑定Teambition成功！！";
    }

    @Action("teambitionapi {phone} {password}")
    public String addTeamApi(String phone, String password, ContextSession session) throws IOException {
        reply("请输入需要绑定的项目名称");
        Message projectMessage = session.waitNextMessage();
        String project = BotUtils.firstString(projectMessage);
        reply("请输入需要设置的名称（将会在api链接中显示）");
        Message nameMessage = session.waitNextMessage();
        String name = BotUtils.firstString(nameMessage);
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("phone", phone);
        map.put("password", password);
        map.put("project", project);
        JSONObject jsonObject = OkHttpUtils.postJson("https://api.kuku.me/teambition", map);
        if (jsonObject.getInteger("code") == 200){
            return "绑定成功！！";
        }else return "绑定失败！！" + jsonObject.getString("message");
    }

    @Action("teambitionapidel {name} {password}")
    public String delTeamApi(String name, String password) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("password", password);
        JSONObject jsonObject = OkHttpUtils.deleteJson("https://api.kuku.me/teambition", map);
        return jsonObject.getString("data");
    }

    @Action("dcloud {email} {password}")
    public String addDCloud(String email, String password, ContextSession session) throws IOException {
        reply("请输入服务空间的spaceId！！");
        Message idMessage = session.waitNextMessage();
        String spaceId = BotUtils.firstString(idMessage);
        DCloudPojo dCloudPojo = dCloudLogic.getData();
        Result<String> identifyResult = codeLogic.identify("3", dCloudPojo.getCaptchaImage());
        if (identifyResult.isFailure()) return identifyResult.getMessage();
        Result<DCloudPojo> loginResult = dCloudLogic.login(dCloudPojo, email, password, identifyResult.getData() );
        if (loginResult.isFailure()) return loginResult.getMessage();
        dCloudPojo = loginResult.getData();
        ConfigEntity configEntity = configService.findByType("dCloud");
        if (configEntity == null) configEntity = new ConfigEntity("dCloud");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("password", password);
        jsonObject.put("cookie", dCloudPojo.getCookie());
        jsonObject.put("token", dCloudPojo.getToken());
        jsonObject.put("spaceId", spaceId);
        configEntity.setContentJsonObject(jsonObject);
        configService.save(configEntity);
        return "绑定dCloud成功！！";
    }

    @Action("图鉴 {username} {password}")
    public String bindTt(String username, String password){
        ConfigEntity configEntity = configService.findByType(ConfigType.IdentifyCode.getType());
        if (configEntity == null) configEntity = new ConfigEntity(ConfigType.IdentifyCode.getType());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        configEntity.setContentJsonObject(jsonObject);
        configService.save(configEntity);
        return "绑定图鉴成功！！";
    }

    @Action("saucenao {apiKey}")
    public String sauceNao(String apiKey){
        ConfigEntity configEntity = getEntity(ConfigType.SauceNao);
        configEntity.setContent(apiKey);
        configService.save(configEntity);
        return "绑定sauceNao成功！！";
    }

    @Action("fateadm {pdId} {pdKey}")
    public String bindFateAdm(String pdId, String pdKey){
        ConfigEntity configEntity = getEntity(ConfigType.FateAdmCode);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pdId", pdId);
        jsonObject.put("pdKey", pdKey);
        configEntity.setContentJsonObject(jsonObject);
        configService.save(configEntity);
        return "绑定fateadm打码成功！！";
    }

    @Action("ddocr {apiKey}")
    public String bindDdOcr(String apiKey){
        ConfigEntity configEntity = getEntity(ConfigType.DdOcrCode);
        configEntity.setContent(apiKey);
        configService.save(configEntity);
        return "绑定ddOcr成功！！";
    }

    private ConfigEntity getEntity(ConfigType configType){
        ConfigEntity configEntity = configService.findByType(configType.getType());
        if (configEntity == null) configEntity = new ConfigEntity(configType.getType());
        return configEntity;
    }

}
