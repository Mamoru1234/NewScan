package org.news.scan.parser

/**
 */
interface NewsParser {
  fun getDocuments(offset: Int): List<ParsedDocument>
}
