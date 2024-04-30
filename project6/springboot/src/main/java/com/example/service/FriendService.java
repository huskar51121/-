package com.example.service;

import com.example.dao.FriendDao;
import com.example.dao.UserDao;
import com.example.entity.Friend;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

@Service
public class FriendService {

    @Autowired
    private UserDao userdao;
    @Autowired
    private FriendDao friendDao;

    // 获取所有订单
    public List<Map<String, Object>> getAllFriends() {
        return friendDao.getallfriends();
    }

    // 通过订单名称获取订单
    public List<Map<String, Object>> getFriendByuserid(int userid) {
        List<Map<String, Object>> list = friendDao.getfriendbyuserid(userid);
        for (Map<String, Object> map : list) {
            map.remove("userid");
            int upid = (int) map.get("upid");
            String upname = (String) userdao.getuserbyid(upid).get("name");
            map.put("upname", upname);
        }
        return list;
    }

    public List<Map<String, Object>> getFriendByupid(int upid) {
        return friendDao.getfriendbyupid(upid);
    }

    // 通过订单名称删除订单
    public String deleteByFriendid(JSONObject json) {
        try {
            return friendDao.deletebyjson(json);
        } catch (Exception e) {
            return "不存在该好友";
        }
    }

    // 添加一个新订单
    public String addOneFriend(JSONObject json) {
        if (json == null) {
            return "好友信息为空";
        }
        try {
            return friendDao.addonefriend(json);
        } catch (Exception e) {
            return "已经存在该好友";
        }

    }
}