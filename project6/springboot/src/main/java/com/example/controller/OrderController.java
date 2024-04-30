package com.example.controller;

import java.time.LocalDate;
import net.sf.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.service.OrderService;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 获取所有订单
    @PostMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllOrders() {
        try {
            List<Map<String, Object>> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 根据用户ID获取所有订单
    @PostMapping("/user/myOrder")
    public ResponseEntity<List<Map<String, Object>>> getAllOrdersByUserId(String idData) {

        List<Map<String, Object>> orders = orderService.getAllOrdersById(Integer.parseInt(idData));
        return ResponseEntity.ok(orders);

    }

    // 通过订单名称获取订单
    @PostMapping("/name/{orderName}")
    public ResponseEntity<Map<String, Object>> getOrderByOrderName(int orderid) {
        Map<String, Object> order = orderService.getOrderByOrderid(orderid);
        return ResponseEntity.ok(order);
    }

    // 通过订单名称删除订单
    @PostMapping("/delete")
    public ResponseEntity<String> deletebyorderid(String orderId) {
        System.out.println("12345678");
        System.out.println(orderId);
        String result = orderService.deleteByOrderid(Integer.parseInt(orderId));
        return ResponseEntity.ok(result);
    }

    // 通过订单ID进行支付更新
    @PostMapping("/pay")
    public ResponseEntity<String> payOrderByOrderId(String orderId) {
        System.out.println("1234567");
        System.out.println(orderId);
        String result = orderService.payOrderByOrderId(Integer.parseInt(orderId));
        return ResponseEntity.ok(result);
    }

    // 添加一个新订单
    @PostMapping("/add")
    public ResponseEntity<String> addOneOrder(String time, String price, String userid) {
        JSONObject json = new JSONObject();
        json = new JSONObject();
        json.put("days", time);
        json.put("price", price);
        json.put("id", Integer.parseInt(userid));
        json.put("purchaseDate", LocalDate.now().toString());
        String result = orderService.addOneOrder(json);
        return ResponseEntity.ok(result);
    }
}