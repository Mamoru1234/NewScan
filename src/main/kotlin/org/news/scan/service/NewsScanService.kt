package org.news.scan.service

import org.news.scan.dto.Document
import org.news.scan.extension.debug
import org.news.scan.extension.logger
import org.springframework.stereotype.Component
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.annotation.PreDestroy

enum class Priority {
    LOW, HIGH, MEDIUM
}

@Component
open class NewsScanService{
    companion object {
        val log by logger()
    }
    val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(4)

    fun scheduleScan(documents: List<Document>, priority: Priority) {
        log.debug {
            documents.toString()
        }
        val (delay, unit) = when (priority) {
            Priority.LOW -> 30 to TimeUnit.MINUTES
            Priority.HIGH -> 30 to TimeUnit.SECONDS
            Priority.MEDIUM -> 10 to TimeUnit.MINUTES
        }
        scheduler.schedule({
            log.debug {
                "Scanning..."
            }
        }, delay.toLong(), unit)
    }

    @PreDestroy
    fun finish() {
        scheduler.shutdownNow()
    }
}