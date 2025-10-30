package com.example.musicserver.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.musicserver.pojo.Lists;
import com.example.musicserver.pojo.Page;
import com.example.musicserver.pojo.Song;
import com.example.musicserver.pojo.User;
import com.example.musicserver.service.impl.ListsServiceImpl;
import com.example.musicserver.service.impl.SendSmsImpl;
import com.example.musicserver.service.impl.SongServiceImpl;
import com.example.musicserver.service.impl.UserServiceImpl;
import com.example.musicserver.utils.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    ListsServiceImpl listsService;

    @Autowired
    SongServiceImpl songService;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Autowired
    SendSmsImpl sendSms;

    @GetMapping("/userById")
    public Result userById(@RequestParam("userId") int userId){
        User user= userService.userById(userId);
        return Result.success(user);
    }

    @GetMapping("/userByAccount")
    public Result userByAccount(@RequestParam("account") String account){
        int count= userService.userByAccount(account);
        if (count > 0){
            return Result.error("107","此账号已存在！");
        }else{
            return Result.success();
        }

    }

    @GetMapping("/userByPhone")
    public Result userByPhone(@RequestParam("userPhone") String userPhone){
        int count= userService.userByPhone(userPhone);
        if (count > 0){
            return Result.error("107","此手机号已被绑定！");
        }else{
            return Result.success();
        }

    }

    @GetMapping("/userByName")
    public Result userByName(@RequestParam("userName") String userName){
        List<User> list= userService.queryUserByName(userName);
        return Result.success(list);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        String resultCode=redisTemplate.opsForValue().get(user.getUserPhone());
        userService.register(user);
        Lists lists=new Lists();
        lists.setListTitle("我喜欢的音乐");
        lists.setListUserId(user.getUserId());
        lists.setListImg("http://101.200.130.226:8081/file/image/myLikeListeadeb3.jpeg");
        lists.setListType(7);
        listsService.addLists(lists);
        return Result.success();
    }

    @PostMapping("/sendSMS/{phone}")
    public Result<?> sendSMS(@PathVariable String phone) {

        String resultCode=redisTemplate.opsForValue().get(phone);
        if(resultCode!=null){
            return Result.error("105","验证码还未过期，请勿重复发送");
        }else{
            String code= (int) ((Math.random()*9+1)*100000)+"";
            boolean flag=sendSms.sendSMS(phone,code);
            if(flag) {
                redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
                return Result.success();
            }else{
                return Result.error("106","发送失败");
            }
        }
    }
    @PostMapping("/validSMS")
    public Result<?> validSMS(@RequestParam ("userPhone") String userPhone,@RequestParam("code") String code) {

        String resultCode=redisTemplate.opsForValue().get(userPhone);
        if(code.equals(resultCode)) {
            return Result.success();
        }else{
            return Result.error("106","验证码错误或已失效！");
        }
    }

    @GetMapping("/login")
    public Result login(@RequestParam("account") String account,
                        @RequestParam("password") String password,
                        HttpServletResponse response,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        if("".equals(account) && "".equals(password)){
            Cookie[] cookies = request.getCookies();
            System.out.println("使用cookie自动登录");
            String cookieAccount="";
            String cookiePassword="";
            if(cookies !=null && cookies.length>0){
                for (Cookie cookie:cookies) {
                    System.out.println("遍历cookie："+ cookie.getName());
                    if ("account".equals(cookie.getName())) {
                        cookieAccount=cookie.getValue();
                    }else if("password".equals(cookie.getName())){
                        cookiePassword=cookie.getValue();
                    }
                }
                User user=userService.login(cookieAccount,cookiePassword);
                List<Song> songList = songService.querySongOfUser(user.getUserId());
                map.put("user",user);
                map.put("list",songList);
                System.out.println("自动登录成功");
                return Result.success("200","Cookie自动登录成功",map);
            }else{
                return Result.error("105","cookie已经过期，自动登录失败！");
            }
        }else{
            User user=userService.login(account,password);
            if(user!=null){
                List<Song> songList = songService.querySongOfUser(user.getUserId());
                map.put("user",user);
                map.put("list",songList);

                Cookie cookie1=new Cookie("account",account);
                cookie1.setMaxAge(60*60*24);
                response.addCookie(cookie1);
                Cookie cookie2=new Cookie("password",password);
                cookie2.setMaxAge(60*60*24);
                response.addCookie(cookie2);
                return Result.success(map);
            }else{
                return Result.error("104","用户名或密码错误");
            }
        }
    }

    @GetMapping("/loginPhone")
    public Result loginPhone(@RequestParam("userPhone") String userPhone,
                        HttpServletResponse response,HttpServletRequest request){
        if("".equals(userPhone)){
            Cookie[] cookies = request.getCookies();
            System.out.println("使用cookie自动登录");
            String phone="";
            if(cookies !=null && cookies.length>0){
                for (Cookie cookie:cookies) {
                    System.out.println("遍历cookie："+ cookie.getName());
                    if ("phone".equals(cookie.getName())) {
                        phone=cookie.getValue();
                    }
                }
                User user=userService.loginPhone(phone);
                System.out.println("自动登录成功");
                return Result.success("200","Cookie自动登录成功",user);
            }else{
                return Result.error("105","cookie已经过期，自动登录失败！");
            }
        }else{
            User user=userService.loginPhone(userPhone);
            if(user!=null){
                Cookie cookie=new Cookie("userPhone",userPhone);
                cookie.setMaxAge(60*60*24);
                response.addCookie(cookie);
                return Result.success(user);
            }else{
                return Result.error("104","用户名或密码错误");
            }
        }
    }

    @GetMapping("/logout")
    public Result logout(@RequestParam("account") String account,
                       @RequestParam("password") String password,
                       HttpServletResponse response){
        Cookie cookie1=new Cookie("account",account);
        cookie1.setMaxAge(0);
        response.addCookie(cookie1);
        Cookie cookie2=new Cookie("password",password);
        cookie2.setMaxAge(0);
        response.addCookie(cookie2);
        return Result.success();
    }

    @PutMapping("/putUser")
    public Result putUser(@RequestBody User user){
        System.out.println(user);
        userService.putUser(user);
        return Result.success();
    }
}
