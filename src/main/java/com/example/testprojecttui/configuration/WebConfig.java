package com.example.testprojecttui.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.accept.RequestedContentTypeResolverBuilder;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.Collections;

@Configuration
public class WebConfig implements WebFluxConfigurer {

    @Override
    public void configureContentTypeResolver(RequestedContentTypeResolverBuilder builder) {
        builder.resolver(exchange -> {
            var mediaTypes = exchange.getRequest().getHeaders().getAccept();
            if (mediaTypes.isEmpty()) {
                mediaTypes = Collections.singletonList(MediaType.APPLICATION_JSON);
            }
            // Check if XML is requested and handle it accordingly
            var isXmlRequested = mediaTypes.stream().anyMatch(mediaType -> mediaType.includes(MediaType.APPLICATION_XML));
            if (isXmlRequested) {
                return Collections.singletonList(MediaType.APPLICATION_JSON); // Default to JSON if XML is requested
            }
            return mediaTypes;
        });
    }
}
