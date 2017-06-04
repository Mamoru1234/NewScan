package org.news.scan.reader

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch
import org.junit.Test

/**
 */
class DiffTest {
  @Test
  fun testDiff() {
    println(DiffMatchPatch().diffMain("test\ntemp", "test1\nmp"))
  }
}
