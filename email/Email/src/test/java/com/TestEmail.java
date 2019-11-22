package com;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import tcwl.MailDemoApplication;
import tcwl.entites.EmailInfo;
import tcwl.service.MailService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = MailDemoApplication.class)
@RunWith(SpringRunner.class)
@Rollback(false)
@Transactional
public class TestEmail {
    @Autowired
    MailService mailService;

    @Test
    public void m1() {
        EmailInfo emailInfo = new EmailInfo();
        emailInfo.setEid(UUID.randomUUID().toString().replace("-", ""))
                .setAccepter("juzhang")
                .setSender("zx")
                .setTitle("测试邮箱")
                .setSendTime(new Date())
                .setPhoneNum("15146895421")
                .setCompany("北京天创")
                .setDocument("16.png")
                .setContent("局长你好,我想请假");
        mailService.addEmailInfo(emailInfo);
//        List<EmailInfo> emailInfos = mailService.showEmailInfo();

    }
}
