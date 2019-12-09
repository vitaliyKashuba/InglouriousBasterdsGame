package springApplication.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Configuration
public class DatasourceConfig {

//    @Bean
//    public DataSource getDataSource() {
//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.url("jdbc:postgresql://localhost:5001/ib");
//        dataSourceBuilder.username("root");
//        dataSourceBuilder.password("123123");
//        return dataSourceBuilder.build();
//    }

    @Bean
    public DataSource dataSource() throws URISyntaxException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
//        URI dbUri = new URI("postgres://root:123123@localhost:5001/ib");

        System.out.println("DB URI " + dbUri.toString());

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
//        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        DataSourceBuilder basicDataSource = DataSourceBuilder.create();;
        basicDataSource.url(dbUrl);
        basicDataSource.username(username);
        basicDataSource.password(password);

        return basicDataSource.build();
    }

}
