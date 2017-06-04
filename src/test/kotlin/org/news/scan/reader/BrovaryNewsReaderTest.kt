package org.news.scan.reader

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.junit.Test

/**
 */
class BrovaryNewsReaderTest {
  @Test
  fun testParse() {
    val parser = BrovaryNewsReader()
    assertThat(parser.getDocuments(0), hasSize(equalTo(10)))
  }
}
