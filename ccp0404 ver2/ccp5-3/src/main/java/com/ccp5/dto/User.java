package com.ccp5.dto;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor; // 추가
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="tbl_user4")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor 
public class User  {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false, unique = true)
	    private String username;

	    private String name;

	   
	    private String password;

	    private String email;

	    @Enumerated(EnumType.STRING)
	    private Role role;

	    @OneToMany(mappedBy = "writer")
	    @JsonIgnore
	    private Collection<Board> boards;

	    


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
