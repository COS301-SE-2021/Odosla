package cs.superleague;

import cs.superleague.user.repos.ShopperRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringJdbcPostgreSqlApplication implements CommandLineRunner {


    public static void main(String[] args)
    {
        SpringApplication.run(SpringJdbcPostgreSqlApplication.class,args);
    }
    @Override
    public void run(String... args) throws Exception {

    }
}
