package it.unisalento.pas.smartcitywastemanagement.controllers;

import it.unisalento.pas.smartcitywastemanagement.configuration.IoTprotocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    private IoTprotocol tcpProtocol;

    @RequestMapping("/home")
    public String home() {
        tcpProtocol.initialize();
        return "home";
    }
}
