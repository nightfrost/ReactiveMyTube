package io.nightfrost.reactivemytube.models;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;


import java.util.ArrayList;

@Data
@Builder
public class Metadata {

    @NotBlank
    private String name;

    @NotEmpty
    private ArrayList<Tags> tags;

    @NotBlank
    @URL
    private String posterUrl;

    @Nullable
    private Integer season;

    @Nullable
    private Integer episode;
}
