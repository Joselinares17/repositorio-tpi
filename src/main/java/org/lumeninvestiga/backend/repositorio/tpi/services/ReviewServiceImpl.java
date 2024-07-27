package org.lumeninvestiga.backend.repositorio.tpi.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.lumeninvestiga.backend.repositorio.tpi.dto.mapper.ReviewMapper;
import org.lumeninvestiga.backend.repositorio.tpi.dto.request.ReviewPostRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.request.ReviewUpdateRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.response.ReviewResponse;
import org.lumeninvestiga.backend.repositorio.tpi.entities.data.Article;
import org.lumeninvestiga.backend.repositorio.tpi.entities.user.Review;
import org.lumeninvestiga.backend.repositorio.tpi.entities.user.User;
import org.lumeninvestiga.backend.repositorio.tpi.exceptions.NotContentCommentException;
import org.lumeninvestiga.backend.repositorio.tpi.exceptions.NotFoundResourceException;
import org.lumeninvestiga.backend.repositorio.tpi.exceptions.ReferenceNotFoundException;
import org.lumeninvestiga.backend.repositorio.tpi.repositories.ArticleRepository;
import org.lumeninvestiga.backend.repositorio.tpi.repositories.ReviewRepository;
import org.lumeninvestiga.backend.repositorio.tpi.repositories.UserRepository;
import org.lumeninvestiga.backend.repositorio.tpi.security.jwt.JwtService;
import org.lumeninvestiga.backend.repositorio.tpi.utils.Utility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.lumeninvestiga.backend.repositorio.tpi.utils.Utility.extractTokenFromRequest;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final JwtService jwtService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, ArticleRepository articleRepository, JwtService jwtService) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public Optional<ReviewResponse> saveReview(Long articleId, ReviewPostRequest request, HttpServletRequest httpRequest) {
        String token = extractTokenFromRequest(httpRequest);
        String username = jwtService.getUsernameFromToken(token);

        Optional<User> currentUser = userRepository.findByUsername(username);
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        if(currentUser.isEmpty() && articleOptional.isEmpty()) {
            throw new ReferenceNotFoundException("No se encontró referencia para el review");
        }
        if(request.comment().isBlank()) {
            throw new NotContentCommentException("No se encontró comentario en el review");
        }
        Review reviewRequest = new Review();
        reviewRequest.setUser(currentUser.get());
        reviewRequest.getArticle().setId(articleId);
        reviewRequest.setComment(request.comment());
        reviewRepository.save(reviewRequest);
        log.info("save data review {}", reviewRequest);
        return Optional.of(ReviewMapper.INSTANCE.toReviewResponse(reviewRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getAllReviews(Pageable pageable) {
        int page = Utility.getCurrentPage(pageable);
        log.info("query reviews by page {}", page);
        Page<Review> reviews = reviewRepository.findAll(PageRequest.of(page, pageable.getPageSize()));
        return reviews.map(ReviewMapper.INSTANCE::toReviewResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsByArticleId(Long articleId, Pageable pageable) {
        int page = Utility.getCurrentPage(pageable);
        articleRepository.findById(articleId)
                .orElseThrow(
                        () -> new NotFoundResourceException("Artículo no encontrado")
                );
        Page<Review> reviews = reviewRepository.findByArticleId(articleId, PageRequest.of(page, pageable.getPageSize()));
        if(reviews.isEmpty()) {
            throw new NotFoundResourceException("No se encontraron comentarios");
        }
        log.info("get reviews by article id {}", page);
        return reviews.map(ReviewMapper.INSTANCE::toReviewResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewResponse> getReviewById(Long id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if(reviewOptional.isEmpty()) {
            throw new NotFoundResourceException("No se encontró el recurso solicitado");
        }
        log.info("get review by id {}", reviewOptional.get());
        return Optional.of(ReviewMapper.INSTANCE.toReviewResponse(reviewOptional.get()));
    }

    @Override
    public void updateReviewComment(Long id, ReviewUpdateRequest request) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        reviewOptional.ifPresentOrElse(reviewDb -> {
            reviewDb.setComment(request.comment());
        },
                () -> new NotFoundResourceException("No se encontró el recurso solicitado")
        );
        log.info("review updated");
    }

    @Override
    @Transactional
    public void deleteReviewById(Long id) {
        if(existReviewById(id)) {
            reviewRepository.deleteById(id);
        }
        log.info("review deleted");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existReviewById(Long id) {
        log.info("review exists");
        return reviewRepository.existsById(id);
    }
}
