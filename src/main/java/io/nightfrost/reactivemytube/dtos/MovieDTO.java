package io.nightfrost.reactivemytube.dtos;


import io.nightfrost.reactivemytube.models.Tags;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@AllArgsConstructor
@Data
@Builder
public class MovieDTO {

    private String _id;

    private String filename;

    private Date uploadDate;

    private ArrayList<Tags> tags;

    private String posterUrl;
}
