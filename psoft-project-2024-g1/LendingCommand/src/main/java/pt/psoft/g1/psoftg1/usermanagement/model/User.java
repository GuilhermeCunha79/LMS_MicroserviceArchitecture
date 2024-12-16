/*
 * Copyright (c) 2022-2024 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package pt.psoft.g1.psoftg1.usermanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.psoft.g1.psoftg1.shared.model.Generator;
import pt.psoft.g1.psoftg1.shared.model.Name;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Entity
@Table(name = "T_USER")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	// database primary key
	@Id
	@Getter
	@Column(name="USER_ID")
	private Long id;

	// optimistic lock concurrency control
	@Version
	@Getter
	private long version =0L;

	// auditing info
	@CreatedDate
	@Column(nullable = false, updatable = false)
	@Getter
	private LocalDateTime createdAt;

	// auditing info
	@LastModifiedDate
	@Column(nullable = false)
	@Getter
	private LocalDateTime modifiedAt;

	// auditing info
	@CreatedBy
	@Column(nullable = false, updatable = false)
	@Getter
	private String createdBy;

	// auditing info
	@LastModifiedBy
	@Column(nullable = false)
	private String modifiedBy;

	@Setter
	@Getter
	private boolean enabled = true;

	@Setter
    @Column(unique = true, /*updatable = false,*/ nullable = false)
	@Email
	@Getter
	@NotNull
	@NotBlank
	private String username;

	@Column(nullable = false)
	@Getter
	@NotNull
	@NotBlank
	private String password;

	@Getter
//	@Setter
	@Embedded
	private Name name;

	@ElementCollection
	@Getter
	@Setter
	private Set<Role> authorities = new HashSet<>();

	protected User() {
		// for ORM only
	}

	/**
	 *
	 * @param username
	 */
	public User(final String username) {
		this.id=Generator.generateLongID();
		this.username = username;
	}

    public void addAuthority(final Role r) {
		authorities.add(r);
	}

	@Override
	public boolean isAccountNonExpired() {
		return isEnabled();
	}

	@Override
	public boolean isAccountNonLocked() {
		return isEnabled();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isEnabled();
	}

	public void setName(String name){
		this.name = new Name(name);
	}
}
