package com.fusion.bank.wallet.model.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @Column(name = "id_transacao", unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "id_carteira")
    private UUID walletId;

    @Column(name = "id_usuario")
    private UUID userId;

    @Column(name = "tipo_transacao")
    private String type;

    @Column(name = "valor")
    private BigDecimal value;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String description;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime createdIn;

    @PrePersist
    public void dateTimeCreated() {
        this.createdIn = LocalDateTime.now();
    }

}
