package com.hitesh.runnerz.user;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;

@Component
public class UserRestClient {

    private final RestClient restClient;

    public UserRestClient(RestClient.Builder builder) {
        JdkClientHttpRequestFactory jdkClientHttpRequestFactory = new JdkClientHttpRequestFactory();
        jdkClientHttpRequestFactory.setReadTimeout(5000);

        this.restClient = builder
                .baseUrl("https://jsonplaceholder.typicode.com")
                // .requestInterceptor(null) //you can use this to intercept request, for
                // example for JWT, this is optional
                // .defaultHeader(null, null) //you can change the header of the requests u
                // send, this is optional
                // .requestFactory(jdkClientHttpRequestFactory) // this step is optional, u and
                // remove this line
                .build();
    }

    public List<User> findAll() {
        return restClient.get()
                .uri("/users")
                .retrieve()
                .body(new ParameterizedTypeReference<List<User>>() {
                });
        // ! new ParameterizedTypeReference<List<User>>(){} is used to convert the body
        // ! into type List<User>
    }

    public User findById(Integer id) {
        return restClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .body(User.class);
        // ! .body(User.class); converts the JSON Obj to User type
    }

}
