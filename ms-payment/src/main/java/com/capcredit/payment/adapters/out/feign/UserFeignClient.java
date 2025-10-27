package com.capcredit.payment.adapters.out.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.capcredit.payment.config.FeignAuthConfig;
import com.capcredit.payment.core.domain.model.User;

@FeignClient(name = "user-client", url = "${ms.user.url}", configuration = FeignAuthConfig.class)
public interface UserFeignClient {

    @GetMapping("/{id}")
    User findById(@PathVariable("id") UUID id);

}
