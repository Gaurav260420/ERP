package com.dynamicbutton.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "app_applicationmenus")
public class ApplicationMenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String label;
    private Long applicationid;
    private Long mainmenuid;
    private String path;
    private String actionurl;

    //        id label applicationid mainmenuid path actionurl applicationmenuid


}
