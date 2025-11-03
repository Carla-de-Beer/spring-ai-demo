package dev.cadebe.springaidemo.springai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "app.config.ai.prompt-templates")
@Getter
@Setter
@Validated
public class TemplateConfig {

    private SystemTemplates systemTemplates;

    @Getter
    @Setter
    @Validated
    public static final class SystemTemplates {

        private Resource estateAgentSearchTemplate;
        private Resource fruitTreeLocationTemplate;
        private Resource fruitTreeInfoTemplate;
        private Resource insuranceCompanyTemplate;
        private Resource serviceRequestTemplate;

    }

}
