package org.usmanzaheer1995.springbootsecurity.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.usmanzaheer1995.springbootsecurity.models.responses.ArticleResponse
import org.usmanzaheer1995.springbootsecurity.services.ArticleService

@RestController
@RequestMapping("/api/article")
class ArticleController(
    private val articleService: ArticleService,
) {
    @GetMapping
    fun allArticles(): ResponseEntity<List<ArticleResponse>> {
        val articles =
            articleService.findAll()
                .map { it.toResponse() }
        return ResponseEntity.ok(articles)
    }
}
