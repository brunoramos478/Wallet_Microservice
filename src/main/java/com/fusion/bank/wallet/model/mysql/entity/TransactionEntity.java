package com.fusion.bank.wallet.model.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "transacao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, updatable = false)
    private UUID id;

    @Column(name = "id_usuario")
    private UUID walletId;

    @Column(name = "tipo")
    private String type;

    @Column(name = "valor")
    private BigDecimal value;

}
