package com.lyl.demo.aliyun.test;

import com.battcn.swagger.properties.ApiDataType;
import com.battcn.swagger.properties.ApiParamType;
import com.lyl.demo.aliyun.TestPostCont;
import com.lyl.demo.aliyun.dulplicate.annotation.AvoidDuplicateSubmission;
import com.lyl.demo.aliyun.lock.CacheLock;
import com.lyl.demo.aliyun.lock.CacheParam;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author lyl
 * @Description
 * @Date 2018/9/30 下午3:30
 */
@RestController
@Slf4j
public class TestController {

    public static final String APP_ID = "2016122204530085";
    public static final String APP_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMKf0X5/DR9vj3X33aRvPMHf6uCqhpLnX4m8+/j4p68HfddX+9v08+AKIcPMg6Pn87VrzhYLCsmMYVJVO5dz7ZAxl0qnb1M9xbV9ZC7U0C9Opv4wim8cFne1kAuJ0QFq9SSQvX80BGyobWCfTvITF1d48oAgeQcX8XA8rm4OEuM7AgMBAAECgYEAtXHgEqmKZ3NPcTDLbyDRqQID7L6uRcj1lekdlKjlU9WNKbV6Nj9xGb74aZ2CYM2/tLSz5nMW9H4uwV569yD6KSLekuNCQmZ+xvjtpa5CKmvW0vcNjGlx6ZzwJB40OETsJo5prq/6fFeIt52V+JcRnPO0I5P371vWSrrSYQrwAekCQQDmENTKJ0r2Muh9W8lIf12/FHOicQm0yPaljY5QAA1AZiP+ej3oTjSvheOwib/drKvfUObHkUM4DtevCC++7V4fAkEA2JA6ZtcOhgpMsHA+2bMzBLRwQngOmurCkYI26L1lvemE791TkLK3/bW4MZSQHxM+Ssjw8g2dkj0t/IOl1RkfZQJBAMhGoKa2W+hE2uS98VQeJCLCEAO5+WQaibQGrXtxABgukmjmS1weMHkXyeUVlSPJO/l0OOJDz0tOiG3DZ6UCh6sCQC1FWTAOXNbgaE+p5zmTIGMnZSBAAK2ZzbqlQd09WntsyYFq/wEMmzHKPwr+R3J3j1oKz3OKLRpY97gFvTDi45ECQQCeWFquPm5Vrl5LygSGNPfb+HK+Ml4oKheE41LpwnDmJKoOeTPkL8dLI8T4A0sq7YIIU3dgvYK317RGy6fpiKHe";
    public static final String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

    public static RestClient restClient;
    private static Client client;
    @Autowired
    private TestPostCont testPostCont;

    /*@GetMapping("/test/create")
    public static void testAliyun() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY,"json","GBK", ALIPAY_PUBLIC_KEY,"RSA");
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\"20171115010101001\"," +
                "\"total_amount\":0.01," +
                "\"subject\":\"Iphone616G\"," +
                "\"buyer_id\":\"用户pid\"" +
                "}");
        AlipayTradeCreateResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }

    }

    public static void testAliyunQuery() throws AlipayApiException {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, "RSA");
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();//创建API对应的request类
        request.setBizContent("{" +
                "    \"out_trade_no\":\"20150320010101001\"}"); //设置业务参数
        AlipayTradeQueryResponse response = alipayClient.execute(request);//通过alipayClient调用API，获得对应的response类
        String str = null;
        if (response.isSuccess()){
            System.out.println("查询成功");
        }else {
            System.out.println("调用失败");
        }
        try {
            str = URLDecoder.decode(response.getBody(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(str);

    }

    @GetMapping("/test/notify")
    public void testNotify(HttpServletRequest request){
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
//        boolean flag = AlipaySignature.rsaCheckV1(params, alipaypublicKey, charset,"RSA2")
    }

    public static void testAliyunOrderCreate() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, "RSA");

        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo("20150320010101001");
        model.setSellerId("2088102146225135");
        model.setTotalAmount("0.01");
        model.setDiscountableAmount("0.01");
        model.setSubject("Iphone6 16G");
        model.setBody("Iphone6 16G");
        request.setBizModel(model);
        AlipayTradeCreateResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    public static void testPrecreate() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, "RSA");
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();//创建API对应的request类
        request.setBizContent("{" +
                "    \"out_trade_no\":\"20150320010101002\"," +
                "    \"total_amount\":\"88.88\"," +
                "    \"subject\":\"Iphone6 16G\"," +
                "    \"store_id\":\"NJ_001\"," +
                "    \"timeout_express\":\"90m\"}");//设置业务参数
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        System.out.print(response.getBody());
    }

    public static void testCancel() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, "RSA");
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();//创建API对应的request类
        request.setBizContent("{" +
                "    \"out_trade_no\":\"20150320010101001\"," +
                "    \"trade_no\":\"2014112611001004680073956707\"}"); //设置业务参数
        AlipayTradeCancelResponse response = alipayClient.execute(request);//通过alipayClient调用API，获得对应的response类
        String str = null;
        if (response.isSuccess()){
            System.out.println("查询成功");
        }else {
            System.out.println("调用失败");
        }
        try {
            str = URLDecoder.decode(response.getBody(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(str);
    }


    @PostMapping("/test/webapp")
    public void testWebApp(HttpServletRequest request, HttpServletResponse response) throws IOException {
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, "RSA"); //获得初始化的AlipayClient
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
//            alipayRequest.setReturnUrl("http://localhost:8080/CallBack/return_url.jsp");
//            alipayRequest.setNotifyUrl("http://localhsot:8080/CallBack/notify_url.jsp");//在公共参数中设置回跳和通知地址
            alipayRequest.setBizContent("{" +
                    " \"out_trade_no\":\"20150320010101002\"," +
                    " \"total_amount\":\"0.01\"," +
                    " \"subject\":\"Iphone6 16G\"," +
                    " \"product_code\":\"QUICK_WAP_PAY\"" +
                    " }");//填充业务参数
            String form="";
            try {
                form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(form);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
    }

    @GetMapping("/test/webapp")
    public void getWebApp(
            @RequestParam(value = "money", required = true) Integer money,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, "RSA"); //获得初始化的AlipayClient
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
            alipayRequest.setReturnUrl("http://www.baidu.com");
//            alipayRequest.setNotifyUrl("http://localhsot:8080/CallBack/notify_url.jsp");//在公共参数中设置回跳和通知地址
        String orderId = String.valueOf(System.currentTimeMillis());
        String m = BigDecimal.valueOf(money).divide(BigDecimal.valueOf(100)) + "";
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(orderId);
        model.setSubject("Iphone6 16G");
        model.setProductCode("QUICK_WAP_WAY");
        model.setTotalAmount(m);
        alipayRequest.setBizModel(model);
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(form);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
    }

    @PutMapping(value = "/advertise/test/{id}")
    public void testId(@PathVariable(value = "id") String id){
        System.out.println(id);
    }*/

