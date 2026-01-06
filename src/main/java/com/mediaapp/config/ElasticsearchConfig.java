package com.mediaapp.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * Elasticsearch Configuration
 * Cấu hình kết nối và client cho Elasticsearch
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.mediaapp.repository.elasticsearch")
@ConditionalOnProperty(name = "spring.data.elasticsearch.repositories.enabled", havingValue = "true")
public class ElasticsearchConfig {

    @Value("${spring.elasticsearch.uris}")
    private String elasticsearchUris;

    @Value("${spring.elasticsearch.username:}")
    private String username;

    @Value("${spring.elasticsearch.password:}")
    private String password;

    @Value("${spring.elasticsearch.connection-timeout:10s}")
    private Duration connectionTimeout;

    @Value("${spring.elasticsearch.socket-timeout:30s}")
    private Duration socketTimeout;

    /**
     * Tạo Elasticsearch Client
     * Sử dụng RestClient với connection pooling và timeout configuration
     */
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // Parse Elasticsearch URIs
        String[] uris = elasticsearchUris.split(",");
        HttpHost[] hosts = new HttpHost[uris.length];
        
        for (int i = 0; i < uris.length; i++) {
            String uri = uris[i].trim();
            // Parse URI (format: http://host:port)
            String[] parts = uri.replace("http://", "")
                                .replace("https://", "")
                                .split(":");
            String host = parts[0];
            int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 9200;
            String scheme = uri.startsWith("https") ? "https" : "http";
            
            hosts[i] = new HttpHost(host, port, scheme);
        }

        // Build RestClient
        RestClientBuilder builder = RestClient.builder(hosts)
            .setRequestConfigCallback(requestConfigBuilder ->
                requestConfigBuilder
                    .setConnectTimeout((int) connectionTimeout.toMillis())
                    .setSocketTimeout((int) socketTimeout.toMillis())
            )
            .setHttpClientConfigCallback(httpClientBuilder -> {
                // Configure connection pool
                httpClientBuilder
                    .setMaxConnTotal(100)
                    .setMaxConnPerRoute(50);

                // Add authentication if credentials provided
                if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
                    BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(
                        AuthScope.ANY,
                        new UsernamePasswordCredentials(username, password)
                    );
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }

                return httpClientBuilder;
            });

        RestClient restClient = builder.build();

        // Create transport with Jackson mapper
        RestClientTransport transport = new RestClientTransport(
            restClient,
            new JacksonJsonpMapper()
        );

        return new ElasticsearchClient(transport);
    }
}
