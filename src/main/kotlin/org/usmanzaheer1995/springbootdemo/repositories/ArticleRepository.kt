package org.usmanzaheer1995.springbootdemo.repositories

import org.usmanzaheer1995.springbootdemo.models.Article

interface ArticleRepository {
    fun findAll(): List<Article>
}
