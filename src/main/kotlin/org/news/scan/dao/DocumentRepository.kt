package org.news.scan.dao

import org.news.scan.entity.DocumentEntity
import org.springframework.data.repository.PagingAndSortingRepository
import java.time.LocalDate

/**
 */
interface DocumentRepository : PagingAndSortingRepository<DocumentEntity, String> {
  fun findByLastUpdateDateBetween(startDate: LocalDate, endDate: LocalDate): List<DocumentEntity>
}
