package com.example.service;

import com.example.dao.GpsdataDao;
import com.example.dao.TripDao;
import com.example.dao.UserDao;
import com.example.entity.Trip;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TripService {

    @Autowired
    private GpsdataDao GpsdataDao;
    @Autowired
    private TripDao tripDao;

    // 获取所有订单
    public List<Map<String, Object>> getAlltrips() {
        return tripDao.getalltrips();
    }

    // 通过用户ID获取所有订单
    public List<Map<String, Object>> getAlltripsById(int id) {
        return tripDao.getalltripsbyid(id);
    }

    // 通过订单名称获取订单
    public Map<String, Object> gettripBytripid(int orderid) {
        return tripDao.gettripbytripid(orderid);
    }

    // 添加一个新订单
    public String addOnetrip(JSONObject json) {
        if (json == null) {
            return "订单信息为空";
        }
        // 这里我们直接调用DAO层的方法，但在实际应用中可能还会进行一些比如订单校验等其它业务操作
        return tripDao.addonetrip(json);
    }

    public ArrayList<Integer> setgpsdata(ArrayList<Map<String, Object>> list) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Map<String, Object> map : list) {
            Integer onedata = GpsdataDao.setdata(map);
            ids.add(onedata);
        }
        return ids;
    }

    public List<Map<String, Object>> getgpsdata(int tripid) {
        Map<String, Object> thetrip = tripDao.gettripbytripid(tripid);
        int beginid = (int) thetrip.get("beginid");
        int endid = (int) thetrip.get("endid");
        List<Map<String, Object>> Gpsdata = new ArrayList<>();
        for (; beginid <= endid; beginid += 1) {
            Map<String, Object> onedata = GpsdataDao.getdatabyid(beginid);
            Gpsdata.add(onedata);
        }
        return Gpsdata;
    }
}