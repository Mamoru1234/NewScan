package org.news.scan.controller

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch
import org.news.scan.dao.DocumentRepository
import org.news.scan.dao.DocumentVersionRepository
import org.news.scan.error.NotFoundDocumentVersionError
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.transaction.Transactional

/**
 */
@RestController
@RequestMapping("/documents")
open class DocumentController (
  private val documentRepository: DocumentRepository,
  private val documentVersionRepository: DocumentVersionRepository
){

  @GetMapping
  open fun getAllDocuments (
    @RequestParam("page", defaultValue = "0") page: Int,
    @RequestParam("size", defaultValue = "10") size: Int
  ) = documentRepository.findAll(PageRequest(page, size))
    .map { it.toDto() }

  @Transactional
  @GetMapping("/{id}/versions")
  open fun getAllVersions(
    @PathVariable("id") id: String,
    @RequestParam("page", defaultValue = "0") page: Int,
    @RequestParam("size", defaultValue = "10") size: Int
  ) = documentVersionRepository.findByDocumentId(id, PageRequest(page, size))
    .map { it.toDto() }

  @Transactional
  @GetMapping("/{documentId}/version/{versionId}/content")
  open fun getVersionContent(
    @PathVariable("documentId") documentId: String,
    @PathVariable("versionId") versionId: UUID,
    @RequestParam("page", defaultValue = "0") page: Int,
    @RequestParam("size", defaultValue = "10") size: Int
  ) = documentVersionRepository.findByIdAndDocumentId(versionId, documentId)
    ?.content
    ?: throw NotFoundDocumentVersionError(documentId, versionId)

  @Transactional
  @GetMapping("/{documentId}/version/{baseVersionId}/diff/{againstVersionId}")
  open fun getVersionsDiff(
    @PathVariable("documentId") documentId: String,
    @PathVariable("baseVersionId") baseVersionId: UUID,
    @PathVariable("againstVersionId") againstVersionId: UUID
  ): LinkedList<DiffMatchPatch.Patch>? {
    val baseContent = documentVersionRepository.findByIdAndDocumentId(baseVersionId, documentId)
      ?.content ?: throw NotFoundDocumentVersionError(documentId, baseVersionId)
    println("Base Content: $baseContent")
    val againstContent = documentVersionRepository.findByIdAndDocumentId(againstVersionId, documentId)
      ?.content ?: throw NotFoundDocumentVersionError(documentId, againstVersionId)
    return DiffMatchPatch().patchMake(baseContent, againstContent)
  }
}
