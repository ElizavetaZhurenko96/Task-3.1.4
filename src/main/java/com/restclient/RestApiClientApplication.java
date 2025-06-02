package com.restclient;

import com.restclient.model.User;
import com.restclient.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class RestApiClientApplication implements CommandLineRunner {

    private final ApiService apiService;

    public RestApiClientApplication(ApiService apiService) {
        this.apiService = apiService;
    }

    public static void main(String[] args) {
        SpringApplication.run(RestApiClientApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String result = apiService.executeAll();
        System.out.println("Ответ от сервера:");
        System.out.println(result);
    }
}
