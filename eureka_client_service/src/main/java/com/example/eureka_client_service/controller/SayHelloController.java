package com.example.eureka_client_service.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen
 * @version 1.0
 * @date 2020/4/27 19:57
 * @description:
 */
@RestController
public class SayHelloController {
    @RequestMapping(value = "/hello/{name}")
    public String sayHello(@PathVariable("name") String name) {
        return "Hello ,".concat(name).concat("! ");
    }
}
