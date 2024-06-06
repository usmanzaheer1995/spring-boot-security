package org.usmanzaheer1995.springbootsecurity.repositories.impl

import org.springframework.stereotype.Repository
import org.usmanzaheer1995.springbootsecurity.models.Article
import org.usmanzaheer1995.springbootsecurity.repositories.ArticleRepository
import java.util.UUID

@Repository
class ArticleRepositoryImpl : ArticleRepository {
    private val articles =
        listOf(
            Article(id = UUID.randomUUID(), title = "First Article", content = "This is the first article"),
            Article(id = UUID.randomUUID(), title = "Second Article", content = "This is the second article"),
        )

    override fun findAll(): List<Article> {
        return articles
    }
}
