package com.matthewa.openapidemo;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class OpenApiConfig {
    @Value("${spring.application.name:Application}")
    private String appName;

    @Value("${info.app.version}")
    private String version;

    @Bean
    @ConditionalOnClass(value = Tag.class)
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            Optional<PreAuthorize> preAuthorizeAnnotation = Optional.ofNullable(handlerMethod.getMethodAnnotation(PreAuthorize.class));
            StringBuilder sb = new StringBuilder();

            if (preAuthorizeAnnotation.isPresent()) {
                sb.append("This api requires **")
                    .append((preAuthorizeAnnotation.get()).value().replaceAll("hasAuthority|\\(|\\)|\\'", ""))
                    .append("** permission.");
            } else {
                sb.append("This api is **public**");
            }
            sb.append("<br/><br/>");

            if (StringUtils.isBlank(operation.getSummary())) {
                String handlerMethodTitle = StringUtils.capitalize(handlerMethod.getMethod().getName());
                String[] methodTitleArray = StringUtils.splitByCharacterTypeCamelCase(handlerMethodTitle);
                String summary = Arrays.stream(methodTitleArray).collect(Collectors.joining(" "));
                operation.setSummary(summary);
            }
            operation.setDescription(sb.toString());

            String controllerTitle = handlerMethod.getBeanType().getSimpleName();
            String[] controllerTitleArray = StringUtils.splitByCharacterTypeCamelCase(controllerTitle);
            List<String> tags = List.of(Arrays.stream(controllerTitleArray).collect(Collectors.joining(" ")));
            operation.setTags(tags);

            return operation;
        };
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return (openApi) -> {
            Info info = new Info();
            info.setTitle(StringUtils.capitalize(appName) + " API");
            info.setVersion(version.replace("'", ""));
            openApi.setInfo(info);
        };
    }
}
