package org.news.scan.dao

import org.news.scan.entity.DocumentVersionEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*

/**
 */
interface DocumentVersionRepository: PagingAndSortingRepository<DocumentVersionEntity, UUID> {
  fun findByDocumentId(documentId: String, page: Pageable): Page<DocumentVersionEntity>
  fun findByIdAndDocumentId(id: UUID, documentId: String): DocumentVersionEntity?
}
