package home_decor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

    @GetMapping("/api/services")
    public String getServices() {
        return "Welcome to Sabarish Home Decor! Services API is working.";
    }
}