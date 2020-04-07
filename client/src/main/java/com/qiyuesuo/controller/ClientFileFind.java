package com.qiyuesuo.controller;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import top.jfunc.json.impl.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询最近10个文件的元数据
 */
@Controller
@RunWith(SpringRunner.class)
public class ClientFileFind {
    String url = "http://localhost:9090/server/serverFileFind";
    @RequestMapping("/clientFileFind")
    public String clientFileFind(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(url, String.class);

        System.out.println(forObject);
        return "showAll";
    }
}