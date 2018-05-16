package bg.deyan.test;

import static java.lang.Boolean.FALSE;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.github.javafaker.Faker;
import com.github.javafaker.service.RandomService;
import com.mysql.cj.jdbc.Driver;

public class InsertionTest {

	private static Logger LOG = LoggerFactory.getLogger(InsertionTest.class);

	private static final Faker FAKER_INSTANCE = Faker.instance(new Locale("bg", "BG"));

	private static final RandomService RANDOM_SERVICE = new RandomService();

	private static final JdbcTemplate jdbcTemplate = new JdbcTemplate(
			DataSourceBuilder.create().url("jdbc:mysql://localhost:3306/diploma")
					.driverClassName(Driver.class.getCanonicalName()).username("root").build());

	private static final String INSERT_QUERY = "INSERT INTO diploma.request_entry "
			+ "(ip_address, response_code, request_url, request_time) VALUES (?, ?, ?, ?);";

	private static final String URL_TEMPLATE = "https://random-site.com/??????";

	private static final int BATCH_SIZE = 1000;

	private static List<String> PERSISTED_IPS = new ArrayList<>();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		URL persistedUsersUrl = Thread.currentThread().getContextClassLoader().getResource("persisted_users.txt");

		if (persistedUsersUrl != null) {
			Files.lines(Paths.get(persistedUsersUrl.toURI())).forEach((s) -> PERSISTED_IPS.add(s));
		} else {
			generatePersistedUsers();
		}
	}
	
	private static void generatePersistedUsers() throws IOException {
		while(PERSISTED_IPS.size() < BATCH_SIZE) {
			PERSISTED_IPS.add(FAKER_INSTANCE.internet().ipV4Address());
		}
		
		Path newFilePath = Paths.get("src/test/resources/persisted_users.txt");
		Files.createFile(newFilePath);
		Files.write(newFilePath, PERSISTED_IPS, Charset.forName("UTF-8"));
	}

	@Test
	void test() {
		int i = BATCH_SIZE;

		while (i > 0) {
			jdbcTemplate.update(INSERT_QUERY, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, PERSISTED_IPS.get(RANDOM_SERVICE.nextInt(PERSISTED_IPS.size() - 1)));
					ps.setInt(2, HttpStatus.values()[RANDOM_SERVICE.nextInt(HttpStatus.values().length - 1)].value());
					ps.setString(3, FAKER_INSTANCE.bothify(URL_TEMPLATE, FALSE));
					ps.setTimestamp(4, getRandomTimestamp());
				}

			});

			i--;
		}
	}

	private Timestamp getRandomTimestamp() {
		try {
			return new Timestamp(FAKER_INSTANCE.date()
					.between(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2011"), Calendar.getInstance().getTime())
					.getTime());
		} catch (IllegalArgumentException | ParseException e) {
			e.printStackTrace();
			return Timestamp.valueOf(LocalDateTime.now());
		}
	}

}
