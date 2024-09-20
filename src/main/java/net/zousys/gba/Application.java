package net.zousys.gba;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import net.zousys.gba.function.batch.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@EnableWebSecurity
@SpringBootApplication(
    exclude = ErrorMvcAutoConfiguration.class)

//@EntityScan(basePackages = {
//        "net.zousys.gbs.entity"
//})
//@EnableJpaRepositories(basePackages = {
//        "net.zousys.gbs.repository"
//})
public class Application  implements CommandLineRunner {
    @Autowired
    private BatchService batchService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        batchService.runJobA("Sa2");
        batchService.runJobB("Sb2");
    }
}
