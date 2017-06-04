package org.news.scan.service

import org.news.scan.dao.DocumentRepository
import org.news.scan.extension.logger
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

val LATEST_REQUEST = PageRequest(0, 1, Sort.Direction.DESC, "created")

@Component
open class NewsScanService(
  val newsService: NewsService,
  val documentRepository: DocumentRepository
){
  companion object {
    val log by logger()
  }
  val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(4)

  @PostConstruct
  fun init() {
    scheduler.scheduleWithFixedDelay({
      try {
        val latestDate = documentRepository.findAll(LATEST_REQUEST)
          .lastOrNull()
          ?.created
          ?: LocalDate.MIN
        newsService.checkForNewDocuments(latestDate)
      } catch (e: Throwable) {
        log.error("Error during check: ", e)
      }
    } , 0, 10, TimeUnit.SECONDS)
  }

  @PreDestroy
  fun finish() {
    scheduler.shutdownNow()
  }
}
