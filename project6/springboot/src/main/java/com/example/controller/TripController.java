package com.example.controller;

import java.time.LocalDate;
import net.sf.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.service.TripService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/trip")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    // 获取所有订单
    @PostMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllTrips() {
        try {
            List<Map<String, Object>> trips = tripService.getAlltrips();
            return ResponseEntity.ok(trips);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 根据用户ID获取所有订单
    @PostMapping("/user/myTrip")
    public ResponseEntity<List<Map<String, Object>>> getAllTripsByUserId(String idData) {

        List<Map<String, Object>> trips = tripService.getAlltripsById(Integer.parseInt(idData));
        return ResponseEntity.ok(trips);

    }

    @PostMapping("/gpsdata")
    public ResponseEntity<List<Map<String, Object>>> getgpsdata(String tripId) {
        List<Map<String, Object>> gpsdata = tripService.getgpsdata(Integer.parseInt(tripId));
        return ResponseEntity.ok(gpsdata);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addOnetrip(String id, String starttime, String endtime,
            String gpslist) {
        ArrayList<Map<String, Object>> jsonArray = new ArrayList<>();

        String[] objects = gpslist.substring(1, gpslist.length() - 1).split("\\},\\{");

        for (String obj : objects) {
            String[] keyValuePairs = obj.split(",");
            Map<String, Object> jsonObject = new java.util.HashMap<>();

            for (String pair : keyValuePairs) {
                String[] entry = pair.split(":");
                String key = entry[0].replaceAll("[{}\"]", "");
                String value = entry[1].replaceAll("[{}\"]", "");
                jsonObject.put(key, value);
            }

            jsonArray.add(jsonObject);
        }
        List<Integer> ids = tripService.setgpsdata(jsonArray);
        JSONObject json = new JSONObject();
        json = new JSONObject();
        json.put("id", Integer.parseInt(id));
        json.put("starttime", starttime);
        json.put("endtime", endtime);
        json.put("beginid", ids.get(0));
        json.put("endid", ids.get(ids.size() - 1));
        String result = tripService.addOnetrip(json);
        return ResponseEntity.ok(result);
    }
}