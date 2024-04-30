package com.example.controller;

import java.time.LocalDate;
import net.sf.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.service.FriendService;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    // 获取所有订单
    @PostMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllFriends() {
        try {
            List<Map<String, Object>> friends = friendService.getAllFriends();
            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 根据用户ID获取所有订单
    @PostMapping("/user/myFriend")
    public ResponseEntity<List<Map<String, Object>>> getAllFriendsByUserId(String idData) {
        System.out.println("12345678");
        System.out.println(idData);
        List<Map<String, Object>> friends = friendService.getFriendByuserid(Integer.parseInt(idData));
        return ResponseEntity.ok(friends);

    }

    @PostMapping("/user/myfans")
    public ResponseEntity<List<Map<String, Object>>> getAllFriendsByupId(String idData) {

        List<Map<String, Object>> friends = friendService.getFriendByupid(Integer.parseInt(idData));
        return ResponseEntity.ok(friends);

    }

    // delete订单
    @PostMapping("/delete")
    public ResponseEntity<String> deletebyfriendid(String userid, String upid) {
        JSONObject json = new JSONObject();
        json = new JSONObject();
        json.put("userid", Integer.parseInt(userid));
        json.put("upid", Integer.parseInt(upid));
        String result = friendService.deleteByFriendid(json);
        return ResponseEntity.ok(result);
    }

    // 添加一个新订单
    @PostMapping("/add")
    public ResponseEntity<String> addOneFriend(String userid, String upid) {
        JSONObject json = new JSONObject();
        json = new JSONObject();
        json.put("userid", Integer.parseInt(userid));
        json.put("upid", Integer.parseInt(upid));
        String result = friendService.addOneFriend(json);
        return ResponseEntity.ok(result);
    }
}