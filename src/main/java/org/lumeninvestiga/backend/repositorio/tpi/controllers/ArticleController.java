package org.lumeninvestiga.backend.repositorio.tpi.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.lumeninvestiga.backend.repositorio.tpi.dto.request.ArticleUpdateRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.response.ArticleResponse;
import org.lumeninvestiga.backend.repositorio.tpi.services.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> readAllArticles(@PageableDefault Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.getAllArticles(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArticleResponse>> readAllArticlesByKeyword(
            @PageableDefault Pageable pageable,
            @RequestParam String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.getAllArticlesByKeyword(pageable, keyword));
    }

    @GetMapping("/v2/search")
    public ResponseEntity<Page<ArticleResponse>> searchArticles(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String subArea,
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String ods,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String advisor,
            @RequestParam(required = false) String resume,
            @RequestParam(required = false) String keywords) {
        return ResponseEntity.status(HttpStatus.OK).body(
                articleService.searchArticles(pageable, area, subArea, period, ods, title, author, advisor, resume, keywords)
        );
    }

    @PostMapping(
            value = "/upload",
            consumes = "multipart/form-data",
            produces = "application/json")
    public ResponseEntity<Void> uploadFiles(@RequestParam("files") List<MultipartFile> files, HttpServletRequest httpRequest) {
        articleService.saveArticle(files, httpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Optional<ArticleResponse>> readByName(@PathVariable String name) {
        return ResponseEntity.status(HttpStatus.FOUND).body(articleService.getArticleByName(name));
    }

    @GetMapping("/{article_id}")
    public ResponseEntity<Optional<ArticleResponse>> readArticleById(@PathVariable("article_id") Long articleId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(articleService.getArticleById(articleId));
    }

    //TODO: Terminar de implementar
    @PutMapping("/{article_id}")
    public ResponseEntity<?> updateArticleById(
            @PathVariable("article_id") Long articleId,
            //TODO: terminar de redactar el DTO
            @Valid @RequestParam ArticleUpdateRequest request) {
        // TODO: Implement method in service layer to update article content
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    //TODO: Agregar un @PatchMapping

    @DeleteMapping("/{article_id}")
    public ResponseEntity<Void> deleteArticleById(@PathVariable("article_id") Long articleId){
        articleService.deleteArticleById(articleId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    // Consider removing this method if search functionality is implemented differently.
//    @GetMapping("/search/name/{name}")
//    public ResponseEntity<?> searchArticleByName(@PathVariable String name) {
//        // TODO: Implement method in service layer to search article by name
//        // Adjust to make searches from a frontend search component
//        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
//    }
}
