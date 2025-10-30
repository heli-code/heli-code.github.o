package com.example.musicserver.controller;

import com.example.musicserver.pojo.Lists;
import com.example.musicserver.pojo.Page;
import com.example.musicserver.pojo.Song;
import com.example.musicserver.service.impl.ListsServiceImpl;
import com.example.musicserver.utils.Result;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lists")
public class ListsController {

    @Autowired
    ListsServiceImpl listsService;
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @GetMapping("/allLists")
    public Result allLists(@RequestParam("pageNum")int pageNum, @RequestParam("pageSize") int pageSize){
        List<Lists> list=listsService.queryLists((pageNum-1)*pageSize,pageSize);
        int count=listsService.countOfList();
        Map<String,Object> map =new HashMap<>();
        map.put("list",list);
        map.put("totalSize",count);
        return Result.success(map);
    }

    @GetMapping("/listByType")
    public Result listByType(@RequestParam("pageNum")int pageNum,
                             @RequestParam("pageSize") int pageSize,
                             @RequestParam("listType")int listType){
        List<Lists> list=listsService.queryListsByType((pageNum-1)*pageSize,pageSize,listType);
        int count=listsService.countOfListByType(listType);
        Map<String,Object> map =new HashMap<>();
        map.put("list",list);
        map.put("totalSize",count);
        return Result.success(map);
    }

    @GetMapping("/listByName")
    public Result listByName(@RequestParam("listName") String listName){
        List<Lists> list= listsService.queryListByName(listName);
        return Result.success(list);
    }

    @GetMapping("/topLists")
    public Result topLists(HttpServletResponse response){
        List<Lists> list=listsService.topLists();
        return Result.success(list);

    }

    @GetMapping("/listByUserId")
    public Result listByUserId(int userId){
        List<Lists> list=listsService.queryListsByUserId(userId);
        return Result.success(list);
    }

    @GetMapping("/colListByUserId")
    public Result colListByUserId(int userId){
        List<Lists> list=listsService.colListsByUserId(userId);
        return Result.success(list);
    }

    @GetMapping("/isCol")
    public Result isCol(@RequestParam("listId") int listId,@RequestParam("userId") int userId){
        int isCol=listsService.isCol(listId, userId);
        return Result.success(isCol);
    }

    @PostMapping("/addCol")
    public Result addCol(@RequestParam("listId") int listId,@RequestParam("userId") int userId){
        listsService.addCol(listId, userId);
        return Result.success();
    }

    @DeleteMapping("/delCol/{listId}/{userId}")
    public Result delCol(@PathVariable("listId") int listId,@PathVariable("userId") int userId){
        listsService.delCol(listId, userId);
        return Result.success();
    }

    @GetMapping("/listById")
    public Result listById(int listId){
        Lists list=listsService.userOfList(listId);
        String key="list_"+listId;
        redisTemplate.opsForValue().increment(key);
        return Result.success(list);
    }
    @PostMapping("/addSongToList/{songId}/{listId}")
    public Result addSongToList(@PathVariable("songId") int songId,@PathVariable("listId") int listId){
        listsService.addSongToList(songId,listId);
        return Result.success();
    }

    @PostMapping("/addLists")
    public Result addList(@RequestBody Lists lists){
        int key=listsService.addLists(lists);
        redisTemplate.opsForValue().set("list_"+lists.getListId(),"0");
        return Result.success();
    }

    @PutMapping("putLists")
    public Result putLists(@RequestBody Lists lists){
        listsService.putLists(lists);
        return Result.success();
    }

    @DeleteMapping("/delLists/{id}")
    public Result delLists(@PathVariable int id){
        listsService.delLists(id);
        return Result.success();
    }
}
