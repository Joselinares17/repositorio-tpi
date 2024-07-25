package org.lumeninvestiga.backend.repositorio.tpi.entities.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.lumeninvestiga.backend.repositorio.tpi.entities.user.Review;
import org.lumeninvestiga.backend.repositorio.tpi.entities.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "articles"
)
public class Article {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Long size;
    @Column(name="created_date",  nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;
    @Column(updatable = false, columnDefinition = "LONGBLOB")
    private byte[] data;
    @Column(name = "mime_type")
    private String mimeType;
    @Column(name = "like_count", nullable = false)
    private Integer likeCount;

    @ManyToOne(targetEntity = User.class)
    @JsonBackReference
    private User user;

    @OneToOne(
            targetEntity = ArticleDetail.class,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.REMOVE,
                    CascadeType.REFRESH
            }
    )
    private ArticleDetail articleDetail;

    @OneToMany(
            targetEntity = Review.class,
            cascade = {
                    CascadeType.REFRESH,
                    CascadeType.REMOVE
            },
            fetch = FetchType.LAZY,
            mappedBy = "article"
    )
    private List<Review> reviews;

    public Article() {
        this.name = "";
        this.size = 0L;
        this.createdDate = LocalDateTime.now();
        this.likeCount = 0;
        this.articleDetail = new ArticleDetail();
        this.reviews = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public User getUser() {
        return user;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArticleDetail getArticleDetail() {
        return articleDetail;
    }

    public void setArticleDetail(ArticleDetail articleDetail) {
        this.articleDetail = articleDetail;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    //TODO: Mover estos métodos a un utils de la clase.
    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        if(this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
