package org.news.scan

import org.news.scan.service.NewsScanService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@ComponentScan
open class ApplicationConfig {
    @Bean
    open fun init(newsScanService: NewsScanService) = CommandLineRunner {
        newsScanService.scheduleScan(12)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ApplicationConfig::class.java)
}