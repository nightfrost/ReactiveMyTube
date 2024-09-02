package io.nightfrost.reactivemytube.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class PostProcessWebFilter implements WebFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostProcessWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String applicationPath = exchange.getRequest().getPath().pathWithinApplication().value();
        HttpMethod method = exchange.getRequest().getMethod();
        boolean containsParams = exchange.getRequest().getQueryParams().isEmpty();

        if (applicationPath.contains("movies/") && method.name().contentEquals("GET") && containsParams) {
            exchange.getResponse()
                    .getHeaders().add(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.asMediaType(MimeType.valueOf("video/mp4"))));
            LOGGER.info("headers added");
        } else {
            exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }
        return chain.filter(exchange);
    }

}
