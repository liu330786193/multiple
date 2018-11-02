package com.lyl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LylWebApplicationTests {

    @Test
    public void contextLoads() {


    }


    public void download(HttpServletRequest request, HttpServletResponse response){

        String[] files = new String[]{"http://pic14.nipic.com/20110605/1369025_165540642000_2.jpg","http://img.zcool.cn/community/0125fd5770dfa50000018c1b486f15.jpg@1280w_1l_2o_100sh.jpg"};

        String blUrl = "http://pic14.nipic.com/20110605/1369025_165540642000_2.jpg";
        String suffix = blUrl.substring(blUrl.lastIndexOf("."), blUrl.length());
        String downloadFilename = "申请资料.zip";//文件的名称

        try {
            downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");//转换中文否则可能会产生乱码
            response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);// 设置在下载框默认显示的文件名
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            for (int i=0;i<files.length;i++) {
                URL url = new URL(files[i]);
                zos.putNextEntry(new ZipEntry(i + suffix));
                InputStream fis = url.openConnection().getInputStream();
                byte[] buffer = new byte[1024];
                int r = 0;
                while ((r = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, r);
                }
                fis.close();
            }
            zos.flush();
            zos.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    private void testStatic(){
        System.out.println(StaticTest.NAME);
    }

}
