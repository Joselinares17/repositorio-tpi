package org.lumeninvestiga.backend.repositorio.tpi.entities.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.lumeninvestiga.backend.repositorio.tpi.entities.data.StorableItem;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Inheritance(
        strategy = InheritanceType.JOINED
)
@Table(
        name = "users"
)
public class User implements UserDetails {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_seq"
    )
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false)
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
            targetEntity = StorableItem.class,
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY,
            mappedBy = "user"
    )
    @JsonManagedReference
    private List<StorableItem> storableItems;

    public User() {
        this.username = "";
        this.password = "";
        this.role = Role.USER;
        this.userDetail = new UserDetail();
        this.reviews = new ArrayList<>();
        this.storableItems = new ArrayList<>();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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

    public List<StorableItem> getStorableItems() {
        return storableItems;
    }

    public void setStorableItems(List<StorableItem> storableItems) {
        this.storableItems = storableItems;
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

    public void addStorableItem(StorableItem storableItem) {
        this.storableItems.add(storableItem);
        storableItem.setUser(this);
    }

    public void removeStorableItem(StorableItem storableItem) {
        this.storableItems.remove(storableItem);
        storableItem.setUser(null);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(id, user.id) && Objects.equals(userDetail, user.userDetail) && Objects.equals(reviews, user.reviews) && Objects.equals(storableItems, user.storableItems) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userDetail, reviews, storableItems, role);
    }
}
