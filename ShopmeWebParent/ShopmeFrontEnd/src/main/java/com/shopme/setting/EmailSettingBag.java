package com.shopme.setting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Builder
@Component
public class EmailSettingBag {
    private SettingService settingService;

    public String getMailFrom() {
        return settingService.findValueByKey("MAIL_FROM");
    }

    public String getHost() {
        return settingService.findValueByKey("MAIL_HOST");
    }

    public String getPassword() {
        return settingService.findValueByKey("MAIL_PASSWORD");
    }

    public String getPort() {
        return settingService.findValueByKey("MAIL_PORT");
    }

    public String getSenderName() {
        return settingService.findValueByKey("MAIL_SENDER");
    }

    public String getUserName() {
        return settingService.findValueByKey("MAIL_USERNAME");
    }

    public String getSmtpAuth() {
        return settingService.findValueByKey("SMTP_AUTH");
    }

    public String getSmtpSecured() {
        return settingService.findValueByKey("SMTP_SECURED");
    }

    public String getSubject() {
        return settingService.findValueByKey("CUSTOMER_VERIFY_SUBJECT");
    }

    public String getContent() {
        return settingService.findValueByKey("CUSTOMER_VERIFY_CONTENT");
    }
}
