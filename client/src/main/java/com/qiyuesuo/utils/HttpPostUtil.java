//package com.qiyuesuo.utils;
//
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class HttpPostUtil {
//    //文件服务器的地址
//    public URL url;
//    HttpURLConnection conn;
//    String boundary = "-----" + System.currentTimeMillis();
//
//    public HttpPostUtil(String url) throws Exception {
//        this.url = new URL(url);
//    }
//
//    //设置请求头信息
//    private void initConnection() throws IOException {
//        conn = (HttpURLConnection) this.url.openConnection();
//
//        conn.setDoOutput(true);//如果想获得输出流，例如像web服务器输出数据
//        conn.setDoInput(true);
//        conn.setUseCaches(false);
//        conn.setConnectTimeout(10000);//连接超时
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Connection", "Keep_alive");
//        conn.setRequestProperty("Charset", "UTF-8");
//        //边界
//        conn.setRequestProperty("Content_type", "multipart/form-data;boundary=" + boundary);
//    }
//
//    public String fileUload(MultipartFile f) throws IOException {
////        OutputStream out = null;
//        BufferedOutputStream out=null;
//        try {
//            initConnection();
//            String originalFilename = f.getOriginalFilename();
//            //头部
//            StringBuilder sb = new StringBuilder();
//            sb.append("--");
//            sb.append(boundary);
//            sb.append("\r\n");
//            sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""+originalFilename+"\"\r\n");
//            sb.append("Content-Type: text/plain\r\n\r\n");
//            //获得输出流
////            out = new DataOutputStream(new File("C:\\Users\\Jasper\\Desktop"));
//           out = new BufferedOutputStream(new FileOutputStream(new File("C:\\Users\\Jasper\\Desktop\\zoo_bak.txt")));
////            out = new DataOutputStream(conn.getOutputStream());
//            out.write(sb.toString().getBytes("utf-8"));
//            //文件数据部分
//            byte[] bytes = f.getBytes();
//            out.write(bytes, 0, bytes.length);
//            //结尾部分
//            byte[] foot = ("\r\n--" + boundary + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
//            out.write(foot);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if(out!=null){
//                try {
//                    out.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        //获取提交后的执行结果
////         BufferedReader br= new BufferedReader(new InputStreamReader(conn.getInputStream()));
////        conn.connect();
////         String line = null;
////         String resultSet = br.readLine();
////         while ((line= br.readLine())!= null){
////                resultSet += line;
////        }
////         br.close();
////         resultSet=resultSet.trim();
//         conn.disconnect();
//         return  null;
//    }
//
//}
