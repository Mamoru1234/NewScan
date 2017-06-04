package org.news.scan.reader

import java.time.LocalDate

/**
 */
data class RawDocument(
  val id: String,
  val creationDate: LocalDate,
  val content: String
)
