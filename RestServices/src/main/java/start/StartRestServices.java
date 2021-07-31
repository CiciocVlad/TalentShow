package start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@ComponentScan("talent")
@SpringBootApplication
public class StartRestServices {
    @Bean(name = "properties")
    @Primary
    public Properties getBdProperties() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("/Users/reisender/projects/java/mpp/ATalentShow/TalentServer/src/main/resources/talentServer.properties"));
        }
        catch (IOException e) {
            System.out.println("configuration file not found " + e);
        }
        return properties;
    }

    public static void main(String[] args) {
        SpringApplication.run(StartRestServices.class, args);
    }
}
