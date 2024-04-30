package com.example.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import net.sf.json.JSONObject;

@Repository
public class FriendDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getallfriends() {

        String sql = "select * from `friend`";
        List<Map<String, Object>> list_map = jdbcTemplate.queryForList(sql);
        return list_map;

    }

    // the id is user's id
    public List<Map<String, Object>> getfriendbyuserid(int userid) {

        String sql = "select * from `friend` WHERE userid LIKE \'" +
                userid +
                "\';";
        List<Map<String, Object>> list_map = jdbcTemplate.queryForList(sql);
        return list_map;
    }

    public List<Map<String, Object>> getfriendbyupid(int upid) {
        String sql = "SELECT * FROM `friend` WHERE upid LIKE\'" +
                upid +
                "\';";
        List<Map<String, Object>> list_map = jdbcTemplate.queryForList(sql);
        return list_map;
    }

    public String addonefriend(JSONObject json) {
        String sql = "insert into `friend`(userid,upid) values (\'" +
                json.get("userid") + "\',\'" +
                json.get("upid") + "\'" +
                ");";
        jdbcTemplate.update(sql);
        return "创建成功";
    }

    public String deletebyjson(JSONObject json) {
        String sql = "DELETE FROM `friend` \nWHERE userid = \'" +
                json.get("userid") + "\' AND upid = \'" +
                json.get("upid") +
                "\';";
        try {
            jdbcTemplate.update(sql);
            return "删除成功";
        } catch (Exception e) {
            return "删除失败";
        }
    }
}
