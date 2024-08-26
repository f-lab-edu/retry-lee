package com.admin.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@Configuration
@EnableJpaRepositories(basePackages = "com.storage.repository")
@EntityScan(basePackages = "com.storage.entity")
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class StorageConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        /*
         * 이 부분은 Admin 회원가입, 로그인 구현시 수정 예정.
         */
        return () -> Optional.of("system");
    }
}
