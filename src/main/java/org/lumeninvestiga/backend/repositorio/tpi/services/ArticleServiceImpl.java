package org.lumeninvestiga.backend.repositorio.tpi.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.lumeninvestiga.backend.repositorio.tpi.dto.mapper.ArticleMapper;
import org.lumeninvestiga.backend.repositorio.tpi.dto.response.ArticleResponse;
import org.lumeninvestiga.backend.repositorio.tpi.entities.data.Article;
import org.lumeninvestiga.backend.repositorio.tpi.entities.user.User;
import org.lumeninvestiga.backend.repositorio.tpi.exceptions.*;
import org.lumeninvestiga.backend.repositorio.tpi.repositories.ArticleRepository;
import org.lumeninvestiga.backend.repositorio.tpi.repositories.UserRepository;
import org.lumeninvestiga.backend.repositorio.tpi.security.jwt.JwtService;
import org.lumeninvestiga.backend.repositorio.tpi.utils.PDFAcademicExtractor;
import org.lumeninvestiga.backend.repositorio.tpi.utils.Utility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.lumeninvestiga.backend.repositorio.tpi.utils.Utility.extractTokenFromRequest;
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService{
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final PDFAcademicExtractor pdfAcademicExtractor;
    private final JwtService jwtService;

    public ArticleServiceImpl(ArticleRepository articleRepository, UserRepository userRepository, PDFAcademicExtractor pdfAcademicExtractor, JwtService jwtService) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.pdfAcademicExtractor = pdfAcademicExtractor;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public Optional<ArticleResponse> saveArticle(List<MultipartFile> multipartFiles, HttpServletRequest httpRequest) {
        String token = extractTokenFromRequest(httpRequest);
        String username = jwtService.getUsernameFromToken(token);

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(ReferenceNotFoundException::new);

        validateFiles(multipartFiles);

        MultipartFile articleFile = null;
        MultipartFile fichaFile = null;

        for (MultipartFile file : multipartFiles) {
            String fileName = file.getOriginalFilename().toLowerCase();
            if (fileName.startsWith("articulo") || fileName.startsWith("artículo")) {
                articleFile = file;
            } else if (fileName.startsWith("ficha")) {
                fichaFile = file;
            } else {
                throw new InvalidDocumentFormatException("Formato de archivo no reconocido: " + fileName);
            }
        }

        if (articleFile == null || fichaFile == null) {
            throw new ResourceCountException("Se esperan dos documentos, uno de tipo artículo y otro de tipo ficha");
        }

        Article articleDb = new Article();
        try {
            articleDb.setUser(currentUser);
            articleDb.setName(articleFile.getOriginalFilename());
            articleDb.setSize(articleFile.getSize());
            articleDb.setCreatedDate(LocalDateTime.now());
            articleDb.setMimeType(articleFile.getContentType());
            articleDb.setData(articleFile.getBytes());

            List<String> articleList = pdfAcademicExtractor.readArticleByBytes(articleFile.getBytes());
            List<String> fichaList = pdfAcademicExtractor.readFichaByBytes(fichaFile.getBytes());

            articleDb.getArticleDetail().setTitle(getTitle(articleList, fichaList));
            articleDb.getArticleDetail().setAuthor(articleList.get(1));
            articleDb.getArticleDetail().setAdvisor(articleList.get(2));
            articleDb.getArticleDetail().setPeriod(articleList.get(3));
            articleDb.getArticleDetail().setResume(articleList.get(4));
            articleDb.getArticleDetail().setKeywords(articleList.get(5));

            articleDb.getArticleDetail().setArea(getArea(fichaList));
            articleDb.getArticleDetail().setSubArea(getSubArea(fichaList));
            articleDb.getArticleDetail().setOds(fichaList.get(3));

            articleRepository.save(articleDb);
        } catch (IOException e) {
            throw new SavingErrorException("Error al guardar el artículo", e);
        }
        log.info("article saved to user {}", username);
        return Optional.of(ArticleMapper.INSTANCE.toArticleResponse(articleDb));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleResponse> getAllArticles(Pageable pageable) {
        int page = Utility.getCurrentPage(pageable);
        log.info("query articles by page {}", page);
        return articleRepository.findAll(PageRequest.of(page, pageable.getPageSize())).stream()
                .map(ArticleMapper.INSTANCE::toArticleResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleResponse> getAllArticlesByKeyword(Pageable pageable, String keyword) {
        int page = Utility.getCurrentPage(pageable);
        log.info("query articles by page and keyword {}", page);
        return articleRepository.findByTitleContaining(PageRequest.of(page, pageable.getPageSize()), keyword)
                .stream()
                .map(ArticleMapper.INSTANCE::toArticleResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArticleResponse> getArticleById(Long id) {
        Optional<Article> articleOptional = articleRepository.findById(id);
        if(articleOptional.isEmpty()) {
            throw new NotFoundResourceException("Entidad no encontrada");
        }
        log.info("get article by id {}", articleOptional.get());
        return Optional.of(ArticleMapper.INSTANCE.toArticleResponse(articleOptional.get()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArticleResponse> getArticleByName(String name) {
        Optional<Article> articleOptional = articleRepository.findByName(name);
        if(articleOptional.isEmpty()) {
            throw new NotFoundResourceException("Entidad no encontrada");
        }
        log.info("get article by name {}", articleOptional.get());
        return Optional.of(ArticleMapper.INSTANCE.toArticleResponse(articleOptional.get()));
    }

    @Override
    @Transactional
    public void deleteArticleById(Long id) {
        if(existArticleById(id)) {
            articleRepository.deleteById(id);
            log.info("article deleted {}", id);
        }
        log.info("article not found");
    }

    @Override
    @Transactional
    public boolean existArticleById(Long id) {
        log.info("article exists {}", id);
        return articleRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArticleResponse> searchArticles(Pageable pageable, String area, String subArea, String period, String ods, String title, String author, String advisor, String resume, String keywords) {
        Specification<Article> spec = Specification.where(null);

        Map<String, String> criteriaMap = new HashMap<>();
        criteriaMap.put("area", area);
        criteriaMap.put("subArea", subArea);
        criteriaMap.put("period", period);
        criteriaMap.put("ods", ods);
        criteriaMap.put("title", title);
        criteriaMap.put("author", author);
        criteriaMap.put("advisor", advisor);
        criteriaMap.put("resume", resume);
        criteriaMap.put("keywords", keywords);

        for (Map.Entry<String, String> entry : criteriaMap.entrySet()) {
            if (StringUtils.hasText(entry.getValue())) {
                spec = spec.and(createSpecification(entry.getKey(), entry.getValue()));
            }
        }

        Page<Article> articles = articleRepository.findAll(spec, pageable);
        return articles.map(ArticleMapper.INSTANCE::toArticleResponse);
    }

    private Specification<Article> createSpecification(String key, String value) {
        return (root, query, cb) -> {
            switch (key) {
                case "area":
                case "subArea":
                case "period":
                case "ods":
                    return cb.equal(root.get("articleDetail").get(key), value);
                case "title":
                case "author":
                case "advisor":
                case "resume":
                case "keywords":
                    return cb.like(cb.lower(root.get("articleDetail").get(key)), "%" + value.toLowerCase() + "%");
                default:
                    return null;
            }
        };
    }

    private void validateFiles(List<MultipartFile> files) {
        if (files.size() != 2 || files.isEmpty()) {
            throw new ResourceCountException("Se esperan dos documentos");
        }
    }

    private String getTitle(List<String> articleList, List<String> fichaList) {
        return articleList.get(0).equals("No encontrado") ? fichaList.get(0) : "No encontrado";
    }

    private String getArea(List<String> fichaList) {
        return fichaList.get(2).split(",")[0].trim();
    }

    private String getSubArea(List<String> fichaList) {
        return fichaList.get(2).split(",")[1].trim();
    }
}
