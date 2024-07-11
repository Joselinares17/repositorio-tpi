package org.lumeninvestiga.backend.repositorio.tpi.services;

import jakarta.servlet.http.HttpServletRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.request.ReviewPostRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.request.ReviewUpdateRequest;
import org.lumeninvestiga.backend.repositorio.tpi.dto.response.ReviewResponse;
import org.lumeninvestiga.backend.repositorio.tpi.entities.user.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
// C R U D
public interface ReviewService {
    Optional<ReviewResponse> saveReview(Long articleId, ReviewPostRequest request, HttpServletRequest httpRequest);
    //TODO: Considerar devolver un Page<ReviewResponse>
    Page<ReviewResponse> getAllReviews(Pageable pageable);
    Page<ReviewResponse> getReviewsByArticleId(Long articleId, Pageable pageable);
    Optional<ReviewResponse> getReviewById(Long id);
    void updateReviewComment(Long id, ReviewUpdateRequest request);
    void deleteReviewById(Long id);
    boolean existReviewById(Long id);
}
