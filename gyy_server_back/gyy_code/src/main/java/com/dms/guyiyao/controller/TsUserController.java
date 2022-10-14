package com.dms.guyiyao.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dms.guyiyao.DemoApplication;
import com.dms.guyiyao.mapper.TsUserMapper;
import com.dms.guyiyao.pojo.Regist_Info;
import com.dms.guyiyao.pojo.entity.User;
import com.dms.guyiyao.pojo.page.PageV0;
import com.dms.guyiyao.pojo.user.TsUser;
import com.dms.guyiyao.security.userdetail.UserdetailImpl;
import com.dms.guyiyao.security.userdetail.UserdetailServiceImpl;
import com.dms.guyiyao.service.ITsUserService;
import com.dms.guyiyao.utils.JsonUtil;
import com.dms.guyiyao.utils.Loggers;
import com.dms.guyiyao.utils.RedisCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.sql.Wrapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author dms
 * @since 2022-01-18
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class TsUserController {
    @Autowired
    private ITsUserService userService;
    @Autowired
    private TsUserMapper userMapper;

    @ApiOperation("分页获取用户列表")
    @GetMapping("/getUser/{page}/{size}")
    public PageV0 list(@PathVariable("page") Integer page, @PathVariable("size") Integer size){
        return userService.getUserList(page, size);
    }
    @ApiOperation("删除用户")
    @PostMapping ("/deleteUser")
    public void deleteUser(HttpServletResponse resp, @RequestParam("id")String id){
        TsUser targetUser = userMapper.selectById(id);
        UsernamePasswordAuthenticationToken operator= (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserdetailImpl details = (UserdetailImpl) operator.getPrincipal();
        if (targetUser.getUsertype().equals("2")||(details.getRole().get(0).equals("1")&&targetUser.getUsertype().equals("1"))){
            Map map=new HashMap();
            map.put("status","无删除权限(请使用更高权限账户完成操作)");
            JSONObject json=JsonUtil.toJsonObject(map);
            JsonUtil.returnJsonClient(resp,json);
            return;
        }
        System.out.println("hello");
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserdetailImpl principal = (UserdetailImpl) authentication.getPrincipal();
        if (principal.getUser().getId().equals(id)) {
            Map map=new HashMap();
            map.put("status","无法删除自己");
            JSONObject json=JsonUtil.toJsonObject(map);
            JsonUtil.returnJsonClient(resp,json);
            return;
        }
        if (userService.deleteUser(id)){
            Map map=new HashMap();
            map.put("status","success");
            JSONObject json=JsonUtil.toJsonObject(map);
            JsonUtil.returnJsonClient(resp,json);
            return;
        }else {
            Map map =new HashMap();
            map.put("status","false");
            JSONObject json=JsonUtil.toJsonObject(map);
            JsonUtil.returnJsonClient(resp,json);
            return;
        }

    }
    @ApiOperation("查询用户")
    @PostMapping("/searchForUsers/{page}/{size}")
    public  PageV0 searchForUsers(@PathVariable("page")Integer page, @PathVariable("size")Integer size
            ,  @RequestParam(value = "username",required = false)String username,@RequestParam(value = "nickname",required = false)String nickname
            , @RequestParam(value = "email",required = false)String email){
        return userService.searchForUser(page,size,username,nickname,email);
    }
@ApiOperation("重置密码")
    @PostMapping("/resetUserpassword")
    public  void reSetPassword(HttpServletResponse resp,@RequestParam("username")String username) {
    QueryWrapper<TsUser> queryWrapper=new QueryWrapper();
    QueryWrapper<TsUser> eq = queryWrapper.eq("username", username);
    TsUser targetUser = userMapper.selectOne(queryWrapper);
    UsernamePasswordAuthenticationToken operator= (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    UserdetailImpl details = (UserdetailImpl) operator.getPrincipal();
    if (targetUser.getUsertype().equals("2")||(details.getRole().get(0).equals("1")&&targetUser.getUsertype().equals("1"))){
        Map map=new HashMap();
        map.put("status","无删除权限(请使用更高权限账户完成操作)");
        JSONObject json=JsonUtil.toJsonObject(map);
        JsonUtil.returnJsonClient(resp,json);
        return;
    }
        if (userService.resetUserPassword(username)){
            Map map=new HashMap();
            map.put("status","success");
            JSONObject json=JsonUtil.toJsonObject(map);
            JsonUtil.returnJsonClient(resp,json);
            return;
        }else {
            Map map =new HashMap();
            map.put("status","false");
            JSONObject json=JsonUtil.toJsonObject(map);
            JsonUtil.returnJsonClient(resp,json);
            return;
//            $10$Sye3OVsTGVGMIsT0BeslZeT.Tf1k0XHby6qI1o1Z63oJZAUbi0kuO
        }

    }
    @PreAuthorize("hasAnyAuthority('2')")
    @ApiOperation("添加管理员")
    @GetMapping ("/addAdmin")
    public String addAdminUser(@RequestParam("targetId")String targetId,@RequestParam("userType") String userType) {
        TsUser targetUser = userMapper.selectById(targetId);
        Authentication operator =SecurityContextHolder.getContext().getAuthentication();
        if (!userType.equals("0")&&!userType.equals("1"))return "无法做出异常权限修改";
        if (targetUser.getUsertype().equals("2"))return "当前管理员无法修改同级管理员权限";
        Loggers.info(operator.getName()+"正在修改"+targetUser.getUsername()+"权限为"+userType);
        UpdateWrapper<TsUser> wrapper=new UpdateWrapper<>();
        wrapper.eq("id",targetId);
        wrapper.set("usertype",userType);
        userMapper.update(new TsUser(),wrapper);
        Loggers.info("权限修改成功");
        return "success";
    }
    public static void main(String[] args) {
//        ConfigurableApplicationContext run = SpringApplication.run(DemoApplication.class, args);
//        System.out.println("容器加载成功");

//        try {
//            Socket socket=new Socket();
//            socket.connect(new InetSocketAddress("127.0.0.1",8888));
//            PrintWriter printWriter=new PrintWriter(socket.getOutputStream(),true);
//
//
//            String req="GET /user/addAdmin\r\n"+
//                    "Accept: */*\r\n" +
//                    "Accept-Language: zh,en-GB;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6\r\n" +
//                    "Connection: keep-alive\r\n" +
//                    "Host: 127.0.0.1:8888\r\n" +
//                    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36\r\n" +
//                    "token: eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzNDQ3NTk5MGY5OGU0MGU1OWY5NDlkOWE4OGViNjFiZiIsInN1YiI6IjA0MzkyMWU4ZjQ2ZjExZWM4Yzk2MDA1MDU2OGM0OWUwIiwiaXNzIjoic2ciLCJpYXQiOjE2NjIyMTUzNTAsImV4cCI6MTY2MjI1MTM1MH0.s-OlLMD20d2ko93KHo3PaRcd71tojiObI92t1SkH9FY\r\n"+
//                    "Accept-Encoding: gzip, deflate, br\r\n";
//            System.out.println(req);
//            printWriter.print(req);
//            printWriter.flush();
//            InputStream inputStream = socket.getInputStream();
//            Scanner scanner=new Scanner(inputStream);
//            System.out.println("开始等待IO");
//            while (scanner.hasNextLine()){
//                System.out.println(scanner.nextLine());
//            }
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        TsUserController tsUserController = run.getBean("tsUserController", TsUserController.class);
////        System.out.println(tsUserController.list(1, 10));
//        System.out.println(tsUserController.addAdminUser("0afe74cd7acc11ecbfc500163e30620d", "1"));

    }
    @GetMapping("/loginUserInfo")
    public UserdetailImpl LoginUserInfo(){
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserdetailImpl principal = (UserdetailImpl) authentication.getPrincipal();
        principal.getUser().setPassword(null);
        principal.getPassword();
        return principal;
    }
    @PostMapping("/addCommonUser")
    public void addCommonUser(HttpServletRequest req, HttpServletResponse resp, Regist_Info regist_info) throws Exception {
        ModelAndView modelAndView =new ModelAndView();
        String userId = UUID.randomUUID().toString().replace("-", "");
        Loggers.info("用户id生成成功");
        try{
            modelAndView = userService.commonUserAdd(req, resp, regist_info,userId);
            modelAndView.addObject("userId",userId);
        }catch (Exception e){
            modelAndView.addObject("status",e.getMessage());
        }

        HashMap map = modelAndView.getModelMap();
//        if(map.get("status").equals("success")){
//            authenticateUserAndSetSession(regist_info.getUsername(),regist_info.getPassword(),req);
//        }
        JSONObject json = JsonUtil.toJsonObject(modelAndView.getModelMap());
        JsonUtil.returnJsonClient(resp, json);
    }


    }


