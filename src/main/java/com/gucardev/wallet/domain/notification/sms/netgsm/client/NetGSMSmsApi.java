package com.gucardev.wallet.domain.notification.sms.netgsm.client;

import com.gucardev.wallet.domain.notification.sms.netgsm.config.NetGSMSmsConfig;
import com.gucardev.wallet.domain.notification.sms.netgsm.client.model.request.NetgsmOtpRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "netgsm-client", url = "${app-specific-configs.netgsm.api.url}", configuration = NetGSMSmsConfig.class)
public interface NetGSMSmsApi {

    @PostMapping(value = "/sms/send/otp", consumes = MediaType.APPLICATION_XML_VALUE)
    String sendOtpSms(@RequestBody NetgsmOtpRequest request);
}

