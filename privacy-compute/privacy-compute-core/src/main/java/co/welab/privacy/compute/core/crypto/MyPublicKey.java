package co.welab.privacy.compute.core.crypto;

import java.math.BigInteger;

/**
 * @author Johnny.lin
 * @Description:
 * @date 19/9/2
 */
public class MyPublicKey {

    // the public key
    private BigInteger y;

    // the prime modulus
    private BigInteger p;

    public MyPublicKey() {}

    public MyPublicKey(BigInteger y, BigInteger p) {
        this.y = y;
        this.p = p;
    }

    public BigInteger getY() {
        return y;
    }

    public void setY(BigInteger y) {
        this.y = y;
    }

    public BigInteger getP() {
        return p;
    }

    public void setP(BigInteger p) {
        this.p = p;
    }
}
