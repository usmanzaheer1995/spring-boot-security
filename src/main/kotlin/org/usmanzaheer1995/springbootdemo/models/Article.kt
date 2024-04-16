package org.usmanzaheer1995.springbootdemo.models

import org.usmanzaheer1995.springbootdemo.models.responses.ArticleResponse
import java.util.UUID

data class Article(
    val id: UUID,
    val title: String,
    val content: String,
) {
    fun toResponse() =
        ArticleResponse(
            id = this.id.toString(),
            title = this.title,
            content = this.content,
        )
}
