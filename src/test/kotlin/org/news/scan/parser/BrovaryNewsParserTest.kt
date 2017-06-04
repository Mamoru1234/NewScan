package org.news.scan.parser

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test
import java.time.LocalDate

/**
 */
class BrovaryNewsParserTest {
  @Test
  fun testParse() {
    val parser = BrovaryNewsParser()
    println(parser.getDocuments(0))
  }
  @Test
  fun testParseDateLine() {
    val date = parseDateLine("343, 1 Червня 2017")
    assertThat(date, equalTo(LocalDate.of(2017, 6,1)))
  }
}
