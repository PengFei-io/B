package com.io.test;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.io.WebAppApplication;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.HashSet;

@SpringBootTest(classes = WebAppApplication.class)
@RunWith(SpringRunner.class)
public class MyTest {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Test
    public void testUpload() throws Exception {
        FileInputStream inputStream = new FileInputStream("D:\\临时文件\\大数据\\Day30-flink\\assets\\1566785920711.png");
        FastImageFile png = new FastImageFile(inputStream, inputStream.available(),
                "png", new HashSet<>());
        StorePath storePath = fastFileStorageClient.uploadImage(png);
        System.out.println(storePath);
    }

    @Test
    public void testDownLoad() throws Exception {
        ByteArrayOutputStream baos = fastFileStorageClient.downloadFile("group1", "M01/00/00/wKiGkV1zsY2AIltxAAEppTeurXo326.png", new DownloadCallback<ByteArrayOutputStream>() {

            @Override

            public ByteArrayOutputStream recv(InputStream ins) throws IOException {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtils.copy(ins, baos);
                return baos;

            }
        });

        IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), new FileOutputStream("D:\\A.png"));
    }
}
