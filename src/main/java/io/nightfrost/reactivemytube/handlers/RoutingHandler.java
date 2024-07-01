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

    private static final String COMMENT_API = "/api/v1/comments";

    private static final String ID = "/{id}";

    private static final String USER_STRING = "/user";

    private static final String MOVIE_STRING = "/movie";

    private static final String SPECIFIC_STRING = "/specific";

    @Bean
    public RouterFunction<ServerResponse> userRouter(UserHandler userHandler) {
        return route(GET(USER_API), userHandler::getAll)
                .andRoute(POST(USER_API).and(accept(MediaType.APPLICATION_JSON)), userHandler::createUser)
                .andRoute(GET(USER_API + ID), userHandler::getUserById)
                .andRoute(PUT(USER_API + ID).and(accept(MediaType.APPLICATION_JSON)), userHandler::updateUser)
                .andRoute(DELETE(USER_API + ID), userHandler::deleteUser);
    }

    @Bean
    public RouterFunction<ServerResponse> commentRouter(CommentHandler commentHandler) {
        return route(GET(COMMENT_API + MOVIE_STRING + ID), commentHandler::getAllByMovieId)
                .andRoute(POST(COMMENT_API).and(accept(MediaType.APPLICATION_JSON)), commentHandler::createComment)
                .andRoute(GET(COMMENT_API + USER_STRING + ID).and(accept(MediaType.APPLICATION_JSON)), commentHandler::getAllByUserId)
                .andRoute(PUT(COMMENT_API + ID).and(accept(MediaType.APPLICATION_JSON)), commentHandler::updateComment)
                .andRoute(DELETE(COMMENT_API + ID).and(accept(MediaType.APPLICATION_JSON)), commentHandler::deleteComment)
                .andRoute(GET(COMMENT_API + SPECIFIC_STRING + ID), commentHandler::getCommentById);
    }
}
