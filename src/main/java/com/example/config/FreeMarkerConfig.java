//package com.example.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
//
//@Configuration
//public class FreeMarkerConfig {
//
//    /**
//     * Configure FreeMarker to use the classpath templates
//     */
//    @Bean
//    public FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean() {
//        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
//        bean.setTemplateLoaderPath("classpath:/templates/");
//        return bean;
//    }
//}
/**
 * FreeMarker configuration is auto-configured by Spring Boot using application.yml settings.
 * No custom configuration needed as spring.freemarker properties handle the setup.
 */
