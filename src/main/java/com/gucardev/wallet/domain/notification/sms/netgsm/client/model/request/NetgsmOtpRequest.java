package com.gucardev.wallet.domain.notification.sms.netgsm.client.model.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "mainbody")
public class NetgsmOtpRequest {

    @JacksonXmlProperty(localName = "header")
    private Header header;

    @JacksonXmlProperty(localName = "body")
    private Body body;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Header {
        @JacksonXmlProperty(localName = "usercode")
        private String usercode;

        @JacksonXmlProperty(localName = "password")
        private String password;

        @JacksonXmlProperty(localName = "msgheader")
        private String msgheader;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Body {
        @JacksonXmlProperty(localName = "msg")
        private String msg;

        @JacksonXmlProperty(localName = "no")
        private String no;
    }
}