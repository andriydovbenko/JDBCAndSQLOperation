package com.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString

public class Profile implements Model {
    private @NonNull int ID;
    private @NonNull String USER_NAME;
    private String JOB_TITLE;
    private String DEPARTMENT;
    private String COMPANY;
    private String SKILL;
}