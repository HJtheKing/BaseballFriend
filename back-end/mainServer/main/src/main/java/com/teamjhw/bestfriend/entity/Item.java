package com.teamjhw.bestfriend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private ItemCategory itemCategory;

    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_info_id",nullable = false)
    private CharacterInfo characterInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id",nullable = false)
    private Team team;

    @Column(name = "item_serial_number", unique = true)
    private Long itemSerialNumber;

}
