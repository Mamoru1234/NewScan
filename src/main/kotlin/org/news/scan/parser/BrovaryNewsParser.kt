package org.news.scan.parser

import org.jsoup.Jsoup
import org.news.scan.extension.debug
import org.news.scan.extension.logger
import org.news.scan.extension.trace
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

fun parseDateLine(line:String):LocalDate {
  val semiIndex = line.lastIndexOf(",")
  if (semiIndex == -1) {
    return LocalDate.parse(line.toLowerCase(), FORMATTER)
  }
  val dateString = line.substring(line.lastIndexOf(",") + 2).toLowerCase()
  return LocalDate.parse(dateString, FORMATTER)
}

@Component
open class BrovaryNewsParser: NewsParser {
  companion object {
    val log by logger()
  }

  override fun getDocuments(offset: Int):List<ParsedDocument> {
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
        val documentPage = Jsoup.connect("http://brovary-rada.gov.ua${it.attr("href")}")
          .get()
//        TODO Error handling
        val dateLine = documentPage.select(DATE_LINE_SELECTOR).last().text()
        val content = documentPage.select(CONTENT_SELECTOR).last().text()
        ParsedDocument(creationDate = parseDateLine(dateLine), content = content, id = matcher.group(1))
      }
  }
}
