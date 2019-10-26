package com.tcwl.caculator;

/**
 * Tea算法 每次操作可以处理8个字节数据 KEY为16字节,应为包含4个int型数的int[]，一个int为4个字节
 * 加密解密轮数应为8的倍数，推荐加密轮数为64轮
 * */
public class SunQQ {
	// 加密解密所用的KEY
	private final static long[] KEY = new long[] { 0x30, 0x31, 0x32, 0x33,
			0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62, 0x63, 0x64, 0x65,
			0x66 };

	// 加密
	public static byte[] encrypt(byte[] content, int offset, long[] key2,
								 int times) {// times为加密轮数
		int[] tempInt = byteToInt(content, offset);
		int y = tempInt[0], z = tempInt[1], sum = 0, i;
		int delta = 0x9e3779b9; // 这是算法标准给的值
		long a = key2[0], b = key2[1], c = key2[2], d = key2[3];

		for (i = 0; i < times; i++) {

			sum += delta;
			y += ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
			z += ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
		}
		tempInt[0] = y;
		tempInt[1] = z;
		return intToByte(tempInt, 0);
	}

	// 解密
	public static byte[] decrypt(byte[] encryptContent, int offset, long[] key,
								 int times) {
		int[] tempInt = byteToInt(encryptContent, offset);
		int y = tempInt[0], z = tempInt[1], sum = 0, i;
		int delta = 0x9e3779b9; // 这是算法标准给的值
		long a = key[0], b = key[1], c = key[2], d = key[3];
		if (times == 32)
			sum = 0xC6EF3720; /* delta << 5 */
		else if (times == 16)
			sum = 0xE3779B90; /* delta << 4 */
		else
			sum = delta * times;

		for (i = 0; i < times; i++) {
			z -= ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
			y -= ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
			sum -= delta;
		}
		tempInt[0] = y;
		tempInt[1] = z;

		return intToByte(tempInt, 0);
	}

	// byte[]型数据转成int[]型数据
	private static int[] byteToInt(byte[] content, int offset) {

		int[] result = new int[content.length >> 2];// 除以2的n次方 == 右移n位 即
		// content.length / 4 ==
		// content.length >> 2
		for (int i = 0, j = offset; j < content.length; i++, j += 4) {
			result[i] = transform(content[j + 3])
					| transform(content[j + 2]) << 8
					| transform(content[j + 1]) << 16 | (int) content[j] << 24;
		}
		return result;

	}

	// int[]型数据转成byte[]型数据
	private static byte[] intToByte(int[] content, int offset) {
		byte[] result = new byte[content.length << 2];// 乘以2的n次方 == 左移n位 即
		// content.length * 4 ==
		// content.length << 2
		for (int i = 0, j = offset; j < result.length; i++, j += 4) {
			result[j + 3] = (byte) (content[i] & 0xff);
			result[j + 2] = (byte) ((content[i] >> 8) & 0xff);
			result[j + 1] = (byte) ((content[i] >> 16) & 0xff);
			result[j] = (byte) ((content[i] >> 24) & 0xff);
		}
		return result;
	}

	// 若某字节为负数则需将其转成无符号正数
	private static int transform(byte temp) {
		int tempInt = (int) temp;
		if (tempInt < 0) {
			tempInt += 256;
		}
		return tempInt;
	}

	// 通过TEA算法加密信息
	public static byte[] encryptByTea(String info) {
		byte[] temp = info.getBytes();
		int n = 0;// 若temp的位数不足8的倍数,需要填充的位数
		byte[] encryptStr = new byte[temp.length + n];
		encryptStr[0] = (byte) n;
		System.arraycopy(temp, 0, encryptStr, n, temp.length);
		byte[] result = new byte[encryptStr.length];
		for (int offset = 0; offset < result.length; offset += 8) {
			byte[] tempEncrpt = encrypt(encryptStr, offset, KEY, 32);
			System.arraycopy(tempEncrpt, 0, result, offset, 8);
		}
		return result;
	}

	// 通过TEA算法解密信息
	public static String decryptByTea(byte[] secretInfo) {
		byte[] decryptStr = null;
		byte[] tempDecrypt = new byte[secretInfo.length];
		for (int offset = 0; offset < secretInfo.length; offset += 8) {
			decryptStr = decrypt(secretInfo, offset, KEY, 32);
			System.arraycopy(decryptStr, 0, tempDecrypt, offset, 8);
		}

		int n = 0;
		return new String(tempDecrypt, n, decryptStr.length - n);

	}

	// 字节数组转成16进制字符串
	private static final char HexCharArr[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String byteArrToHex(byte[] btArr) {
		char strArr[] = new char[btArr.length * 2];
		int i = 0;
		for (byte bt : btArr) {
			strArr[i++] = HexCharArr[bt >>> 4 & 0xf];
			strArr[i++] = HexCharArr[bt & 0xf];
		}
		return new String(strArr);
	}

	// 16进制字符串转成字节数组
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

	public static void main(String[] args) {
		String info = "--Beijing TCHF IoT Technology Co.,Ltd.--";
		System.out.println("原数据：" + info);
		for (byte i : info.getBytes()) {
			System.out.print(String.format("%02x ", new Integer(i & 0xff)));
		}
		byte[] encryptInfo = encryptByTea(info);
		// 99 0b 91 61 a8 af 81 e6
		System.out.println();
		System.out.println("加密后的数据：");
//		byte[] changeBytes = changeBytes(encryptInfo);
		for (byte i : encryptInfo) {
			System.out.print(String.format("%02X ", new Integer(i & 0xff)));
		}
		System.out.println();
		String decryptByTea = decryptByTea(encryptInfo);
		System.out.println("解密后的数据：" + decryptByTea);
		for (byte i : decryptByTea.getBytes()) {
			System.out.print(String.format("%02x ", new Integer(i & 0xff)));
		}

	}
}
