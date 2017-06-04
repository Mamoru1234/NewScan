package org.news.scan.error

import java.util.*

/**
 */
class NotFoundDocumentVersionError(
  documentId: String,
  versionId: UUID
) : Error("Version with $versionId Not found for $documentId")
