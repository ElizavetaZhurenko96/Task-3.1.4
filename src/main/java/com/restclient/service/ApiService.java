package com.restclient.service;

import com.restclient.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ApiService {

    private final RestTemplate restTemplate;
    private String sessionId;

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String executeAll() {
        StringBuilder result = new StringBuilder();

        ResponseEntity<User[]> response = getUsers(null);
        extractSessionId(response);
        printUsers("Запрос GET", response.getBody());

        User newUser = new User();
        newUser.setId(3L);
        newUser.setName("James");
        newUser.setLastName("Brown");
        newUser.setAge(23);

        HttpHeaders postHeaders = createHeadersWithSession();
        postHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> postEntity = new HttpEntity<>(newUser, postHeaders);
        ResponseEntity<String> postResponse = restTemplate.exchange(
                "http://94.198.50.185:7081/api/users",
                HttpMethod.POST,
                postEntity,
                String.class
        );
        result.append(postResponse.getBody());
        printUsers("Запрос POST", getUsers(postHeaders).getBody());

        newUser.setName("Thomas");
        newUser.setLastName("Shelby");
        HttpEntity<User> putEntity = new HttpEntity<>(newUser, postHeaders);
        ResponseEntity<String> putResponse = restTemplate.exchange(
                "http://94.198.50.185:7081/api/users",
                HttpMethod.PUT,
                putEntity,
                String.class
        );
        result.append(putResponse.getBody());
        printUsers("Запрос PUT", getUsers(postHeaders).getBody());

        HttpEntity<String> deleteEntity = new HttpEntity<>(postHeaders);
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                "http://94.198.50.185:7081/api/users/3",
                HttpMethod.DELETE,
                deleteEntity,
                String.class
        );
        result.append(deleteResponse.getBody());
        printUsers("Запрос DELETE", getUsers(postHeaders).getBody());

        return result.toString();
    }

    private ResponseEntity<User[]> getUsers(HttpHeaders headers) {
        if (headers == null) headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                "http://94.198.50.185:7081/api/users",
                HttpMethod.GET,
                entity,
                User[].class
        );
    }

    private void extractSessionId(ResponseEntity<?> response) {
        List<String> cookies = response.getHeaders().get("Set-Cookie");
        if (cookies != null && !cookies.isEmpty()) {
            sessionId = cookies.get(0).split(";")[0];
            System.out.println("Session ID: " + sessionId);
        }
    }

    private HttpHeaders createHeadersWithSession() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionId);
        return headers;
    }

    private void printUsers(String message, User[] users) {
        System.out.println(message);
        if (users != null) {
            Arrays.stream(users).forEach(System.out::println);
        } else {
            System.out.println("Нет пользователей.");
        }
        System.out.println();
    }
}
