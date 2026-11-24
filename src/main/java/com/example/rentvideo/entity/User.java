package com.example.rentvideo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
@Table(name = "users")
public class User {

 @Id 
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
@Column(unique = true, nullable = false, length = 255)
 private String email;
 @NotBlank 
 @Column(nullable = false)
 private String password;
 @NotBlank 
 @Column(nullable = false)
 private String firstName;
@NotBlank 
@Column(nullable = false)
 private String lastName;
 @Column(nullable = false)
  @NotNull 
  @Enumerated(EnumType.STRING)
 private Role role;

}
