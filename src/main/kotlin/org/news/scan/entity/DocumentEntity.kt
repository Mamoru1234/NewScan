package org.news.scan.entity

import org.news.scan.dto.DocumentDto
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
) {
  fun toDto(): DocumentDto = DocumentDto(
    id = id,
    lastUpdateDate = lastUpdateDate,
    versions = versions?.map { it.id } ?: emptyList()
  )
}
