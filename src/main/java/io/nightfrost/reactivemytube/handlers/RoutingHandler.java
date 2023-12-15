package io.nightfrost.reactivemytube.handlers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RoutingHandler {

    private static final String USER_API = "/api/v1/users";

    private static final String ID = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> userRouter(UserHandler userHandler) {
        return route(GET(USER_API), userHandler::getAll)
                .andRoute(POST(USER_API).and(accept(MediaType.APPLICATION_JSON)), userHandler::createUser)
                .andRoute(GET(USER_API + ID), userHandler::getUserById)
                .andRoute(PUT(USER_API + ID).and(accept(MediaType.APPLICATION_JSON)), userHandler::updateUser)
                .andRoute(DELETE(USER_API + ID), userHandler::deleteUser);
    }
/*
See MovieHandler
    @Bean
    public RouterFunction<ServerResponse> movieRouter(MovieHandler movieHandler) {
        return route(GET(MOVIE_API + ID), movieHandler::getMovie)
                .andRoute(POST(MOVIE_API), movieHandler::putMovie);
    }*/
}
