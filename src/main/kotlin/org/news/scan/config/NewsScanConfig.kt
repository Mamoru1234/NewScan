package org.news.scan.config

import org.news.scan.service.Priority
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.time.Period
import java.util.concurrent.TimeUnit

data class PriorityScheduleValues(
  var startPeriod: Period,
  var endPeriod: Period,
  var unit: TimeUnit,
  var initialDelay: Long,
  var delay: Long
)

/**
 */
@Component
@ConfigurationProperties("news.scan")
open class NewsScanConfig(
  var offsetLimit: Int = Int.MAX_VALUE,
  var pageSize: Int = 10,
  var checkDelay: Long = 1,
  var checkUnit: TimeUnit = TimeUnit.HOURS,

  var priorityConfig: Map<Priority, PriorityScheduleValues> = mapOf(
    Priority.HIGH to PriorityScheduleValues(
      startPeriod = Period.ofDays(3),
      endPeriod = Period.ofDays(0),
      unit = TimeUnit.MINUTES,
      initialDelay = 30,
      delay = 10
    ),
    Priority.LOW to PriorityScheduleValues(
      startPeriod = Period.ofMonths(1),
      endPeriod = Period.ofDays(4),
      unit = TimeUnit.DAYS,
      initialDelay = 2,
      delay = 5
    )
  )
)
