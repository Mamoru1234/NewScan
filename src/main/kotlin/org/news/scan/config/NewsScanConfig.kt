package org.news.scan.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 */
@Component
@ConfigurationProperties("news.scan")
open class NewsScanConfig(
  var offsetLimit: Int = Int.MAX_VALUE,
  var pageSize: Int = 10
)
