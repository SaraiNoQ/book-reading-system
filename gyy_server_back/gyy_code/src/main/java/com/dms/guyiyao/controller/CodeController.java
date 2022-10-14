package com.dms.guyiyao.controller;

import com.dms.guyiyao.mapper.TsBookMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Api(tags = "码表接口")
@RestController
public class CodeController {
@Autowired
    TsBookMapper tsBookMapper;
@PostMapping("/code/Books")
public List<String>getBooks(){
     return  tsBookMapper.getBookName();
}
}
