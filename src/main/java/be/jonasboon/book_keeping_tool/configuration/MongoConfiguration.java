package be.jonasboon.book_keeping_tool.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.mongock.driver.api.driver.ConnectionDriver;
import io.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String databaseUrl;

    private ConnectionString mongoClientURI;

    @Override
    protected String getDatabaseName() {
        return mongoClientURI.getDatabase();
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        mongoClientURI = new ConnectionString(databaseUrl);
        return MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(mongoClientURI)
                        .retryWrites(false)
                        .build()
        );
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }

    @Bean
    public ConnectionDriver mongockConnectionDriver() {
        MongoSync4Driver driver = MongoSync4Driver.withDefaultLock(mongoClient(), getDatabaseName());
        driver.disableTransaction();
        return driver;
    }

}
