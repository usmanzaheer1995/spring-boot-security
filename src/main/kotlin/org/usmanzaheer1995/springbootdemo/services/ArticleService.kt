package org.usmanzaheer1995.springbootdemo.services

import org.springframework.stereotype.Service
import org.usmanzaheer1995.springbootdemo.repositories.ArticleRepository

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
) {
    fun findAll() = articleRepository.findAll()
}
