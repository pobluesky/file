import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.commons.util.InetUtils;

import java.util.List;
import java.util.Map;

@Configuration
public class EurekaClientConfig {

    @Value("${ECS_CONTAINER_METADATA_URI_V4}")
    private String ecsMetadataUri;

    private final InetUtils inetUtils;

    public EurekaClientConfig(InetUtils inetUtils) {
        this.inetUtils = inetUtils;
    }

    @Bean
    public EurekaInstanceConfigBean eurekaInstanceConfigBean() {
        EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);

        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> taskMetadata = restTemplate.getForObject(ecsMetadataUri + "/task", Map.class);

            List<Map<String, Object>> containers = (List<Map<String, Object>>) taskMetadata.get("Containers");
            Map<String, Object> container = containers.get(0);
            List<Map<String, Object>> networks = (List<Map<String, Object>>) container.get("Networks");
            Map<String, Object> network = networks.get(0);
            List<String> ipAddresses = (List<String>) network.get("IPv4Addresses");
            String ipAddress = ipAddresses.get(0);

            config.setIpAddress(ipAddress);
            config.setPreferIpAddress(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return config;
    }
}
