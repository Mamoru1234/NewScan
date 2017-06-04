package org.news.scan.entity

import java.time.LocalDate
import java.util.*
import javax.persistence.*

/**
 */
@Entity
@Table(name = "document")
data class DocumentEntity(
  @Id
  val id: String = UUID.randomUUID().toString(),

  var created: LocalDate = LocalDate.now(),
  @Lob
  @Basic(fetch = FetchType.LAZY)
  var content: String = ""

  )
