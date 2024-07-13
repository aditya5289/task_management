package myCompany.TaskMangement.UserService;

import org.springframework.boot.SpringApplication;

import myCompany.taskManagement.userService.TaskMangementApplication;

public class TestTaskMangementApplication {

	public static void main(String[] args) {
		SpringApplication.from(TaskMangementApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
