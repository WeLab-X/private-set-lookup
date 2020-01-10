package co.welab.privacy.compute.core.utils;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

/**
 * @author Johnny.lin
 * @Description:
 * @date 19/8/18
 */
public class HttpClientUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final int TIMEOUT = 10000;

    public static String get(String url) {
        return get(url, TIMEOUT);
    }

    public static String get(String url, int timeOut) {
        return get(url, timeOut, null);
    }

    public static String get(String url, int timeOut, Map<String, String> map) {
        LOG.info("get url: " + url);
        Request request = Request.Get(url);
        if (timeOut > 0) {
            request.connectTimeout(timeOut).socketTimeout(timeOut);
        }
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            HttpResponse returnResponse = request.execute().returnResponse();
            int statusCode = returnResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                String result = StringUtil
                        .unicodeToString(EntityUtils.toString(returnResponse.getEntity(), "UTF-8"));
                LOG.info("get return: " + result);
                return result;
            } else {
                LOG.error("client.executeMethod failed: " + url + statusCode);
            }
        } catch (Exception e) {
            LOG.error("url:" + url + ", " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 发送 http post 请求
     *
     * @param url
     * @param data
     * @return
     */
    public static String post(String url, String data) {
        return post(url, data, TIMEOUT);
    }

    /**
     * 发送 http post 请求
     */
    public static String post(String url, String data, String mimeType) {
        return post(url, data, TIMEOUT, mimeType);
    }

    /**
     * 发送 http post 请求
     */
    public static String post(String url, String data, int timeOut) {
        return post(url, data, timeOut, "application/x-www-form-urlencoded");
    }

    /**
     * 发送 http post 请求
     *
     * @param url
     * @param data
     * @return
     * @timeOut time to timeOut
     */
    public static String post(String url, String data, int timeOut, String mimeType) {
        LOG.info("post url: " + url);
        if (StringUtil.isEmpty(url) || StringUtil.isEmpty(data)) {
            return null;
        }
        Request request = Request.Post(url).bodyString(data,
                ContentType.create(mimeType, Consts.UTF_8));
        if (timeOut > 0) {
            request.connectTimeout(timeOut).socketTimeout(timeOut);
        }
        try {
            HttpResponse returnResponse = request.execute().returnResponse();
            int statusCode = returnResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                LOG.info("post successfully.");
                String result = StringUtil.unicodeToString(EntityUtils.toString(returnResponse.getEntity(), "UTF-8"));
                LOG.info("post return: " + result);
                return result;
            } else {
                LOG.error("post failed: " + statusCode);
            }
        } catch (Exception e) {
            LOG.error("url:" + url + ", " + e.getMessage(), e);
        }
        return null;
    }

    public static String postJson(String url, String data) {
        return postJson(url, data, TIMEOUT);
    }

    /**
     * 发送 http post 请求
     *
     * @param url
     * @param data
     * @return
     */
    public static String postJson(String url, String data, int timeOut) {
        LOG.info("post url: " + url);
        if (StringUtil.isEmpty(url) || StringUtil.isEmpty(data)) {
            return null;
        }
        Request request = Request.Post(url).bodyString(data, ContentType.APPLICATION_JSON);
        if (timeOut > 0) {
            request.connectTimeout(timeOut).socketTimeout(timeOut);
        }
        try {
            HttpResponse returnResponse = request.execute().returnResponse();
            int statusCode = returnResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                LOG.info("post successfully.");
                String result = StringUtil
                        .unicodeToString(EntityUtils.toString(returnResponse.getEntity(), "UTF-8"));
                LOG.info("post return: " + result);
                return result;
            } else {
                LOG.error("post failed: " + statusCode);
            }
        } catch (Exception e) {
            LOG.error("url:" + url + ", " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * @return String    返回类型
     * @Title: method
     * @Description: 带header参数的post请求
     */
    public static String postJson(String url, String data, Map<String, String> headerParams, int timeOut) {
        LOG.info("post url: " + url);
        if (StringUtil.isEmpty(url) || StringUtil.isEmpty(data)) {
            return null;
        }
        Request request = Request.Post(url).bodyString(data, ContentType.APPLICATION_JSON);
        if (timeOut > 0) {
            request.connectTimeout(timeOut).socketTimeout(timeOut);
        }

        if (headerParams != null) {
            for (Map.Entry<String, String> entry : headerParams.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }

        try {
            HttpResponse returnResponse = request.execute().returnResponse();
            int statusCode = returnResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                LOG.info("post successfully.");
                String result = StringUtil
                        .unicodeToString(EntityUtils.toString(returnResponse.getEntity(), "UTF-8"));
                LOG.info("post return: " + result);
                return result;
            } else {
                LOG.error("post failed: " + statusCode);
            }
        } catch (Exception e) {
            LOG.error("url:" + url + ", " + e.getMessage(), e);
        }
        return null;
    }
}
