@Configuration
@Slf4j
public class EcsConfig {

    @Bean
    public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
        EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            log.info("ECS Task Container Private Ip address is {}", ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        config.setIpAddress(ip);
        config.setPreferIpAddress(true);

        return config;
    }
}