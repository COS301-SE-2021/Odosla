package cs.superleague;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class SpringJdbcPostgreSqlApplication implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args)
    {
        SpringApplication.run(SpringJdbcPostgreSqlApplication.class,args);
    }
    @Override
    public void run(String... args) throws Exception {
        String sql = "INSERT INTO Shopper (shopperid, storeid, orders_completed) VALUES (" +
                "'55534567-9CBC-FEF0-1254-56789ABCDEF0', '01234567-9ABC-DEF0-1234-56789ABCDEF0', '0')";
        int rows = jdbcTemplate.update(sql);
        if(rows >0)
        {
            System.out.println("A new entry has been made");
        }
    }
}
