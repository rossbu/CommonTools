package com.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/*
  this demo is to demo
    1. the difference between @Value and @ConfigurationProperties
        https://docs.spring.io/spring-boot/docs/2.4.x/reference/html/spring-boot-features.html#boot-features-external-config-vs-value
    2. relaxed binding
        https://docs.spring.io/spring-boot/docs/2.4.x/reference/html/spring-boot-features.html#boot-features-external-config-relaxed-binding
    3. passing spring profile
        - jvm:        -Dspring.profiles.active=dev
        - Unix shell: export spring_profiles_active=dev
        - global.env.SPRING_PROFILES_ACTIVE=$BUILD_ENV
           rules:
            Replace dots (.) with underscores (_).
            Remove any dashes (-).
            Convert to uppercase.
    4. YAML uses three dashes (“---”)
        - The three dashes --- are used to signal the start of a document
        - To separate directives from document content.
        - To signal the document start when you have multiple yaml documents in the same stream, e.g., a yaml file:
            doc 1
            ---
            doc 2
        - To separate different profiles ( dev or prod ):
            The three dashes separating the two profiles indicate the start of a new document,
            so all the profiles can be described in the same YAML file

            spring:
                config:
                    activate:
                        on-profile: test
            name: test-YAML
            enabled: false
            ---
            spring:
                config:
                    activate:
                        on-profile: prod
            name: prod-YAML
            enabled: true

            Note: in runtime, set spring.profiles.active=prod in src/main/resources/application.properties
*/
@SpringBootApplication
public class PropertyApplication {
  public static void main(String[] args) {
    SpringApplication.run(new Class[] {PropertyApplication.class}, args);
  }

  @Component
  @Getter
  @ToString
  static class ValueConfig {
    @Value("${account.secret}") // which means order.start-duration
    String secret;

    @Value("${account.alias:one,two,three}")
    private String[] accountAlias;
  }

  @Getter
  @Setter
  @Configuration
  @ConfigurationProperties("pricing")
  static class PropertyConfig {
    boolean enabled;
    String firstBooking;
    String secondBooking;
    Integer[] levels;
    Map accountType;
    List<String> roles;
  }



  @Service
  @Getter
  @AllArgsConstructor
  static class DataService {
    ValueConfig valueConfig;
    PropertyConfig propertyConfig;
  }

  @Component
  static class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired DataService dataService;

    Environment environment;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("+++++++++++++++++++++  Demo for Properties binding in application.yaml +++++++++++++++++++++");
        System.out.println("with @value , secret is " + dataService.getValueConfig().getSecret());
        Arrays.stream(dataService.getValueConfig().getAccountAlias()).forEach(System.out::println);

      System.out.println(
          "with @ConfigurationProperties enable is " + dataService.getPropertyConfig().isEnabled());
      System.out.println(
          "with @ConfigurationProperties first-booking  --> relaxed binding --> firstBooking is "
              + dataService.getPropertyConfig().getFirstBooking());
      System.out.println(
          "with @ConfigurationProperties second_booking --> relaxed binding --> secondBooking  is "
              + dataService.getPropertyConfig().getSecondBooking());
      Arrays.stream(dataService.getPropertyConfig().getLevels()).forEach(System.out::println);

      dataService
          .getPropertyConfig()
          .getAccountType()
          .forEach(
              (k, v) -> {
                System.out.println(k + " and " + v);
              });

      dataService.getPropertyConfig().getRoles().stream().forEach(System.out::println);

    }
  }

  class AppListener implements GenericApplicationListener {

    public static final String APPLICATION_CONFIGURATION_PROPERTY_SOURCE_NAME =
        "applicationConfigurationProperties";

    @Override
    public boolean supportsEventType(ResolvableType eventType) {
      return ApplicationPreparedEvent.class.getTypeName().equals(eventType.getType().getTypeName());
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
      return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
      if (event instanceof ApplicationPreparedEvent) {
        ApplicationPreparedEvent _event = (ApplicationPreparedEvent) event;
        ConfigurableEnvironment env = _event.getApplicationContext().getEnvironment();

        // change priority order application.properties in PropertySources
        PropertySource ps =
            env.getPropertySources().remove(APPLICATION_CONFIGURATION_PROPERTY_SOURCE_NAME);
        env.getPropertySources().addFirst(ps);
        // logging.config is testing. VM parameter -Dlogging.config=xxx will be override by
        // application.properties
        System.out.println(env.getProperty("logging.config"));
      }
    }

    @Override
    public int getOrder() {
      return 0;
    }
  }
}
