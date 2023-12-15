package io.nightfrost.reactivemytube.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class MovieDTO {

    private String _id;

    private String filename;

    private Date uploadDate;
}
