package org.news.scan.service

import org.news.scan.config.NewsScanConfig
import org.news.scan.dao.DocumentRepository
import org.news.scan.extension.debug
import org.news.scan.extension.logger
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Period
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

val LATEST_REQUEST = PageRequest(0, 1, Sort.Direction.DESC, "lastUpdateDate")

enum class Priority {
  HIGH,
  LOW
}

@Component
open class NewsScanService(
  private val newsService: NewsService,
  private val documentRepository: DocumentRepository,
  newsScanConfig: NewsScanConfig
){
  companion object {
    val log by logger()
  }
  val priorityConfig = newsScanConfig.priorityConfig
  val scheduler: ScheduledExecutorService = Executors
    .newScheduledThreadPool(Priority.values().size + 1)

  @PostConstruct
  fun init() {
    scheduler.scheduleWithFixedDelay(
      this::checkForNewDocuments,
      0, 1, TimeUnit.HOURS
    )
    Priority.values().forEach {
      val (
        startPeriod, endPeriod, unit,
        initialDelay, delay
        ) = priorityConfig[it]!!
      scheduler.scheduleWithFixedDelay(
        createDocumentsUpdateTask(startPeriod, endPeriod),
        initialDelay, delay, unit
      )
    }
  }

  fun checkForNewDocuments() {
    try {
      val latestDate = documentRepository.findAll(LATEST_REQUEST)
        .lastOrNull()
        ?.lastUpdateDate
        ?: LocalDate.MIN
      newsService.checkForNewDocuments(latestDate)
    } catch (e: Throwable) {
      log.error("Error during check: ", e)
    }
  }

  fun createDocumentsUpdateTask(startPeriod: Period, endPeriod: Period): () -> Unit {
    return {
      try {
        val now = LocalDate.now()
        log.debug {
          "Checking updates $now..."
        }
        val documents = documentRepository
          .findByLastUpdateDateBetween(now - startPeriod, now - endPeriod)
        newsService.checkForUpdates(documents)
      } catch (e: Throwable) {
        log.error("Error during update: ", e)
      }
    }
  }

  @PreDestroy
  fun finish() {
    scheduler.shutdownNow()
  }
}
