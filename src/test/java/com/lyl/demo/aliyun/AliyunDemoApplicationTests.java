package com.lyl.demo.aliyun;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AliyunDemoApplicationTests {

    public static Client client;

    @Test
    public void contextLoads() {
    }


    @Test
    public void testElasticsearch() throws UnknownHostException {
        String ip = "localhost";
        Settings settings = Settings.builder()
                //集群名称
                .put("cluster.name", "onesearch")
                //自动嗅探
                .put("client.transport.sniff", true)
                .put("discovery.type", "zen")
                .put("discovery.zen.minimum_master_nodes", 1)
                .put("discovery.zen.ping_timeout", "500ms")
                .put("discovery.initial_state_timeout", "500ms")
                .build();
        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), 9300));

    }


    @Test
    public void testEsClient() throws UnknownHostException {
        String ip = "localhost";
        Settings settings = Settings.builder()
                //集群名称
                .put("cluster.name", "onesearch")
                //自动嗅探
                .put("client.transport.sniff", true)
                .put("discovery.type", "zen")
                .put("discovery.zen.minimum_master_nodes", 1)
                .put("discovery.zen.ping_timeout", "500ms")
                .put("discovery.initial_state_timeout", "500ms")
                .build();
        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), 9300));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "Smith Chen");
        map.put("age", 5);
        map.put("interests", new String[] { "reading", "film" });
        map.put("about", "I love to go rock climbing");

        IndexResponse response = client.prepareIndex("megacorp", "employee", UUID.randomUUID().toString())
                .setSource(map).get();
        System.out.println("写入数据结果=" + response.status().getStatus() + "！id=" + response.getId());

    }

}
