package co.welab.privacy.compute.core.api;

import co.welab.privacy.compute.core.crypto.DHKeyAgreementHelper;
import co.welab.privacy.compute.core.crypto.MyPublicKey;
import co.welab.privacy.compute.core.utils.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.KeyPair;
import java.security.PrivateKey;

/**
 * @author Johnny.lin
 * @date 19/9/10
 * @Description:
 */
public class PrivacyComputeManger {
    private static final Logger LOG = LoggerFactory.getLogger(PrivacyComputeManger.class);

    /**
     * 判断是否命中手机黑名单
     * @param phoneNumber 明文的手机号码
     * @param phoneNumberWithMask 带掩码的手机号码
     * @param url 服务端请求地址
     * @return true:命中，false:未命中
     * @throws Exception
     */
    public static boolean isHitPhoneNumberBlacklist(String phoneNumber, String phoneNumberWithMask, String url) throws Exception {
        KeyPair keyPair = DHKeyAgreementHelper.createDHKeyPair(2048);
        PrivateKey privateKey = keyPair.getPrivate();
        // 通过私钥加密信息
        MyPublicKey myPublicKey = DHKeyAgreementHelper.encryptByPrivateKey(privateKey, phoneNumber);

        // 请求服务端
        JSONObject request = new JSONObject();
        request.put("myPublicKey", myPublicKey);
        request.put("phoneNumberWithMask", phoneNumberWithMask);
        String result = HttpClientUtil.postJson(url, request.toJSONString(), 10000);

        // 匹配结果
        boolean isHit = DHKeyAgreementHelper.isHit(privateKey, result);
        LOG.info("命中黑名单的结果为: " + isHit);
        return isHit;
    }
}
