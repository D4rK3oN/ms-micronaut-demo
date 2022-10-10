package com.example.simple.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "simple_objects")
public class Simple {

    @Id
    private Integer id;

    @Column
    private String name;

    @Column
    private String nickname;

    @Column
    @Email
    private String email;

    @Column
    private Integer age;
}
