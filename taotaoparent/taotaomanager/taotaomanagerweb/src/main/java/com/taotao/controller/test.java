package com.taotao.controller;

import com.taotao.common.utils.FtpUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @ Create by ostreamBaba on 18-11-27
 * @ 描述
 */

@Controller
public class test {

    @RequestMapping("/123")
    @ResponseBody
    public int a(){
        return 1;
    }

    //上传成功
    //把图片长传到文件服务器
    @Test
    public void testFtpClient() throws IOException {
        //创建一个Ftp对象
        FTPClient ftpClient = new FTPClient();
        //创建ftp连接
        //port 默认21
        ftpClient.connect("127.0.0.1");
        //登录ftp服务器
        ftpClient.login("ftpuser","iostream");
        //上传文件
        //第一个参数 服务器端文件名
        //第二个参数 上传文件的InputStream 字节流
        FileInputStream in = new FileInputStream(new File("/home/ios666/Desktop/1.jpg"));
        //设置上传的路径
        ftpClient.changeWorkingDirectory("/var/myftp/images");
        //设置上传文件的格式
        ftpClient.setFileType( FTP.BINARY_FILE_TYPE);
        ftpClient.storeFile("hello2.jpg", in);
        //关闭链接
        ftpClient.logout();
    }

    @Test
    public void testFtpUtil() throws FileNotFoundException {
        FileInputStream in = new FileInputStream(new File("/home/ios666/Desktop/1.jpg"));
        FtpUtils.uploadFile("127.0.0.1", 21, "ftpuser", "iostream", "/var/myftp/images"
        ,"/2018/12/09", "test1.jpg", in);
    }


}
