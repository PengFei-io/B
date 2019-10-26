package com.tcwl.tea2;

public class TEA {

    private static long UINT32_MAX = 0xFFFFFFFFL;
    private static long delta = 0x9E3779B9L;

    private static long k0;
    private static long k1;
    private static long k2;
    private static long k3;

    private static int loops = 32;
    /**
     * 秘钥
     */
    private static byte Commkey[] = {0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36,
            0x37, 0x38, 0x39, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66};

    /**
     * 加密
     *
     * @param data
     * @param key
     * @return
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        int data_len = data.length; // 数据的长度
        if (data_len == 0) {
            return new byte[]{};
        }

        if (!setKey(key)) {
            return new byte[]{};
        }

        int group_len = 8;
        int residues = data_len % group_len; // 余数
        int dlen = data_len - residues;
        // 用于储存加密的密文，第一字节为余数的大小
        int result_len = data_len + 0; //改过。+1
        if (residues > 0) {
            result_len += group_len - 0;//改过  -residues
        }
        byte[] result = new byte[result_len];
        //result[0] = (byte) residues;

        byte[] plain = new byte[group_len];
        byte[] enc = new byte[group_len];

        for (int i = 0; i < dlen; i += group_len) {
            for (int j = 0; j < group_len; j++) {
                plain[j] = data[i + j];
            }
            enc = encrypt_group(plain);
            for (int k = 0; k < group_len; k++) {
                result[i + k + 0] = enc[k]; //改过 +1
            }
        }
        if (residues > 0) {
            for (int j = 0; j < residues; j++) {
                plain[j] = data[dlen + j];
            }
            int padding = group_len - residues;
            for (int j = 0; j < padding; j++) {
                plain[residues + j] = (byte) 0x00;
            }
            enc = encrypt_group(plain);
            for (int k = 0; k < group_len; k++) {
                result[dlen + k + 1] = enc[k];
            }
        }
        return result;
    }

    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        int group_len = 8;
        if (!setKey(key)) {
            return new byte[]{};
        }
        int data_len = data.length - 0, dlen; // 数据的长度 改过-1
        int residues = data_len % group_len; // 余数 改过 data[0]
        if (residues > 0) {
            dlen = data_len - group_len;
        } else {
            dlen = data_len;
        }

        byte[] result = new byte[dlen + residues];

        byte[] dec = new byte[group_len];
        byte[] enc = new byte[group_len];
        for (int i = 0; i < dlen; i += group_len) {
            for (int j = 0; j < group_len; j++) {
                enc[j] = data[i + j + 0]; //改过 1
            }
            dec = decrypt_group(enc);
            for (int k = 0; k < group_len; k++) {
                result[i + k] = dec[k];
            }
        }
        return result;
    }

    /**
     * 设置密钥
     *
     * @param k 密钥
     * @return 密钥长度为16个byte时， 设置密钥并返回true，否则返回false
     */
    public static boolean setKey(byte[] k) {
        if (k.length != 16) {
            return false;
        }
        k0 = bytes_to_uint32(new byte[]{k[3], k[2], k[1], k[0]});
        k1 = bytes_to_uint32(new byte[]{k[7], k[6], k[5], k[4]});
        k2 = bytes_to_uint32(new byte[]{k[11], k[10], k[9], k[8]});
        k3 = bytes_to_uint32(new byte[]{k[15], k[14], k[13], k[12]});
        return true;
    }

    /**
     * 设置加密的轮数，默认为32轮
     * <p>
     * 加密轮数
     *
     * @return 轮数为16、32、64时，返回true，否则返回false
     */
    public boolean setLoops(int loopss) {
        loops = loopss;
        switch (loops) {
            case 16:
            case 32:
            case 64:
                return true;
        }
        return false;
    }

    /**
     * 加密一组明文
     *
     * @param v 需要加密的明文
     * @return 返回密文
     */
    private static byte[] encrypt_group(byte[] v) {
        long v0 = bytes_to_uint32(new byte[]{v[3], v[2], v[1], v[0]});
        long v1 = bytes_to_uint32(new byte[]{v[7], v[6], v[5], v[4]});
        long sum = 0L;
        long v0_xor_1 = 0L, v0_xor_2 = 0L, v0_xor_3 = 0L;
        long v1_xor_1 = 0L, v1_xor_2 = 0L, v1_xor_3 = 0L;
        for (int i = 0; i < loops; i++) {
            sum = toUInt32(sum + delta);
            v0_xor_1 = toUInt32(toUInt32(v1 << 4) + k0);
            v0_xor_2 = toUInt32(v1 + sum);
            v0_xor_3 = toUInt32((v1 >> 5) + k1);
            v0 = toUInt32(v0 + toUInt32(v0_xor_1 ^ v0_xor_2 ^ v0_xor_3));
            v1_xor_1 = toUInt32(toUInt32(v0 << 4) + k2);
            v1_xor_2 = toUInt32(v0 + sum);
            v1_xor_3 = toUInt32((v0 >> 5) + k3);
            // System.out.printf("%08X\t%08X\t%08X\t%08X\n", i, v0, v0 >> 5,
            // k3);
            v1 = toUInt32(v1 + toUInt32(v1_xor_1 ^ v1_xor_2 ^ v1_xor_3));
        }
        byte[] b0 = long_to_bytes(v0, 4);
        byte[] b1 = long_to_bytes(v1, 4);
        return new byte[]{b0[0], b0[1], b0[2], b0[3], b1[0], b1[1], b1[2],
                b1[3]};
    }

