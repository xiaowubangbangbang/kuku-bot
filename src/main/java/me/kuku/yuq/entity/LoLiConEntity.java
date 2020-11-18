package me.kuku.yuq.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "LOLICONPICTURE")
@Data
@Builder
public class LoLiConEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;
    @Column
    private String pid;
    @Column
    private String url;
    @Column
    private String uid;
    @Column
    private String type;
}


