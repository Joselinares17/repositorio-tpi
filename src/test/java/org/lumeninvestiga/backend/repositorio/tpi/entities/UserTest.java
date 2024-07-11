package org.lumeninvestiga.backend.repositorio.tpi.entities;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.lumeninvestiga.backend.repositorio.tpi.entities.data.StorableItem;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.lumeninvestiga.backend.repositorio.tpi.entities.user.User;
import org.lumeninvestiga.backend.repositorio.tpi.entities.user.Role;
import org.lumeninvestiga.backend.repositorio.tpi.entities.user.Review;
import org.lumeninvestiga.backend.repositorio.tpi.entities.user.UserDetail;

public class UserTest {
    private User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals("", user.getUsername());
        assertEquals("", user.getPassword());
        assertEquals(Role.USER, user.getRole());
        assertNotNull(user.getUserDetail());
        assertTrue(user.getReviews().isEmpty());
        assertTrue(user.getStorableItems().isEmpty());
    }

    @Test
    public void testSetAndGetId() {
        Long id = 1L;
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    public void testSetAndGetUsername() {
        String username = "testUser";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testSetAndGetPassword() {
        String password = "testPassword";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testSetAndGetRole() {
        Role role = Role.ADMIN;
        user.setRole(role);
        assertEquals(role, user.getRole());
    }

    @Test
    public void testSetAndGetUserDetail() {
        UserDetail userDetail = new UserDetail();
        user.setUserDetail(userDetail);
        assertEquals(userDetail, user.getUserDetail());
    }

    @Test
    public void testSetAndGetReviews() {
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review());
        user.setReviews(reviews);
        assertEquals(reviews, user.getReviews());
    }

    @Test
    public void testSetAndGetStorableItems() {
        List<StorableItem> items = new ArrayList<>();
        items.add(new StorableItem());
        user.setStorableItems(items);
        assertEquals(items, user.getStorableItems());
    }

    @Test
    public void testGetAuthorities() {
        user.setRole(Role.ADMIN);
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ADMIN")));
    }

    @Test
    public void testAccountStatuses() {
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    public void testAddAndRemoveReview() {
        Review review = new Review();
        user.addReview(review);
        assertTrue(user.getReviews().contains(review));
        assertEquals(user, review.getUser());

        user.removeReview(review);
        assertFalse(user.getReviews().contains(review));
        assertNull(review.getUser());
    }

    @Test
    public void testAddAndRemoveStorableItem() {
        StorableItem item = new StorableItem();
        user.addStorableItem(item);
        assertTrue(user.getStorableItems().contains(item));
        assertEquals(user, item.getUser());

        user.removeStorableItem(item);
        assertFalse(user.getStorableItems().contains(item));
        assertNull(item.getUser());
    }

    @Test
    public void testEquals() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(1L);
        User user3 = new User();
        user3.setId(2L);

        assertTrue(user1.equals(user2));
        assertFalse(user1.equals(user3));
    }

    @Test
    public void testHashCode() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(1L);

        assertEquals(user1.hashCode(), user2.hashCode());
    }
}