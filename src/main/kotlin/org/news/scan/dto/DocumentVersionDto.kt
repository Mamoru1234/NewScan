package org.news.scan.dto

import java.time.LocalDate
import java.util.*

/**
 */
data class DocumentVersionDto(
  val id: UUID,
  val date: LocalDate,
  val version: Int
)
