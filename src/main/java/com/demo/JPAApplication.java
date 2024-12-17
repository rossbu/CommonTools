/* (C)2021 */
package com.demo;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.boot.SpringApplication.run;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * command : ./mvnw clean spring-boot:run or mvn clean spring-boot:run
 *
 * <p>Create newone curl -X POST localhost:8080/employees -H 'Content-type:application/json' -d
 * '{"name": "Samwise Gamgee", "role": "gardener"}' Update curl -X PUT localhost:8080/employees/3 -H
 * 'Content-type:application/json' -d '{"name": "Samwise Gamgee", "role": "ring bearer"}'
 *
 * <p>
 *
 * <p>GetAll: curl -v localhost:8080/employees Delete one curl -X DELETE localhost:8080/employees/3
 */
// @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
@Configuration
// @EnableJpaRepositories(basePackages = "com.demo", entityManagerFactoryRef = "factoryBean")
@EnableJpaRepositories
public class JPAApplication {
  private static final Logger log = getLogger(JPAApplication.class);

  @Bean
  CommandLineRunner init(EmployeeRepository repository) {
    return args -> {
      log.info("Preloading " + repository.save(new Employee("Cargo", "B")));
      log.info("Preloading " + repository.save(new Employee("Kaushik", "murfy")));
    };
  }

  public static void main(String... args) {
    run(JPAApplication.class, args);
  }
}

// @Configuration
// class JPAConfig {
//  @Bean
//  public EntityManager entityManager() {
//    return entityManagerFactory().getObject().createEntityManager();
//  }
// }

class EmployeeNotFoundException extends RuntimeException {

  EmployeeNotFoundException(Long id) {
    super("Could not find employee " + id);
  }
}

/**
 * @ResponseBody signals that this advice is rendered straight into the response
 * body. @ExceptionHandler configures the advice to only respond if an EmployeeNotFoundException is
 * thrown. @ResponseStatus says to issue an HttpStatus.NOT_FOUND, i.e. an HTTP 404.
 *
 * <p>The body of the advice generates the content. In this case, it gives the message of the
 * exception.
 */
@ControllerAdvice
class EmployeeNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(EmployeeNotFoundException.class)
  @ResponseStatus(NOT_FOUND)
  String employeeNotFoundHandler(EmployeeNotFoundException ex) {
    return ex.getMessage();
  }
}

/**
 * Spring makes accessing data easy. By simply declaring the following EmployeeRepository interface
 * we automatically will be able to
 *
 * <p>Create new Employees
 *
 * <p>Update existing ones
 *
 * <p>Delete Employees
 *
 * <p>Find Employees (one, all, or search by simple or complex properties)
 */
@Repository
interface EmployeeRepository extends JpaRepository<Employee, Long> {}

@Entity
@Getter
@Setter
class Employee {
  @Id
  @GeneratedValue
  @Column(name = "_id")
  private Long id;

  private String name;
  private String role;

  public Employee() {}

  public Employee(String name, String role) {
    this.name = name;
    this.role = role;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}

@RestController
@Data
class EmployeeController {
  private final EmployeeRepository repository;

  // Aggregate root
  // tag::get-aggregate-root[]
  @GetMapping("/employees")
  List<Employee> all() {
    return repository.findAll();
  }

  // end::get-aggregate-root[]

  @PostMapping("/employees")
  Employee newEmployee(@RequestBody Employee newEmployee) {
    return repository.save(newEmployee);
  }

  // Single item

  @GetMapping("/employees/{id}")
  Employee one(@PathVariable Long id) {
    return repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
  }

  @PutMapping("/employees/{id}")
  Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
    return repository
        .findById(id)
        .map(
            employee -> {
              employee.setName(newEmployee.getName());
              employee.setRole(newEmployee.getRole());
              return repository.save(employee);
            })
        .orElseGet(
            () -> {
              newEmployee.setId(id);
              return repository.save(newEmployee);
            });
  }

  @DeleteMapping("/employees/{id}")
  void deleteEmployee(@PathVariable Long id) {
    repository.deleteById(id);
  }
}
