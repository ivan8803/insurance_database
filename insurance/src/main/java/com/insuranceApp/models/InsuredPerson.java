package com.insuranceApp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "insured_person")
public class InsuredPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insured_person_sequence")
    @SequenceGenerator(name = "insured_person_sequence", sequenceName = "insured_person_sequence", allocationSize = 1)
    private Long id;
    private String firstname;
    private String lastname;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
    @Column(unique = true)
    private String email;
    private String phone;
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private UserEntity createdBy;
    @OneToMany(mappedBy = "insuredPerson", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Insurance> insurances;
}
