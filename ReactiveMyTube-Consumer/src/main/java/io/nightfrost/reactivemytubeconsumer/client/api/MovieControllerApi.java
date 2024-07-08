package io.nightfrost.reactivemytubeconsumer.client.api;

import io.nightfrost.reactivemytubeconsumer.client.ApiClient;

import io.nightfrost.reactivemytubeconsumer.client.model.MovieDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-07-08T22:09:52.285444900+02:00[Europe/Copenhagen]", comments = "Generator version: 7.7.0")
public class MovieControllerApi {
    private ApiClient apiClient;

    public MovieControllerApi() {
        this(new ApiClient());
    }

    @Autowired
    public MovieControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param query The query parameter
     * @return List&lt;MovieDTO&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getAllAvailableMovies1RequestCreation(String query) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'query' is set
        if (query == null) {
            throw new WebClientResponseException("Missing the required parameter 'query' when calling getAllAvailableMovies1", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "query", query));
        
        final String[] localVarAccepts = { 
            "*/*"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<MovieDTO> localVarReturnType = new ParameterizedTypeReference<MovieDTO>() {};
        return apiClient.invokeAPI("/api/v1/movies", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param query The query parameter
     * @return List&lt;MovieDTO&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Flux<MovieDTO> getAllAvailableMovies1(String query) throws WebClientResponseException {
        ParameterizedTypeReference<MovieDTO> localVarReturnType = new ParameterizedTypeReference<MovieDTO>() {};
        return getAllAvailableMovies1RequestCreation(query).bodyToFlux(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param query The query parameter
     * @return ResponseEntity&lt;List&lt;MovieDTO&gt;&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<List<MovieDTO>>> getAllAvailableMovies1WithHttpInfo(String query) throws WebClientResponseException {
        ParameterizedTypeReference<MovieDTO> localVarReturnType = new ParameterizedTypeReference<MovieDTO>() {};
        return getAllAvailableMovies1RequestCreation(query).toEntityList(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param query The query parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec getAllAvailableMovies1WithResponseSpec(String query) throws WebClientResponseException {
        return getAllAvailableMovies1RequestCreation(query);
    }
    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param id The id parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getMovieRequestCreation(String id) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new WebClientResponseException("Missing the required parameter 'id' when calling getMovie", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("id", id);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI("/api/v1/movies/{id}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param id The id parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Void> getMovie(String id) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return getMovieRequestCreation(id).bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param id The id parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Void>> getMovieWithHttpInfo(String id) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {};
        return getMovieRequestCreation(id).toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @param id The id parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec getMovieWithResponseSpec(String id) throws WebClientResponseException {
        return getMovieRequestCreation(id);
    }
    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @return Object
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec postMovieRequestCreation() throws WebClientResponseException {
        Object postBody = null;
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "*/*"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {};
        return apiClient.invokeAPI("/api/v1/movies", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @return Object
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Object> postMovie() throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {};
        return postMovieRequestCreation().bodyToMono(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @return ResponseEntity&lt;Object&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Object>> postMovieWithHttpInfo() throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {};
        return postMovieRequestCreation().toEntity(localVarReturnType);
    }

    /**
     * 
     * 
     * <p><b>200</b> - OK
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec postMovieWithResponseSpec() throws WebClientResponseException {
        return postMovieRequestCreation();
    }
}
