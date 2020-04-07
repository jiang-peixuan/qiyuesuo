package com.qiyuesuo.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

/**
 * 查询最近10个文件的元数据
 */
@Controller
@RunWith(SpringRunner.class)
public class ClientFileFindById {
    String url = "http://localhost:9090/server/serverFileFindById";
    @RequestMapping("/clientFileFind")
    @Test
    public void clientFileFind() {
//        String url = "http://localhost:9090/server/serverDownLoad?filename=acb2d0ed-3dd9-4c52-8af2-a0b6b0731271.txt";
        String uuid ="acb2d0ed-3dd9-4c52-8af2-a0b6b0731271.txt";
       url = url+"?filename="+uuid;
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(url, String.class);
        System.out.println(forObject);
    }
}