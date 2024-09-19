package com.pobluesky.global.config;


import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class EcsConfig {

    @Value("${server.port}")
    private int serverPort;  // server.port를 가져오기 위한 필드 추가

    @Bean
    public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
        EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            log.info("ECS Task Container Private IP address is {}", ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        config.setIpAddress(ip);
        config.setPreferIpAddress(true);
        config.setNonSecurePort(serverPort);  // 설정한 server.port를 사용하여 nonSecurePort 설정

        return config;
    }
}
