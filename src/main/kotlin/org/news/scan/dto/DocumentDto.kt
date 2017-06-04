package org.news.scan.dto

import java.time.LocalDate
import java.util.*

/**
 */
data class DocumentDto(
  val id: String,
  val lastUpdateDate: LocalDate,
  val versions: List<UUID>
)
