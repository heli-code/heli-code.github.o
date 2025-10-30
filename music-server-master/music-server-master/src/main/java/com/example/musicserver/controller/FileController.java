package com.example.musicserver.controller;

import com.example.musicserver.utils.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${config.ip}")
    private String ip;

    @PostMapping("/upload")
    public Result uploadListImg(@RequestParam("file") MultipartFile file,@RequestParam("type")String type,HttpServletRequest request) throws IllegalStateException, IOException {
        String dirPath = System.getProperty("user.dir")+"/file/"+type+"/";
        System.out.println("dirPath="+dirPath);

        File dir = new File(dirPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        int beginIndex = fileName.lastIndexOf(".");
        String suffix ="";
        if(beginIndex!=-1) {
            suffix = fileName.substring(beginIndex);
            fileName=fileName.substring(0,beginIndex);
        }
        String filename = fileName+UUID.randomUUID().toString().substring(0,6)+suffix;

        File dest = new File(dir,filename);
        //执行保存
        file.transferTo(dest);

        return Result.success(ip+"file/"+type+"/"+filename);//

}
    @RequestMapping("/{type}/{fileName}")
    public void getFile(@PathVariable("type")String type,@PathVariable("fileName")String fileName, HttpServletResponse response) throws IOException{

            String dirPath = System.getProperty("user.dir")+"/file/";
            String file=dirPath+type+"/"+fileName;
            FileInputStream hFile=new FileInputStream(file);
            int i=hFile.available();
            byte data[]=new byte[i];
            hFile.read(data);
            hFile.close();
            if("image".equals(type)) {
                response.setContentType("image/*");
            }else if("music".equals(type)){
                response.setContentType("audio/*");
            }
            OutputStream toClient=response.getOutputStream();
            toClient.write(data);
            toClient.close();



    }
}
