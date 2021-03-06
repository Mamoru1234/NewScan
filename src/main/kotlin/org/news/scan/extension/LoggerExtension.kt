package org.news.scan.extension

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.companionObject

// unwrap companion class to enclosing class given a Java Class
fun <T: Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
  return if (ofClass.enclosingClass != null && ofClass.enclosingClass.kotlin.companionObject?.java == ofClass) {
    ofClass.enclosingClass
  } else {
    ofClass
  }
}

fun <R:Any> R.logger(): Lazy<Logger> {
  return lazy { LoggerFactory.getLogger(unwrapCompanionClass(this.javaClass)) }
}

fun Logger.debug(log: () -> String) {
  if (this.isDebugEnabled) {
      this.debug(log())
  }
}

fun Logger.trace(log: () -> String) {
  if (this.isTraceEnabled) {
    this.trace(log())
  }
}
