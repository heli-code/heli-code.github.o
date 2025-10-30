package com.example.musicserver.controller;

import com.example.musicserver.pojo.Page;
import com.example.musicserver.pojo.Singer;
import com.example.musicserver.pojo.Song;
import com.example.musicserver.service.impl.SingerServiceImpl;
import com.example.musicserver.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/singer")
public class SingerController {

    @Autowired
    SingerServiceImpl singerService;

    @GetMapping("/allSinger")
    public Result querySingers(@RequestParam("pageNum")int pageNum,@RequestParam("pageSize") int pageSize){
        List<Singer> singerList=singerService.queryAllSinger((pageNum-1)*pageSize,pageSize);
        int count=singerService.countOfSinger();
        Map<String,Object> map = new HashMap<>();
        map.put("list",singerList);
        map.put("totalSize",count);
        return Result.success(map);
    }

    @GetMapping("/topSinger")
    public Result topSinger(){
        List<Singer> singerList=singerService.topSinger();
        return Result.success(singerList);
    }

    @GetMapping("/singerByType")
    public Result singerByType(@RequestParam("pageNum")int pageNum,
                               @RequestParam("pageSize") int pageSize,
                               @RequestParam("singerType") int singerType){
        List<Singer> singerList=singerService.querySingerByType((pageNum-1)*pageSize,pageSize,singerType);
        int count=singerService.countOfSingerByType(singerType);
        Map<String,Object> map = new HashMap<>();
        map.put("list",singerList);
        map.put("totalSize",count);
        return Result.success(map);
    }

    @GetMapping("/singerById")
    public Result singerById(@RequestParam("singerId") int singerId){
        Singer singer = singerService.querySingerById(singerId);
        return Result.success(singer);

    }

    @GetMapping("/singerByName")
    public Result singerByName(@RequestParam("singerName") String singerName){
        List<Singer> list= singerService.querySingerByName(singerName);
        return Result.success(list);
    }

    @PostMapping("/addSinger")
    public Result addSinger(@RequestBody Singer singer){
        singerService.addSinger(singer);
        return Result.success();
    }

    @PutMapping("/putSinger")
    public Result putSinger(@RequestBody Singer singer){
        singerService.putSinger(singer);
        return Result.success();
    }

    @DeleteMapping("/delSinger/{id}")
    public Result delSinger(@PathVariable int id){
        singerService.delSinger(id);
        return Result.success();
    }
}
