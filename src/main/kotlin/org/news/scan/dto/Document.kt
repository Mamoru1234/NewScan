package org.news.scan.dto

import java.time.LocalDate

data class Document(
  val id: String,
  val creationDate: LocalDate,
  val content: String
)
