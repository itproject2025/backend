package com.itprojectbackend.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CloudFrontProperties {
    @Value("${cloudfront.domain}")
    private String domain;

    public String buildFlagUrl(String countryCode) {
        return domain + "/flag/" + countryCode + ".png";
    }
}

