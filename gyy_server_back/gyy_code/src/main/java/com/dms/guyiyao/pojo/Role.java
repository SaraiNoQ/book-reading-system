package com.dms.guyiyao.pojo;

import lombok.Data;

@Data
public class Role {
    private static final long serialVersionUID = -40356785423868312L;
    private  String name;
    public  Role( String name){
        this.name=name;
    }
}
