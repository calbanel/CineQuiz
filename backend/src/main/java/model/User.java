package model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection="User")
public class User{
    @Id
    private int id;
    private String pseudo;
    private String email;
    private String password;
}