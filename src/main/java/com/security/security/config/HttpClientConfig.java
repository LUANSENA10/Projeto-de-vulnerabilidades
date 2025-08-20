package com.security.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Slf4j
@Configuration
public class HttpClientConfig {

    @Bean
    public HttpClient httpClientLocal() throws NoSuchAlgorithmException, KeyManagementException {
        log.info("HttpClient - Configurado para ambiente [LOCAL]");
        return HttpClient.newBuilder()
                .sslContext(createSSLContext())
                .build();
    }

    private SSLContext createSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{new TrustAllCertificates()}, null);
        return sslContext;
    }

    private static class TrustAllCertificates implements javax.net.ssl.X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
