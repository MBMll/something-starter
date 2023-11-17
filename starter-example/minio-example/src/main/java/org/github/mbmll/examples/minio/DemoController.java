package org.github.mbmll.examples.minio;

import io.minio.MinioClient;
import io.minio.messages.Bucket;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author xlc
 * @Description
 * @Date 2023/5/25 22:51
 */
@Slf4j
@RestController
@RequestMapping("demo")
public class DemoController {

    @Autowired
    private MinioClient minioClient;

    @GetMapping("list-buckets")
    public List<Bucket> hello(String name) throws Exception {
        return minioClient.listBuckets();
//        return DemoEntity.builder().name(name).build();
    }

}
