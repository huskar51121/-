package com.example.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import net.sf.json.JSONObject;

@Repository
public class GpsdataDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Map<String, Object> getdatabyid(int id) {
        String sql = "SELECT * FROM gps_data WHERE id LIKE\'" +
                id +
                "\';";
        try {
            Map<String, Object> map = jdbcTemplate.queryForMap(sql);
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    public int setdata(Map<String, Object> map) {
        String sql = "insert into `gps_data`(latitude,longitude) values (\'" +
                map.get("latitude") + "\',\'" +
                map.get("longitude") + "\'" +
                ");";
        jdbcTemplate.update(sql);
        Integer lastInsertId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        if (lastInsertId != null) {
            return lastInsertId;
        } else {
            // 如果自增主键值为空，可以根据实际情况返回一个默认值或者抛出异常
            return -1; // 返回默认值 -1
        }
    }
}
