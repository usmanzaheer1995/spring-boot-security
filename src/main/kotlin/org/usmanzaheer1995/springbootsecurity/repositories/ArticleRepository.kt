package org.usmanzaheer1995.springbootsecurity.repositories

import org.usmanzaheer1995.springbootsecurity.models.Article

interface ArticleRepository {
    fun findAll(): List<Article>
}
