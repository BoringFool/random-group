package com.zm.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class CustomLogConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        YamlPropertiesFactoryBean yamlProFb = new YamlPropertiesFactoryBean();
        yamlProFb.setResources(new ClassPathResource("application.yml"));
        Properties properties = yamlProFb.getObject();

        try {
            assert properties != null;
            return (String) properties.get("dubbo.application.name") +
                    "- -" +
                    InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
