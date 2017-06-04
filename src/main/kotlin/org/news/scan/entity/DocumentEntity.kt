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

  var lastUpdateDate: LocalDate = LocalDate.now(),

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "document")
  var versions: List<DocumentVersionEntity>? = null

)
