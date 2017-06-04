package org.news.scan.parser

import java.time.LocalDate

/**
 */
data class ParsedDocument(
  val id: String,
  val creationDate: LocalDate,
  val content: String
)
