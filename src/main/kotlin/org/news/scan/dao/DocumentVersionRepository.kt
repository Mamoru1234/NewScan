package org.news.scan.dao

import org.news.scan.entity.DocumentVersionEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

/**
 */
interface DocumentVersionRepository: CrudRepository<DocumentVersionEntity, UUID>
