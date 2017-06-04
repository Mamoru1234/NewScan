package org.news.scan.entity

import org.news.scan.dto.DocumentVersionDto
import java.time.LocalDate
import java.util.*
import javax.persistence.*

/**
 */
@Entity
@Table(name = "document_version")
data class DocumentVersionEntity(
  @Id
  val id: UUID = UUID.randomUUID(),

  val version: Int = 1,

  val date: LocalDate = LocalDate.MIN,

  @Lob
  @Basic(fetch = FetchType.LAZY)
  var content: String = "",

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_document_id")
  var document: DocumentEntity? = null

) {
  fun toDto() = DocumentVersionDto(
    id = id,
    version = version,
    date = date
  )
}
