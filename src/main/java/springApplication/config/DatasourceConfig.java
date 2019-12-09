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

    /**
     * copy-pasted from hwroku dev-center
     *
     * to keep in secret db credentials (not push to github in .properties, but take from heroku env)
     * local test env is "postgres://root:123123@localhost:5001/ib"
     *
     * @return
     * @throws URISyntaxException
     */
    @Bean
    public DataSource dataSource() throws URISyntaxException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        System.out.println("DB URI " + dbUri.toString());

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        DataSourceBuilder basicDataSource = DataSourceBuilder.create();;
        basicDataSource.url(dbUrl);
        basicDataSource.username(username);
        basicDataSource.password(password);

        return basicDataSource.build();
    }

}
