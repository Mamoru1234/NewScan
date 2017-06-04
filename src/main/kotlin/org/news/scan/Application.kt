package org.news.scan

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
@EnableConfigurationProperties
@ComponentScan
open class ApplicationConfig {

  @Bean
  open fun run() = CommandLineRunner {
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(ApplicationConfig::class.java)
}
