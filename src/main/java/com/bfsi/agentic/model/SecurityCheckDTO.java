package com.bfsi.agentic.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SecurityCheckDTO {
    @NotEmpty
    private String q1;
    @NotEmpty
    private String a1;
    @NotEmpty
    private String q2;
    @NotEmpty
    private String a2;
    @NotEmpty
    private String q3;
    @NotEmpty
    private String a3;
}