    /**
     * 解密一组密文
     *
     * @param v 要解密的密文
     * @return 返回明文
     */
    private static byte[] decrypt_group(byte[] v) {
        long v0 = bytes_to_uint32(new byte[]{v[3], v[2], v[1], v[0]});
        long v1 = bytes_to_uint32(new byte[]{v[7], v[6], v[5], v[4]});

        long sum = 0xC6EF3720L, // delta << 5
                tmp = 0L;
        for (int i = 0; i < loops; i++) {
            tmp = toUInt32(toUInt32(v0 << 4) + k2);
            v1 = toUInt32(v1
                    - toUInt32(tmp ^ toUInt32(v0 + sum)
                    ^ toUInt32((v0 >> 5) + k3)));
            tmp = toUInt32(toUInt32(v1 << 4) + k0);
            v0 = toUInt32(v0
                    - toUInt32(tmp ^ toUInt32(v1 + sum)
                    ^ toUInt32((v1 >> 5) + k1)));
            sum = toUInt32(sum - delta);
        }
        byte[] b0 = long_to_bytes(v0, 4);
        byte[] b1 = long_to_bytes(v1, 4);
        return new byte[]{b0[0], b0[1], b0[2], b0[3], b1[0], b1[1], b1[2],
                b1[3]};
    }

    /**
     * 将 long 类型的 n 转为 byte 数组，如果 len 为 4，则只返回低32位的4个byte
     *
     * @param n
     * 需要转换的long
     * @param len
     * 若为4，则只返回低32位的4个byte，否则返回8个byte
     * @return 转换后byte数组
     */

    private static long BYTE_1 = 0xFFL;
    private static long BYTE_2 = 0xFF00L;
    private static long BYTE_3 = 0xFF0000L;
    private static long BYTE_4 = 0xFF000000L;

    private static byte[] long_to_bytes(long n, int len) {
        byte a = (byte) ((n & BYTE_4) >> 24);
        byte b = (byte) ((n & BYTE_3) >> 16);
        byte c = (byte) ((n & BYTE_2) >> 8);
        byte d = (byte) (n & BYTE_1);
        if (len == 4) {
            return new byte[]{d, c, b, a};
        }
        byte ha = (byte) (n >> 56);
        byte hb = (byte) ((n >> 48) & BYTE_1);
        byte hc = (byte) ((n >> 40) & BYTE_1);
        byte hd = (byte) ((n >> 32) & BYTE_1);
        return new byte[]{hd, hc, hb, ha, d, c, b, a};
    }

    /**
     * 将4个byte转为 Unsigned Integer 32，以 long 形式返回
     *
     * @param bs 需要转换的字节
     * @return 返回 long，高32位为0，低32位视为Unsigned Integer
     */
    private static long bytes_to_uint32(byte[] bs) {
        return ((bs[0] << 24) & BYTE_4) + ((bs[1] << 16) & BYTE_3)
                + ((bs[2] << 8) & BYTE_2) + (bs[3] & BYTE_1);
    }

    /**
     * 将long的高32位清除，只保留低32位，低32位视为Unsigned Integer
     *
     * @param n 需要清除的long
     * @return 返回高32位全为0的long
     */
    private static long toUInt32(long n) {
        return n & UINT32_MAX;
    }

    /**
     * 字节数组转成16进制字符串
     */
    private static final char HexCharArr[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String byteArrToHex(byte[] btArr) {
        char strArr[] = new char[btArr.length * 2];
        int i = 0;
        for (byte bt : btArr) {
            strArr[i++] = HexCharArr[bt >>> 4 & 0xf];
            strArr[i++] = HexCharArr[bt & 0xf];
        }
        return new String(strArr);
    }

    /**
     * 16进制字符串转成字节数组
     *
     * @param s
     * @return
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 切换大小端序
     */
    public static byte[] changeBytes(byte[] a) {
        byte[] b = new byte[a.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = a[b.length - i - 1];
        }
        return b;
    }

    /**
     * 字节数组转16进制数
     *
     * @param b
     */
    private static void println_array(byte[] b) {
        for (byte x : b) {
            System.out.printf("%02x ", x);
        }
        System.out.println();
    }

    public static void main(String[] args) {

        String info = "你好你好1234";
        byte[] pnt = info.getBytes();
        byte[] enc = encrypt(pnt, Commkey);
        System.out.println("原文：" + info);
        println_array(pnt);
        System.out.println("秘钥:");
        println_array(Commkey);
        System.out.println();
        System.out.println("密文：");
        println_array(enc);
        System.out.println();
        byte[] dec = decrypt(enc, Commkey);
        System.out.println("解密后的数据：" + new String(dec));
        println_array(dec);

    }

}