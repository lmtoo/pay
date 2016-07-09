package com.somnus.pay.payment.thirdPay.shouxin.util;

import java.io.FileInputStream;
import java.math.BigInteger;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.util.encoders.Hex;

/**
  * @description: 首信提供RSAMD5工具类
  * @author: qingshu
  * @version: 1.0
  * @createdate: 2015-08-20
  * Modification  History:
  * Date         Author        Version        Discription
  * -----------------------------------------------------------------------------------
  * 2015-08-20   qingshu     1.0        首信提供RSAMD5工具类
 */
public class RSA_MD5 {
        RSAKeyParameters pubParameters;
        RSAKeyParameters privParameters;
        AsymmetricBlockCipher eng = new RSAEngine();
        static RSA_MD5 RSA_MD51 = new RSA_MD5();

        public int PublicVerifyMD5(String filename, String MD5Value, String src) {
            int i = 0;

            String s = ReadFile(filename);

            String mod = GetValue("m=", s);
            String pubExp = GetValue("e=", s);

            s = "";
            byte[] b = Hex.decode(MD5Value);
            b = RSA_MD51.PublicKeyDecrypt(mod, pubExp, b);

            s = new String(b);
            String ss = GetMD5(src.getBytes());

            i = s.compareToIgnoreCase(ss);
            return i; }

        public String PublicSignMD5(String filename, String src) {
            String MD5Value = "";
            int i = 0;

            String s = ReadFile(filename);

            String mod = GetValue("m=", s);
            String pubExp = GetValue("e=", s);

            s = GetMD5(src.getBytes());

            byte[] b = Hex.decode(s);
            b = RSA_MD51.PublicKeyEncrypt(mod, pubExp, s.getBytes());

            MD5Value = new String(Hex.encode(b));

            return MD5Value;
        }

        public int PrivateVerifyMD5(String filename, String MD5Value, String src)
        {
            int i = 0;

            String s = ReadFile(filename);

            String mod = GetValue("m=", s);
            String pubExp = GetValue("e=", s);
            String privExp = GetValue("privateExponent=", s);
            String p = GetValue("p=", s);
            String q = GetValue("q=", s);
            String pExp = GetValue("dP=", s);
            String qExp = GetValue("dQ=", s);
            String crtCoef = GetValue("qInv=", s);

            s = "";
            byte[] b = Hex.decode(MD5Value);
            b = RSA_MD51.PrivateKeyDecrypt(mod, pubExp, privExp, p, q, pExp, qExp, crtCoef, b);

            s = new String(b);
            String ss = GetMD5(src.getBytes());

            i = s.compareToIgnoreCase(ss);
            return i; }

        public String PrivateSignMD5(String filename, String src) {
            String MD5Value = "";
            int i = 0;

            String s = ReadFile(filename);

            String mod = GetValue("m=", s);
            String pubExp = GetValue("e=", s);
            String privExp = GetValue("privateExponent=", s);
            String p = GetValue("p=", s);
            String q = GetValue("q=", s);
            String pExp = GetValue("dP=", s);
            String qExp = GetValue("dQ=", s);
            String crtCoef = GetValue("qInv=", s);

            s = GetMD5(src.getBytes());

            byte[] b = Hex.decode(s);
            b = RSA_MD51.PrivateKeyEncrypt(mod, pubExp, privExp, p, q, pExp, qExp, crtCoef, s.getBytes());

            MD5Value = new String(Hex.encode(b));

            return MD5Value;
        }

        public byte[] PrivateKeyEncrypt(String mod, String pubExp, String privExp, String p, String q, String pExp, String qExp, String crtCoef, byte[] data)
        {
            byte[] out = new byte[0];
            RSAKeyParameters privParameters = new RSAPrivateCrtKeyParameters(new BigInteger(mod, 16), new BigInteger(pubExp, 16), new BigInteger(privExp, 16), new BigInteger(p, 16), new BigInteger(q, 16), new BigInteger(pExp, 16), new BigInteger(qExp, 16), new BigInteger(crtCoef, 16));

            this.eng = new PKCS1Encoding(((PKCS1Encoding)this.eng).getUnderlyingCipher());
            this.eng.init(true, privParameters);
            try
            {
                out = this.eng.processBlock(data, 0, data.length);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            System.err.println(new String(Hex.encode(out)));
            return out;
        }

        public byte[] PrivateKeyDecrypt(String mod, String pubExp, String privExp, String p, String q, String pExp, String qExp, String crtCoef, byte[] data) {
            byte[] out = new byte[0];
            RSAKeyParameters privParameters = new RSAPrivateCrtKeyParameters(new BigInteger(mod, 16), new BigInteger(pubExp, 16), new BigInteger(privExp, 16), new BigInteger(p, 16), new BigInteger(q, 16), new BigInteger(pExp, 16), new BigInteger(qExp, 16), new BigInteger(crtCoef, 16));

            this.eng.init(false, privParameters);
            try
            {
                data = this.eng.processBlock(data, 0, data.length);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            System.err.println(new String(data));
            return data;
        }

        public byte[] PublicKeyDecrypt(String mod, String pubExp, byte[] data)
        {
            byte[] out = new byte[0];
            RSAKeyParameters pubParameters = new RSAKeyParameters(false, new BigInteger(mod, 16), new BigInteger(pubExp, 16));

            this.eng.init(false, pubParameters);
            try
            {
                out = this.eng.processBlock(data, 0, data.length);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            System.err.println(new String(out));

            return out; }

        public byte[] PublicKeyEncrypt(String mod, String pubExp, byte[] data) {
            byte[] out = new byte[0];

            RSAKeyParameters pubParameters = new RSAKeyParameters(false, new BigInteger(mod, 16), new BigInteger(pubExp, 16));

            this.eng.init(true, pubParameters);
            try
            {
                out = this.eng.processBlock(data, 0, data.length);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            System.err.println(new String(Hex.encode(out)));

            return out;
        }

        public static String GetValue(String n, String src) {
            String s = "";

            int i1 = src.indexOf(n);
            i1 += n.length();

            int i2 = src.indexOf(";", i1);
            s = src.substring(i1, i2);
            return s; }

        public static String ReadFile(String FileName) {
            String s = "";
            try
            {
                FileInputStream f = new FileInputStream(FileName);
                byte[] b = new byte[f.available()];
                f.read(b);
                s = new String(b);
            }
            catch (Exception localException)
            {
            }

            return s;
        }

        public RSA_MD5() {
            this.eng = new PKCS1Encoding(this.eng);
        }

        public static String GetMD5(byte[] bytes)
        {
            Digest digest = new MD5Digest();
            byte[] resBuf = new byte[digest.getDigestSize()];
            String resStr = "";

            digest.update(bytes, 0, bytes.length);

            digest.doFinal(resBuf, 0);

            resStr = new String(Hex.encode(resBuf));

            return resStr;
        }
}
