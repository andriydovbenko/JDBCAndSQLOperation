package com.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Model{
    private @NonNull int ID;
    private @NonNull String USER_NAME;
    private String FIRST_NAME;
    private String LAST_NAME;
    private String CITY;
    private String GENDER;
}