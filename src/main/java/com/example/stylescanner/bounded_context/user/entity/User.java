package com.example.stylescanner.bounded_context.user.entity;

import com.example.stylescanner.base.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @Column(nullable = false)
    private Integer user_id;




}
