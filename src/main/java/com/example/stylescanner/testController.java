package com.example.stylescanner;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
        @RequestMapping("/")
        public String sample() {
            return "Home";
        }

}
