package org.news.scan.service

import org.news.scan.config.NewsScanConfig
import org.news.scan.dao.DocumentRepository
import org.news.scan.dao.DocumentVersionRepository
import org.news.scan.entity.DocumentEntity
import org.news.scan.entity.DocumentVersionEntity
import org.news.scan.extension.logger
import org.news.scan.extension.trace
import org.news.scan.reader.NewsReader
import org.news.scan.reader.RawDocument
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

/**
 */
@Service
open class NewsService(
  private val documentRepository: DocumentRepository,
  private val documentVersionRepository: DocumentVersionRepository,
  private val newsReader: NewsReader,
  newsScanConfig: NewsScanConfig
) {
  companion object {
    val log by logger()
  }

  val pageSize = newsScanConfig.pageSize
  val offsetLimit = newsScanConfig.offsetLimit

  @Transactional
  open fun checkForNewDocuments(latestDate: LocalDate) {
    log.info("Checking for new documents $latestDate...")
    var offset = 0
    do {
      val rawDocuments = newsReader.getDocuments(offset)

      val documentEntities = rawDocuments.map {
        val rawDocument = it
        documentRepository.findOne(it.id) ?: create(rawDocument)
      }
      documentRepository.save(documentEntities)

      compareAndUpdate(documentEntities, rawDocuments)

      val olderDocument = rawDocuments.asSequence()
        .filter { it.creationDate < latestDate }
        .firstOrNull()
      log.trace {
        "Older document $olderDocument"
      }

      offset += pageSize
    } while (olderDocument == null && !rawDocuments.isEmpty() && offset <= offsetLimit)
    log.info("Finished")
  }

  @Transactional
  open fun checkForUpdates(startDate: LocalDate, endDate: LocalDate) {
    log.info("Checking for updates in range $startDate to $endDate...")
    val documentEntities = documentRepository
      .findByLastUpdateDateBetween(startDate, endDate)
    val rawDocuments = documentEntities
      .map {
        newsReader.getDocumentById(it.id)
      }
    compareAndUpdate(documentEntities, rawDocuments)
    log.info("Update finished")
  }

  fun compareAndUpdate(
    documentEntities: List<DocumentEntity>,
    rawDocuments: List<RawDocument>
  ) {
    val updatedDocumentEntities = documentEntities.zip(rawDocuments)
      .partition { (documentEntity, rawDocument) ->
        updateDocument(documentEntity, rawDocument)
      }
      .first
      .map { it.first }

    val documentVersions = updatedDocumentEntities
      .flatMap {
        it.versions
          ?: emptyList()
      }
    documentVersionRepository.save(documentVersions)
    documentRepository.save(updatedDocumentEntities)
  }

  private fun updateDocument(
    documentEntity: DocumentEntity,
    newVersion: RawDocument
  ): Boolean {
    val updated = documentEntity.versions
      ?.lastOrNull()
      ?.content
      ?.let { it != newVersion.content }
      ?: true
    if (!updated) return updated
    log.debug("New version of ${newVersion.id} ${newVersion.creationDate}")
    val versionEntity = DocumentVersionEntity(
      document = documentEntity,
      content = newVersion.content,
      date = newVersion.creationDate
    )
    val newVersions = documentEntity.versions
      ?.let { it + versionEntity }
      ?.sortedBy { it.date }
    documentEntity.versions = newVersions
      ?.mapIndexed { index, documentVersionEntity ->
        documentVersionEntity.copy(version = index + 1)
      }
    documentEntity.lastUpdateDate = newVersions
      ?.lastOrNull()
      ?.date
      ?: LocalDate.MIN
    return updated
  }

  private fun create(rawDocument: RawDocument): DocumentEntity {
    log.debug("Creation of ${rawDocument.id} ${rawDocument.creationDate}")
    val documentEntity = DocumentEntity(
      id = rawDocument.id,
      lastUpdateDate = rawDocument.creationDate
    )
    documentEntity.versions = emptyList()
    return documentEntity
  }
}
