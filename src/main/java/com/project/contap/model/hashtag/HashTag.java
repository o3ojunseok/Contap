package com.project.contap.model.hashtag;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Setter
@Getter
public class HashTag {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private int type; //0:stack, 1:interest

    public HashTag(String name, int type)
    {
        this.name = name;
        this.type = type;
    }
    public HashTag(Long id, String name, int type) // test용
    {
        this.id = id;
        this.name = name;
        this.type = type;
    }

}
