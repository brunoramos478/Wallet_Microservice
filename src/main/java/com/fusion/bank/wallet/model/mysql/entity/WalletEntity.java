package com.fusion.bank.wallet.model.mysql.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Persistent;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "carteira")
public class WalletEntity implements Serializable {

    @Id
    @Column(name = "id", unique = true,updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "saldo")
    private BigDecimal balance;

    @Persistent
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime createdIn = LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "data_atualizacao", updatable = true)
    private LocalDateTime updateIn;

    @Column(name = "id_usuario")
    private UUID userId;
}