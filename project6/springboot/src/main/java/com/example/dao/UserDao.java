package com.example.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import net.sf.json.JSONObject;

@Repository
public class UserDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getallusers() {

        String sql = "select * from user";
        List<Map<String, Object>> list_map = jdbcTemplate.queryForList(sql);
        return list_map;

    }

    public Map<String, Object> getuserbyname(String name) {
        String sql = "SELECT * FROM user WHERE name LIKE\'" +
                name +
                "\';";
        try {
            Map<String, Object> map = jdbcTemplate.queryForMap(sql);
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, Object> getuserbyid(int id) {
        String sql = "SELECT * FROM user WHERE id LIKE\'" +
                id +
                "\';";
        try {
            Map<String, Object> map = jdbcTemplate.queryForMap(sql);
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    public String changepasswordbyid(int id, String password) {
        String sql = "UPDATE user \n SET password = \'" +
                password +
                "\' \n WHERE id = \'" +
                id +
                "\';";
        try {
            jdbcTemplate.update(sql);
            return "修改成功";
        } catch (Exception e) {
            return "修改失败";
        }
    }

    public String addgoldbyid(int id) {
        String sql = "UPDATE user \n SET gold = gold + 1" +
                "\n WHERE id = \'" +
                id +
                "\';";
        try {
            jdbcTemplate.update(sql);
            return "修改成功";
        } catch (Exception e) {
            return "修改失败";
        }
    }

    public String changeenddatebyid(int id, String enddate) {
        String sql = "UPDATE user \n SET enddate = \'" +
                enddate +
                "\' \n WHERE id = \'" +
                id +
                "\';";
        try {
            jdbcTemplate.update(sql);
            return "修改成功";
        } catch (Exception e) {
            return "修改失败";
        }
    }

    public String changentabyid(int id, String name, String tagline, String avatarurl) {
        String sql = "UPDATE user \n SET " +
                "name = \'" + name + "\'," +
                "tagline = \'" + tagline + "\'," +
                "avatarurl = \'" + avatarurl +
                "\' \n WHERE id = \'" +
                id +
                "\';";
        jdbcTemplate.update(sql);
        return "修改成功";
    }

    public String addoneusers(JSONObject json) {
        String sql = "insert into user(name,password,phone,enddate,gold) values (\'" +
                json.get("name") + "\',\'" +
                json.get("password") + "\',\'" +
                json.get("phone") + "\',\'" +
                json.get("enddate") + "\',\'" +
                0 + "\'" +
                ");";
        jdbcTemplate.update(sql);
        return "创建成功";
    }

    public String getpasswordbyname(String name) {
        String sql = "SELECT password FROM user WHERE name LIKE\'" +
                name +
                "\';";
        try {
            String password = jdbcTemplate.queryForObject(sql, String.class);
            return password;
        } catch (Exception e) {
            return null;
        }
    }
}
