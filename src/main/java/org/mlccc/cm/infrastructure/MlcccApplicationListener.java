package org.mlccc.cm.infrastructure;

import org.mlccc.cm.infrastructure.security.MlcccCipher;
import org.mlccc.cm.service.util.BraintreeGatewayFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class MlcccApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event){
        ConfigurableEnvironment environment = event.getEnvironment();
        Properties props = new Properties();
        String dbPassword;
        String mailPassword;
        try {
            MlcccCipher.init(environment.getProperty("application.keyFilePath"),environment.getProperty("application.ivFilePath"));
            dbPassword = MlcccCipher.decrypt(environment.getProperty("spring.datasource.password"));
            mailPassword = MlcccCipher.decrypt(environment.getProperty("spring.mail.password"));
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        props.put("spring.datasource.password", dbPassword);
        props.put("spring.mail.password", mailPassword);
        environment.getPropertySources().addFirst(new PropertiesPropertySource("mlcccProps", props));
    }
}
