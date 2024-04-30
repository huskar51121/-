package com.example.service;

import com.example.dao.OrderDao;
import com.example.dao.UserDao;
import com.example.entity.Order;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private UserDao userdao;
    @Autowired
    private OrderDao orderDao;

    // 获取所有订单
    public List<Map<String, Object>> getAllOrders() {
        return orderDao.getallorders();
    }

    // 通过用户ID获取所有订单
    public List<Map<String, Object>> getAllOrdersById(int id) {
        return orderDao.getallordersbyid(id);
    }

    // 通过订单名称获取订单
    public Map<String, Object> getOrderByOrderid(int orderid) {
        return orderDao.getorderbyorderid(orderid);
    }

    // 通过订单名称删除订单
    public String deleteByOrderid(int orderid) {
        return orderDao.deletebyorderid(orderid);
    }

    // 通过订单ID进行支付更新
    public String payOrderByOrderId(int orderid) {
        // 修改用户的截止日期
        Map<String, Object> theorder = orderDao.getorderbyorderid(orderid);
        int userid = (int) theorder.get("id");
        Map<String, Object> theuser = userdao.getuserbyid(userid);
        LocalDate lastdate = LocalDate.parse(theuser.get("enddate").toString());
        LocalDate datenow = LocalDate.now();
        LocalDate newdate;
        if (lastdate.compareTo(datenow) < 0) {
            newdate = datenow;
        } else {
            newdate = lastdate;
        }
        String days = (String) theorder.get("days");
        LocalDate finaldate;
        System.out.println("345678");
        System.out.println(days);
        if (days.equals("oneweek")) {
            finaldate = newdate.plus(Period.ofDays(7));
        } else if (days.equals("onemonth")) {
            finaldate = newdate.plus(Period.ofDays(30));
        } else if (days.equals("threemonth")) {
            finaldate = newdate.plus(Period.ofDays(90));
        } else {
            finaldate = newdate.plus(Period.ofDays(365));
        }

        userdao.changeenddatebyid(userid, finaldate.toString());
        return orderDao.paybyorderid(orderid);
    }

    // 添加一个新订单
    public String addOneOrder(JSONObject json) {
        if (json == null) {
            return "订单信息为空";
        }
        return orderDao.addoneorder(json);
    }
}