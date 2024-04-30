package com.example.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import net.sf.json.JSONObject;

@Repository
public class TripDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getalltrips() {

        String sql = "select * from `trip`";
        List<Map<String, Object>> list_map = jdbcTemplate.queryForList(sql);
        return list_map;

    }

    // the id is user's id
    public List<Map<String, Object>> getalltripsbyid(int id) {

        String sql = "select * from `trip` WHERE id LIKE \'" +
                id +
                "\';";
        List<Map<String, Object>> list_map = jdbcTemplate.queryForList(sql);
        return list_map;

    }

    public Map<String, Object> gettripbytripid(int tripid) {
        String sql = "SELECT * FROM `trip` WHERE tripid LIKE\'" +
                tripid +
                "\';";
        try {
            Map<String, Object> map = jdbcTemplate.queryForMap(sql);
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    public String addonetrip(JSONObject json) {
        String sql = "insert into `trip`(id,starttime,endtime,beginid,endid) values (\'" +
                json.get("id") + "\',\'" +
                json.get("starttime") + "\',\'" +
                json.get("endtime") + "\',\'" +
                json.get("beginid") + "\',\'" +
                json.get("endid") + "\'" +
                ");";
        System.out.println(sql);
        jdbcTemplate.update(sql);
        return "创建成功";
    }
}
