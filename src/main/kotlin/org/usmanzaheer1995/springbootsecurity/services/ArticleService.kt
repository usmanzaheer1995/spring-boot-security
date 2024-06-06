package org.usmanzaheer1995.springbootsecurity.services

import org.springframework.stereotype.Service
import org.usmanzaheer1995.springbootsecurity.repositories.ArticleRepository

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
) {
    fun findAll() = articleRepository.findAll()
}
