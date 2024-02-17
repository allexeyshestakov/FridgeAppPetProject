package com.example.fridgeapppetproject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/lizka")
    public String sayHello(){
            return "Ифка биз буз";

    }

}
