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
@Table(name = "character_info")
public class CharacterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_info_id")
    private Long id;

    private String characterName;

    private Long price;

    @Column(name = "character_info_serial_number", unique = true)
    private Long characterInfoSerialNumber;
}
