package com.fusion.bank.wallet.model.mysql.entity;

import com.fusion.bank.wallet.shared.enums.CardFlagType;
import com.fusion.bank.wallet.shared.enums.CategoryCard;
import com.fusion.bank.wallet.shared.enums.ModalityCard;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cartao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private UUID id;

    @Column(name = "numero_cartao", nullable = false, unique = true, updatable = false)
    @Size(min = 16, max = 20)
    private String cardNumber;

    @Column(name = "nome_titular", nullable = false)
    @Size(min = 3, max = 25)
    private String titularName;

    @Column(name = "limite_cartao", nullable = false)
    private BigDecimal limitCard;

    @Column(name = "codigo_seguranca", nullable = false, updatable = false)
    @Size(min = 3, max = 3)
    private String cvc;

    @Column(name = "data_validade", nullable = false, updatable = false)
    @Size(min = 5, max = 5, message = "Formato deve ser MM/AA")
    private String expirationDate;

    @Column(name = "bandeira", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private CardFlagType flag;

    @Column(name = "categoria", nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryCard categoryCard;

    @Column(name = "modalidade", nullable = false)
    @Enumerated(EnumType.STRING)
    private ModalityCard modalityCard;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime createdIn;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime updatedIn;

    @PrePersist
    public void prePersist() {
        this.createdIn = LocalDateTime.now();
        this.updatedIn = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedIn = LocalDateTime.now();
    }

}
