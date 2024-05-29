package org.lumeninvestiga.backend.repositorio.tpi.entities.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(
        name = "reviews"
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Review {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(nullable = false)
    private boolean liked;
    @Column(nullable = false)
    private String comment;
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    @JsonBackReference
    private User user;

    public Review() {
        this.liked = false;
        this.comment = "";
    }

    public Long getId() {
        return id;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public void removeUser() {
//        if(user != null) {
//            user.getReviews().remove(this);
//        }
//        this.user = null;
//    }

}