    @GetMapping(value = "/test/es")
    public void testElasticsearch() throws UnknownHostException {
        String ip = "localhost";
        Settings settings = Settings.builder()
                //集群名称
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

    @GetMapping(value = "/test/es/insert")
    public void testEsClient() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "Smith Chen");
        map.put("age", 5);
        map.put("interests", new String[] { "reading", "film" });
        map.put("about", "I love to go rock climbing");

        String index = "index";
        String type = "type";


        IndexResponse response = client.prepareIndex(index, type, UUID.randomUUID().toString())
                .setSource(map).get();
        System.out.println("写入数据结果=" + response.status().getStatus() + "！id=" + response.getId());

    }

    @GetMapping(value = "/test/es/get")
    public void testGetClient() {
        SearchRequestBuilder requestBuilder = client.prepareSearch("index").setTypes("type")
                .setQuery(QueryBuilders.matchQuery("about", "rock climbing"));
        System.out.println(requestBuilder.toString());

        SearchResponse response = requestBuilder.execute().actionGet();

        System.out.println(response.status());
        if (response.status().getStatus() == 200) {
            for (SearchHit hits : response.getHits().getHits()) {
                System.out.println(hits.getSourceAsString());
            }
        }
    }

    @GetMapping(value = "/test/es/rest/init")
    public void testGetESClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200));
        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder) {
                builder.setConnectTimeout(10000);
                builder.setSocketTimeout(30000);
                builder.setConnectionRequestTimeout(10000);
                return builder;
            }
        });
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                return httpClientBuilder.setDefaultIOReactorConfig(
                        IOReactorConfig.custom()
                                .setIoThreadCount(100)//线程数配置
                                .setConnectTimeout(10000)
                                .setSoTimeout(10000)
                                .build());
            }
        });
        //设置超时
        builder.setMaxRetryTimeoutMillis(10000);
        //构建low level client
        restClient = builder.build();
    }

    @GetMapping(value = "/test/es/rest/insert")
    public void testGetESRestClientInsert() throws IOException {
        String method = "PUT";
        String endpoint = "/test-index/test/1";
        HttpEntity entity = new NStringEntity(
                "{\n" +
                        "    \"user\" : \"kimchy\",\n" +
                        "    \"post_date\" : \"2009-11-15T14:12:12\",\n" +
                        "    \"message\" : \"trying out Elasticsearch\"\n" +
                        "}", ContentType.APPLICATION_JSON);

        Response response = restClient.performRequest(method,endpoint, Collections.<String, String>emptyMap(),entity);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    @GetMapping(value = "/test/es/rest/get")
    public void testGetESRestClientGet() throws IOException {
        String method = "GET";
        String endpoint = "/test-index/test/1";
        Response response = restClient.performRequest(method,endpoint);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    @GetMapping(value = "/test/exception")
    public void testException(){
        int i = 10 / 0;
        System.out.println("测试异常");
    }

    @GetMapping(value = "/test/con")
    public void testCon(){
        System.out.println(testPostCont.getId());
        System.out.println(TestPostCont.getAge());


        testPostCont.setId(testPostCont.getId() + 10);
        TestPostCont.setAge(TestPostCont.getAge() + 10);

        System.out.println(testPostCont.getId());
        System.out.println(testPostCont.getAge());
    }

    @GetMapping(value = "/test/exce")
    public void testExce(){
        log.error("abc, {}", "eaard", new RuntimeException());
    }


    @ApiOperation(value = "测试防提交锁")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "标志符号", dataType = ApiDataType.INT, paramType = ApiParamType.FORM),
    })
    @CacheLock(prefix = "test:lock", expire = 10, timeUnit = TimeUnit.MINUTES)
    @GetMapping("/test/lock")
    public String query(@CacheParam(name = "token") Integer token){
        System.out.println("进入了");
        return "success - " + token;
    }

    @AvoidDuplicateSubmission(needSaveToken = true)
    @GetMapping("/test/avoid/save")
    public String testAvoid(){
        System.out.println("needSaveToken");
        return "needSaveToken";
    }

    @AvoidDuplicateSubmission(needRemoveToken = true)
    @GetMapping("/test/avoid/remove")
    public String testNeedRemoveToken(){
        System.out.println("needRemoveToken");
        return "needRemoveToken";
    }

}
