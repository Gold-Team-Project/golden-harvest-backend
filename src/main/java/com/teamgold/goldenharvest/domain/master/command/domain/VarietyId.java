package com.teamgold.goldenharvest.domain.master.command.domain;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VarietyId implements Serializable {
    private Long produceMaster;
    private String varietyCode;
}