package unirio.pm.external_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class DotenvConfig {
    
    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
            .directory(System.getProperty("user.dir") + "/external-service")
            .filename(".env")
            .load();
    }
}
