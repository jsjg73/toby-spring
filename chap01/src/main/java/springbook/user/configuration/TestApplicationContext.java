package springbook.user.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("/t-applicationContext.xml")
public class TestApplicationContext {

}
