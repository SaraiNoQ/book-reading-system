package com.dms.guyiyao.pojo.original;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ImgForm {
    private String id;
    private String chapter;
    private  String[] file;
}
