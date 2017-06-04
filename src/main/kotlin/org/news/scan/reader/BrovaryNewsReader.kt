package org.news.scan.reader

import org.jsoup.Jsoup
import org.news.scan.extension.debug
import org.news.scan.extension.logger
import org.news.scan.extension.trace
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern



/**
 */
val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM uuuu", Locale("uk"))
val ID_PATTERN: Pattern = Pattern.compile("^/documents/(\\d+)\\.html")
val CONTENT_SELECTOR = "#resSerch > div > div > div.bg1-content.col-md-8.col-sm-8"
val DATE_LINE_SELECTOR = ".container .breadcrumb .active"

private fun parseDateLine(line:String):LocalDate {
  val semiIndex = line.lastIndexOf(",")
  if (semiIndex == -1) {
    return LocalDate.parse(line.toLowerCase(), FORMATTER)
  }
  val dateString = line.substring(line.lastIndexOf(",") + 2).toLowerCase()
  return LocalDate.parse(dateString, FORMATTER)
}

@ConditionalOnProperty("news.scan.reader", havingValue = "brovary")
@Component
open class BrovaryNewsReader : NewsReader {
  companion object {
    private val log by logger()
  }

  override fun getDocumentById(documentId: String): RawDocument {
    val documentPage = Jsoup.connect("http://brovary-rada.gov.ua/documents/$documentId.html")
      .get()
//        TODO Error handling
    val dateLine = documentPage.select(DATE_LINE_SELECTOR).last().text()
    val content = documentPage.select(CONTENT_SELECTOR).last().html()
    return RawDocument(
      creationDate = parseDateLine(dateLine),
      content = content,
      id = documentId
    )
  }

  override fun getDocuments(offset: Int):List<RawDocument> {
    log.debug {
      "Fetching documents with offset: $offset"
    }
    val page = Jsoup.connect("http://brovary-rada.gov.ua/documents/")
      .data("start", "$offset")
      .get()
    return page.select("#resSerch .bg1-content a[href$=\"html\"]")
      .mapNotNull {
        log.trace { "Mapping page..." }
        val href = it.attr("href")
        val matcher = ID_PATTERN.matcher(href)
        if (!matcher.find()) {
          log.error("Wrong href: $href on offset: $offset")
          return@mapNotNull null
        }
        getDocumentById(matcher.group(1))
      }
  }
}
