package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

@SpringBootApplication
@EnableScheduling
public class SchedulingBugDemoApplication implements SchedulingConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SchedulingBugDemoApplication.class, args);
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("TBU-Scheduler-");
        scheduler.setPoolSize(10);
        // block spring context stopping to allow SI pollers to complete
        // (to graceful shutdown still running tasks, without destroying beans used in these tasks)
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(20);
        return scheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                () -> work(),
                new CronTrigger("*/5 * * * * *"));

        // The second task sits in the scheduler queue and prevents scheduler to shutdown().
        // The task's cancellation lies in taskRegistrar's destroy() method, but no one
        // calls it until context bean destruction is in place. At the same time the context
        // waits for taskScheduler to terminate to continue beans destruction procedure.
        taskRegistrar.addTriggerTask(
                () -> work(),
                new CronTrigger("0 0 0 */1 * *"));
    }

    void work() {
        System.out.println("working.....");
    }

}