package io.nightfrost.reactivemytube.handlers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Component
public class RoutingHandler {

    private static final String USER_API = "/api/v1/users";

    private static final String COMMENT_API = "/api/v1/comments";

    private static final String ID = "/{id}";

    private static final String USER_STRING = "/user";

    private static final String MOVIE_STRING = "/movie";

    private static final String SPECIFIC_STRING = "/specific";

    @Bean
    /*@RouterOperations({
            @RouterOperation(path = USER_API, beanClass = UserHandler.class, beanMethod = "getAll", method = RequestMethod.GET, operation = @Operation(operationId = "getAllUsers", method = "GET")),
            @RouterOperation(path = USER_API + ID, beanClass = UserHandler.class, beanMethod = "getUserById", method = RequestMethod.GET, operation = @Operation(operationId = "getUserById", method = "GET", parameters = { @Parameter(name = "id", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, required = true, schema = @Schema(implementation = String.class))})),
            @RouterOperation(path = USER_API, beanClass = UserHandler.class, beanMethod = "createUser", method = RequestMethod.POST, operation = @Operation(operationId = "createUser", method = "POST", requestBody = @RequestBody(required = true, description = "User Form", content = @Content(schema = @Schema(implementation = User.class))))),
            @RouterOperation(path = USER_API + ID, beanClass = UserHandler.class, beanMethod = "updateUser", method = RequestMethod.PUT, operation = @Operation(operationId = "updateUser", method = "PUT", requestBody = @RequestBody(required = true, description = "User Form", content = @Content(schema = @Schema(implementation = User.class))), parameters = { @Parameter(name = "id", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, required = true, schema = @Schema(implementation = String.class))})),
            @RouterOperation( path = USER_API + ID, beanClass = UserHandler.class, beanMethod = "deleteUser", method = RequestMethod.DELETE, operation = @Operation(operationId = "deleteUser", method = "DELETE", parameters = { @Parameter(name = "id", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, required = true, schema = @Schema(implementation = String.class))}))
    })*/
    public RouterFunction<ServerResponse> userRouter(UserHandler userHandler) {
        return route(GET(USER_API), userHandler::getAll)
                .andRoute(POST(USER_API).and(accept(MediaType.APPLICATION_JSON)), userHandler::createUser)
                .andRoute(GET(USER_API + ID), userHandler::getUserById)
                .andRoute(PUT(USER_API + ID).and(accept(MediaType.APPLICATION_JSON)), userHandler::updateUser)
                .andRoute(DELETE(USER_API + ID), userHandler::deleteUser);
    }

    @Bean
    /*@RouterOperations({
            @RouterOperation(path = COMMENT_API + MOVIE_STRING + ID, beanClass = CommentHandler.class, beanMethod = "getAllByMovieId", method = RequestMethod.GET, operation = @Operation(operationId = "getAllByMovieId", method = "GET", parameters = { @Parameter(name = "id", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, required = true, schema = @Schema(implementation = String.class))})),
            @RouterOperation(path = COMMENT_API + USER_STRING + ID, beanClass = CommentHandler.class, beanMethod = "getAllByUserId", method = RequestMethod.GET, operation = @Operation(operationId = "getAllByUserId", method = "GET", parameters = { @Parameter(name = "id", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, required = true, schema = @Schema(implementation = String.class))})),
            @RouterOperation(path = COMMENT_API, beanClass = CommentHandler.class, beanMethod = "createComment", method = RequestMethod.POST, operation = @Operation(operationId = "createComment", method = "POST", requestBody = @RequestBody(required = true, description = "Comment Form", content = @Content(schema = @Schema(implementation = Comment.class))))),
            @RouterOperation(path = COMMENT_API + ID, beanClass = CommentHandler.class, beanMethod = "updateComment", method = RequestMethod.PUT, operation = @Operation(operationId = "updateComment", method = "PUT", requestBody = @RequestBody(required = true, description = "Comment Form", content = @Content(schema = @Schema(implementation = Comment.class))), parameters = { @Parameter(name = "id", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, required = true, schema = @Schema(implementation = String.class))})),
            @RouterOperation(path = COMMENT_API + SPECIFIC_STRING + ID, beanClass = CommentHandler.class, beanMethod = "getCommentById", method = RequestMethod.GET, operation = @Operation(operationId = "getCommentById", method = "GET", parameters = { @Parameter(name = "id", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, required = true, schema = @Schema(implementation = String.class))})),
            @RouterOperation(path = COMMENT_API + ID, beanClass = CommentHandler.class, beanMethod = "deleteComment", method = RequestMethod.DELETE, operation = @Operation(operationId = "deleteComment", method = "DELETE", parameters = { @Parameter(name = "id", in = ParameterIn.PATH, style = ParameterStyle.SIMPLE, required = true, schema = @Schema(implementation = String.class))}))
    })*/
    public RouterFunction<ServerResponse> commentRouter(CommentHandler commentHandler) {
        return route(GET(COMMENT_API + MOVIE_STRING + ID), commentHandler::getAllByMovieId)
                .andRoute(POST(COMMENT_API).and(accept(MediaType.APPLICATION_JSON)), commentHandler::createComment)
                .andRoute(GET(COMMENT_API + USER_STRING + ID).and(accept(MediaType.APPLICATION_JSON)), commentHandler::getAllByUserId)
                .andRoute(PUT(COMMENT_API + ID).and(accept(MediaType.APPLICATION_JSON)), commentHandler::updateComment)
                .andRoute(DELETE(COMMENT_API + ID).and(accept(MediaType.APPLICATION_JSON)), commentHandler::deleteComment)
                .andRoute(GET(COMMENT_API + ID), commentHandler::getCommentById);
    }
}
