package org.news.scan.service

import org.news.scan.dao.DocumentRepository
import org.news.scan.entity.DocumentEntity
import org.news.scan.extension.logger
import org.news.scan.parser.NewsParser
import org.news.scan.parser.ParsedDocument
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

/**
 */
@Service
open class NewsService(
  val documentRepository: DocumentRepository,
  val newsParser: NewsParser
) {
  companion object {
    val log by logger()
  }
  val offsetLimit = Int.MAX_VALUE

  @Transactional
  open fun checkForNewDocuments(latestDate: LocalDate) {

    log.info("Checking for new documents $latestDate...")
    var offset = 0
    do {
      val documents = newsParser.getDocuments(offset)
      val documentEntities = documents.map {
        val newDocument = it
        documentRepository.findOne(it.id)
          ?.let { this.update(it, newDocument) }
          ?: create(newDocument)
      }
      offset += 10
      val olderDocument = documents.stream()
        .filter { it.creationDate < latestDate }
        .findFirst()
      documentRepository.save(documentEntities)
    } while (!olderDocument.isPresent && !documents.isEmpty() && offset <= offsetLimit)
    log.info("Finished")
  }

  fun update(documentEntity: DocumentEntity, newVersion: ParsedDocument): DocumentEntity {
    log.debug("Update of ${newVersion.id} ${newVersion.creationDate}")
    documentEntity.created = newVersion.creationDate
    documentEntity.content = newVersion.content
    return documentEntity
  }

  fun create(document: ParsedDocument): DocumentEntity {
    log.debug("Creation of ${document.id} ${document.creationDate}")
    return DocumentEntity(id = document.id, content = document.content, created = document.creationDate)
  }
}
