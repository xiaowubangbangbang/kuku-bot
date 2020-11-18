package me.kuku.yuq.controller.ark;

import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "userRecord", uniqueConstraints = {@UniqueConstraint(columnNames = {"qq", "pool"})})
@Data
public class UserRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Long qq = 0L;
    @Column
    private String pool = "";
    @Column
    private Integer fourFloor = 0;
    @Column
    private Integer fiveFloor = 0;
    @Column
    private Integer sixFloor = 0;
    @Column
    private Integer beforeTenCount = 1;
    @Column
    private Boolean upFive = false;
    @Column
    private Boolean upFour = false;
    @Column
    private Boolean beforeTen = true;
}
