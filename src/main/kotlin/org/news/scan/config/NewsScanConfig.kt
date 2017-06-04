package org.news.scan.config

import org.news.scan.service.Priority
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.time.Period

/**
 */
@Component
@ConfigurationProperties("news.scan")
open class NewsScanConfig(
  var offsetLimit: Int = Int.MAX_VALUE,
  var pageSize: Int = 10,
  var timePeriods: Map<Priority, Pair<Period, Period>> = mapOf(
    Priority.HIGH to (Period.ofDays(3) to Period.ofDays(0)),
    Priority.LOW to (Period.ofMonths(1) to Period.ofDays(3))
  )
)
