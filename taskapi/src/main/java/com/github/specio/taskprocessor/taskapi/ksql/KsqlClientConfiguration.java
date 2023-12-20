package com.github.specio.taskprocessor.taskapi.ksql;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ClientOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class KsqlClientConfiguration {
    @Bean
    Client ksqlClient(@Value("${ksqldb.host}") String host,
                      @Value("${ksqldb.port}") int port) {
        ClientOptions options = ClientOptions.create()
                .setHost(host)
                .setPort(port);
        return Client.create(options);
    }

}