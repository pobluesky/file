import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;

import java.util.Map;

@Configuration
public class EurekaClientConfig {

    @Value("${ECS_CONTAINER_METADATA_URI_V4}")
    private String ecsMetadataUri;

    @Bean
    public EurekaInstanceConfigBean eurekaInstanceConfigBean() {
        EurekaInstanceConfigBean config = new EurekaInstanceConfigBean();

        try {
            RestTemplate restTemplate = new RestTemplate();
            // Task 메타데이터 가져오기
            Map<String, Object> taskMetadata = restTemplate.getForObject(ecsMetadataUri + "/task", Map.class);

            // 컨테이너의 네트워크 인터페이스에서 IP 주소 가져오기
            Map<String, Object> container = ((List<Map<String, Object>>) taskMetadata.get("Containers")).get(0);
            Map<String, Object> network = ((List<Map<String, Object>>) container.get("Networks")).get(0);
            String ipAddress = ((List<String>) network.get("IPv4Addresses")).get(0);

            // IP 주소를 Eureka 인스턴스 설정에 반영
            config.setIpAddress(ipAddress);
            config.setPreferIpAddress(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return config;
    }
}