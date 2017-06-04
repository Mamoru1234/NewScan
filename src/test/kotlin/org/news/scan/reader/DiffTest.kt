package org.news.scan.reader

import org.junit.Test
import java.time.LocalDate
import java.time.Period

/**
 */
class DiffTest {
  @Test
  fun testDiff() {
    println(LocalDate.now().minus(Period.ofDays(0)))
  }
}
