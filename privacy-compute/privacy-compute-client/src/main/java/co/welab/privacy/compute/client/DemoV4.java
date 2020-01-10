package co.welab.privacy.compute.client;

import co.welab.privacy.compute.core.api.PrivacyComputeManger;

/**
 * @author Johnny.lin
 * @Description:
 * @date 19/8/13
 */
public class DemoV4 {
    public static void main(String[] args) throws Exception {

        boolean isHit = PrivacyComputeManger.isHitPhoneNumberBlacklist("13812341234", "1381234****", "http://localhost:8080/pcs/phoneNumberBlacklistV4");
        System.out.println("命中黑名单的结果为: " + isHit);
    }

}
