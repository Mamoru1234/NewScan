package org.news.scan.reader

import org.junit.Test

/**
 */
class BrovaryNewsReaderTest {
  @Test
  fun testParse() {
    val parser = BrovaryNewsReader()
    println(parser.getDocuments(0))
  }
}
