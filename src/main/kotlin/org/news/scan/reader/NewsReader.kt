package org.news.scan.reader

/**
 */
interface NewsReader {
  fun getDocuments(offset: Int): List<RawDocument>
  fun getDocumentById(documentId: String): RawDocument
}
