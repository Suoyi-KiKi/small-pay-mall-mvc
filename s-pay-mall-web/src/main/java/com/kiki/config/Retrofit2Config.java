package com.kiki.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import com.kiki.service.weixin.IweixinApiService;

@Configuration  
public class Retrofit2Config {

    private static final String BASE_URL = "https://api.weixin.qq.com/";
    
    @Bean
    public Retrofit retrofit() {
        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    }

    @Bean
    public IweixinApiService iweixinApiService(Retrofit retrofit) {
        return retrofit().create(IweixinApiService.class);
    }
}