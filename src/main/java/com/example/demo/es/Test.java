package com.example.demo.es;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Slf4j
@RestController
@RequestMapping("/esTest")
public class Test {


    @GetMapping("/test1")
    public String test(String string) throws Exception {
        //elasticsearchRepository.save()
//        TestMainTxt dd = new TestMainTxt();
//        dd.test1(string);

        return "ok";
    }



}
