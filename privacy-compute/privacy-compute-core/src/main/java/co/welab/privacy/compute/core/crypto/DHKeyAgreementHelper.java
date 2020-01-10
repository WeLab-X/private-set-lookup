package co.welab.privacy.compute.core.crypto;

import co.welab.privacy.compute.core.utils.StringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.spec.DHParameterSpec;
import java.math.BigInteger;
import java.security.*;

/**
 * @author Johnny.lin
 * @Description: Diffie-Hellman密钥协商的工具类
 * @date 19/8/8
 */
public class DHKeyAgreementHelper {
    private static final Logger LOG = LoggerFactory.getLogger(DHKeyAgreementHelper.class);

    // 指定加密算法为Diffie-Hellman，即DH
    private static final String ALGORITHM = "DH";

    /**
     * 生成DH的密钥对
     * @param keySize
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair createDHKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 通过私钥加密信息
     * @param privateKey 私钥
     * @param message 明文的信息
     * @return
     */
    public static MyPublicKey encryptByPrivateKey(PrivateKey privateKey, String message) {
        DHParameterSpec dhParamShared = ((DHPrivateKey) privateKey).getParams();
        BigInteger p = dhParamShared.getP();

        BigInteger g = new BigInteger(String.valueOf(message.hashCode()));
        BigInteger x = new BigInteger(StringUtil.byteToHex(privateKey.getEncoded()), 16);

        // calculate public value y
        BigInteger y = g.modPow(x, p);
        return new MyPublicKey(y, p);
    }

    /**
     * 通过密钥加密信息
     * @param privateKey
     * @param g
     * @param p
     * @return
     */
    public static MyPublicKey encryptByPrivateKey(PrivateKey privateKey, BigInteger g, BigInteger p) {
        BigInteger x = new BigInteger(StringUtil.byteToHex(privateKey.getEncoded()), 16);
        // calculate public value y
        BigInteger y = g.modPow(x, p);
        return new MyPublicKey(y, p);
    }

    /**
     * 通过密钥加密信息
     * @param privateKey
     * @param messages
     * @param p
     * @return
     */
    public static MyPublicKey[] encryptByPrivateKey(PrivateKey privateKey, String messages[], BigInteger p) {
        MyPublicKey myPublicKeys[] = new MyPublicKey[messages.length];

        BigInteger x = new BigInteger(StringUtil.byteToHex(privateKey.getEncoded()), 16);
        for (int i=0; i<messages.length; i++) {
            BigInteger g = new BigInteger(String.valueOf(messages[i].hashCode()));
            // calculate public value y
            BigInteger y = g.modPow(x, p);
            MyPublicKey myPublicKey = new MyPublicKey(y, p);
            myPublicKeys[i] = myPublicKey;
        }

        return myPublicKeys;
    }

    /**
     * 判断是否命中
     * @param privateKey 私钥
     * @param result 服务端返回的数据
     * @return
     * @throws Exception
     */
    public static boolean isHit(PrivateKey privateKey,  String result) throws Exception {
        if (StringUtil.isEmpty(result))
            throw new Exception("result is empty.");

        JSONObject json = JSONObject.parseObject(result, JSONObject.class);
        JSONObject data = json.getJSONObject("data");
        if (data == null || data.isEmpty()) {
            throw new Exception("data node is empty.");
        }

        JSONObject shareSecret = data.getJSONObject("sharedSecret");
        if (shareSecret == null || shareSecret.isEmpty()) {
            throw new Exception("shareSecret node is empty.");
        }

        JSONArray myPublicKeys = data.getJSONArray("myPublicKeys");
        if (myPublicKeys == null || myPublicKeys.isEmpty()) {
            throw new Exception("myPublicKeys node is empty.");
        }

        for (int i=0; i<myPublicKeys.size(); i++) {
            JSONObject obj = (JSONObject) myPublicKeys.get(i);
            BigInteger y = obj.getBigInteger("y");
            BigInteger p = obj.getBigInteger("p");
            MyPublicKey my = encryptByPrivateKey(privateKey, y, p);

            BigInteger shareSecretY = shareSecret.getBigInteger("y");
            BigInteger myY = my.getY();

            if (shareSecretY.equals(my.getY())) {
                return true;
            }
        }

        return false;
    }

}
