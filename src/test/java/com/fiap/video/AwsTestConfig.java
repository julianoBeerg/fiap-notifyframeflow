package com.fiap.video;

import com.fiap.video.config.S3Config;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AwsTestConfig {

    @Bean
    public S3Config s3Config() {
        S3Config mockConfig = Mockito.mock(S3Config.class);

        Mockito.when(mockConfig.getAccessKeyId()).thenReturn("teste");
        Mockito.when(mockConfig.getSecretAccessKey()).thenReturn("teste");
        Mockito.when(mockConfig.getRegionName()).thenReturn("us-east-1");

        return mockConfig;
    }
}
