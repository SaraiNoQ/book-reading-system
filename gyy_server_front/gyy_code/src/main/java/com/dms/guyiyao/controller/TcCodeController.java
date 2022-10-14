package com.dms.guyiyao.controller;


import com.dms.guyiyao.service.CodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 基础码表 前端控制器
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Api(tags="码表")
@RestController
public class TcCodeController {
@Autowired
    private CodeService codeService;
@ApiOperation("获取内容码表")
@PostMapping("/code/getContentType")
    public List<String> getContentType(){
        return    codeService.getContentType();
}
}
