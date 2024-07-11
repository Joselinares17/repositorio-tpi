package org.lumeninvestiga.backend.repositorio.tpi.repositories;

import org.lumeninvestiga.backend.repositorio.tpi.entities.user.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.article.id = :id")
    Page<Review> findByArticleId(Long id, Pageable pageable);
}
