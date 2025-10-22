package com.capcredit.ms_notification;


import com.capcredit.ms_notification.application.service.SMSService;
import com.capcredit.ms_notification.core.domain.SMS;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SMSController {

    private final SMSService service;

    public SMSController(SMSService service) {
        this.service = service;
    }


    @PostMapping("/send-sms")
    public void sendSMS(@RequestBody SMS sms) {
        service.send(sms);
    }
}