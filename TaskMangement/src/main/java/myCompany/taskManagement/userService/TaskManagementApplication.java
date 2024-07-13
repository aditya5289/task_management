package myCompany.taskManagement.userService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import myCompany.taskManagement.userService.config.DatabaseInitializer;

@SpringBootApplication
@Import(DatabaseInitializer.class)
public class TaskManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskManagementApplication.class, args);
    }
}
