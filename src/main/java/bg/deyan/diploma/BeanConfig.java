package bg.deyan.diploma;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class BeanConfig {

	@Bean
	@Autowired
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
		// DataSourceBuilder.create().url("jdbc:mysql://localhost:3306/diploma")
		// .driverClassName(Driver.class.getCanonicalName()).username("root").build());
	}

}
