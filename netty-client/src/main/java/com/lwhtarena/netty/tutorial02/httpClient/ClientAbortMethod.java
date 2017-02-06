package com.lwhtarena.netty.tutorial02.httpClient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * @author： liwh
 * @Date: 2017/2/5.
 * @Description：<p>这个例子演示了如何中止一个HTTP请求。
 *
 *     </P>
 */
public class ClientAbortMethod {

    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpget =new HttpGet("http://www.baidu.com");

            System.out.println("Executing request " + httpget.getURI());
            CloseableHttpResponse response = httpClient.execute(httpget);

            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                // Do not feel like reading the response body
                // Call abort on the request object
                httpget.abort();
            }finally {
                response.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
