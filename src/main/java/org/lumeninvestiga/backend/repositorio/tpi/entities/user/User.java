package org.lumeninvestiga.backend.repositorio.tpi.entities.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.lumeninvestiga.backend.repositorio.tpi.entities.data.Article;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "users"
)
public class User implements UserDetails {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = true)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(
            targetEntity = UserDetail.class,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.REMOVE
            })
    @JsonManagedReference
    private UserDetail userDetail;

    @OneToMany(
            targetEntity = Review.class,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.REMOVE
            },
            fetch = FetchType.LAZY,
            mappedBy = "user"
    )
    @JsonManagedReference
    private List<Review> reviews;

    @OneToMany(
            targetEntity = Article.class,
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY,
            mappedBy = "user"
    )
    @JsonManagedReference
    private List<Article> articles;

    public User() {
        this.username = "";
        this.password = "";
        this.role = Role.STUDENT;
        this.userDetail = new UserDetail();
        this.reviews = new ArrayList<>();
        this.articles = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
        review.setUser(this);
    }

    public void removeReview(Review review) {
        this.reviews.remove(review);
        review.setUser(null);
    }

    public void addArticle(Article article) {
        this.articles.add(article);
        article.setUser(this);
    }

    public void removeArticle(Article article) {
        this.articles.remove(article);
        article.setUser(null);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && role == user.role && Objects.equals(userDetail, user.userDetail) && Objects.equals(reviews, user.reviews) && Objects.equals(articles, user.articles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, role, userDetail, reviews, articles);
    }
}
