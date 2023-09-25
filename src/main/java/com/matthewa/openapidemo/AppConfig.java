package com.matthewa.openapidemo;

import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class AppConfig {
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
            sb.append("<br /><br />");

            if(StringUtils.isBlank(operation.getSummary())) {
                var xyz = StringUtils.splitByCharacterTypeCamelCase(StringUtils.capitalize(handlerMethod.getMethod().getName()));
                String summary = java.util.Arrays.stream(xyz).collect(Collectors.joining(" "));
                operation.setSummary(summary);
            }
            operation.setDescription(sb.toString());
            return operation;
        };
    }
}
