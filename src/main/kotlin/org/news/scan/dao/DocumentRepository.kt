package org.news.scan.dao

import org.news.scan.entity.DocumentEntity
import org.springframework.data.repository.PagingAndSortingRepository

/**
 */
interface DocumentRepository : PagingAndSortingRepository<DocumentEntity, String>
