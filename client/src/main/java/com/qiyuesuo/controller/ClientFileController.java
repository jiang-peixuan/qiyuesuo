package com.qiyuesuo.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiyuesuo.pojo.MyFile;
import com.qiyuesuo.utils.AESUtil;
import com.qiyuesuo.utils.RSAUtil;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.security.interfaces.RSAPrivateKey;
import java.util.List;
import java.util.Random;
import java.util.UUID;


/*
  接收前端上传的文件，接收到的文件需要调用 Server 端
的文件上传接口，将文件上传至文件服务器，然后调用服务端的的元数据接
口获取文件详细信息返回给前端，响应前端的下载接口，从 Server 端下载
文件，解密后返回给前端*/

@Controller
public class ClientFileController {

    private static final Logger logger = Logger.getLogger(ClientFileController.class);
    public static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ3HwsCHIKz3CErQS7+NFqLAW5SGHw+J444ozfuZ/Sm6pLUzIDksJ7//mtmBDKydfx+3dXbqCjd/QGYbV+XCk4FmfGJHy4WpaUwOc5wh2UoCBE9SEdXOpmvFSV50HcWxUncsc6ufr/Bpy5Ktks3RsZ0c73lEE6FOFezcTlbtoDMhAgMBAAECgYAgK0Rn4KUm3s8QAdwP2AJPeIyzgYz/rAt7RpKIw+K8CVPfpebiAUCxgrndstQUtZ/fpZYLgrhGjGli6BxJuhw8qVpYu01APMbLGj3JhrAWT0zPMQw+JmmIyHKl8q43Dy8/dvhZ+jZdf6WRJxldyMLLJszUqVPsU/eAxEiKALfRgQJBAOGW3YHOE95a5dO1yKA/1Nc+wb2gVX9tKzCh5ZrLDu6a3GmW7Lk9gtuMtOi0r1AlwgvgJH6aaAKFTnE76x14FhkCQQCzDMrmwH1w86fsoha0zJez9zQIyVcunxPdRudySK3/VG0VGG0obGEaKT1lo3AewJ7qb6I1T7uAI9Iwf2QWUdZJAkBdingpBfGZJunbwqoBQNaZti0R2zT4lKTvEoKpj/+OEurIYcug+A+VyB+PyrRTMITo9bVMRexQ90PSkjzoyE2pAkAOK84HU2baQL6isPWBG8xJ9x/MLjtTOk31Ln51AiGbWtBDYiqJj4Jj8q2kVLo0BOTPA0TgWU4qxysEoaCHT7TZAkEAqmnxx2euNUc++xsPOHTJHtKts+K8IgTxldAJCTDVO3eltU69bKHd/tUBen/oyUJ9OWGp+WTFxyh8vQ4rF5kZkw==";


    private static HttpHeaders builderHeader() throws Exception {
        // 请求头设置属性
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentType(MediaType.parseMediaType("multipart/form-data; charset=UTF-8"));
        // X-SID :随机数
//        String sid = getRandomString(10);
        String sid = UUID.randomUUID().toString().replaceAll("-", "");
        // X-Signature :RSA算法 使用私钥对sid加密
        String signature = RSAUtil.getSignByPrivateKey(sid).replaceAll("\n", "");
        headers.add("X-SID", sid);
        headers.add("X-Signature", signature);
        return headers;
    }

    @PostMapping(value = "/ClientUpload")
    public String ClientUpload(Model model, @RequestParam(value = "file") MultipartFile file) throws Exception {
        if(file!=null){
            builderHeader();
            String url = "http://localhost:9090/server/ServerFileUpload";
            String uuid = UploadUtil.uploadFileTest(file, url);
            model.addAttribute("uuid", uuid);
        }
        return "index";
    }

    @RequestMapping("/clientFileFindById")
    public String  clientFileFind(Model model,@RequestParam String uuid) throws Exception {
        builderHeader();
        uuid="ad29bfe9fcc141738697065775586d9f..xlsx";
        MyFile oneById = findOneById(uuid);
        System.out.println(oneById);
        MyFile myfile = new MyFile();
        model.addAttribute("file1",myfile);
        System.out.println(myfile.toString());
        return "index";
    }

    @RequestMapping("/clientFileFind")
    public static String clientFileFind(Model model) throws Exception {
        builderHeader();
        String url = "http://localhost:9090/server/serverFileFind";
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(url, String.class);
        Gson gson = new Gson();
        List<MyFile> myFiles = gson.fromJson(forObject, new TypeToken<List<MyFile>>(){}.getType());
        model.addAttribute("myFiles",myFiles);
        return "index";
    }

    @GetMapping("/clientDownload")
    public   void clientDownload(@RequestParam("newName") String newName, HttpServletResponse response) throws Exception {

        // 设置header
       builderHeader();
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(form, null);
        RestTemplate restTemplate = new RestTemplate();
        // 2.向Server端请求下载
        String url ="http://localhost:9090/server/serverDownLoad?uuid="+newName;
        ResponseEntity<Resource> result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Resource.class);
        if (result.getStatusCode() == HttpStatus.OK) {
            InputStream in = result.getBody().getInputStream();
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(newName, "UTF-8"));
            OutputStream out = response.getOutputStream();
            // 3.解密，输出到浏览器
            //我们需要拿到数组信封，对其解密！！得到AES
            MyFile myFile = findOneById(newName);
            //获取私钥
            RSAPrivateKey privateKey = RSAUtil.getPrivateKey();
            String AESkey = RSAUtil.decryptByPrivateKey(myFile.getDigitalEnvelope(), privateKey);
            logger.info("得到AESkey:"+AESkey);
            //使用AES对文件解密
            AESUtil.decryptFileAndOutput(AESkey, in, out);
        }
    }


    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static void inputStreamToFile(InputStream in, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer, 0, 1024)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //下载的时候，需要数字信封。
    public  MyFile  findOneById(String uuid) throws Exception {
        builderHeader();
        String url = "http://localhost:9090/server/serverFileFindById";
        url = url + "?filename=" + uuid;
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(url, String.class);
        Gson gson = new Gson();
        MyFile myfile = gson.fromJson(forObject, MyFile.class);
        return myfile;
    }


    @Test
    public void  test123() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("key\\private.key")));
        System.out.println("uu");
    }
}