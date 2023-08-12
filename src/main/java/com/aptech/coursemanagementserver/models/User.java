package com.aptech.coursemanagementserver.models;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.aptech.coursemanagementserver.enums.AuthProvider;
import com.aptech.coursemanagementserver.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "Users")
// implement interface UserDetails make user become spring user
public class User implements UserDetails, OAuth2User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private long id;
    @Column(columnDefinition = "nvarchar(100)")
    private String first_name;
    @Column(columnDefinition = "nvarchar(100)")
    private String last_name;
    @Column(columnDefinition = "nvarchar(100)")
    private String name;
    @Column(unique = true, columnDefinition = "nvarchar(100)")
    private String email;
    @EqualsAndHashCode.Include
    private String password;

    @Column(columnDefinition = "bit")
    @Builder.Default
    private boolean isVerified = false;

    @Builder.Default
    @Column(columnDefinition = "tinyint")
    private int userStatus = 1;

    @Column(columnDefinition = "bit DEFAULT(0)")
    @Builder.Default
    private boolean isNotify = true;

    @CreationTimestamp
    @Builder.Default
    private Instant created_at = Instant.now();
    @UpdateTimestamp
    private Instant updated_at;

    private String imageUrl;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    // @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
    // "user")
    // @Builder.Default
    // private Set<Roles> roles = new HashSet<>();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private AuthProvider provider = AuthProvider.local;

    private String providerId;

    @Transient
    private Map<String, Object> attributes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "user")
    @Builder.Default
    Set<UserPermission> userPermissions = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "user")
    @Builder.Default
    Set<Enrollment> enrollments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @Builder.Default
    private Set<Orders> orders = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @Builder.Default
    private Set<Blog> blogs = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @Builder.Default
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userTo")
    @Builder.Default
    private Set<Notification> notificationsPublish = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userFrom")
    @Builder.Default
    private Set<Notification> notificationsReceive = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userPost")
    @Builder.Default
    private Set<Post> postsOfUser = new HashSet<>();

    @ManyToMany(mappedBy = "likedUsers", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Post> posts = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userPermissions.stream()
                .map(permission -> new SimpleGrantedAuthority("ROLE_" + permission.getPermission().getPermission()))
                .collect(Collectors.toList());
        // Spring will use the list of authorities of each user
    }

    @Override
    public String getUsername() { // login by email => username: email
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() { // Make sure account nonexpired
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

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {

        return this.first_name + " " + this.last_name;
    }

}
