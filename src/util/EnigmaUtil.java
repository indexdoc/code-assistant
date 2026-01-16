package util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class EnigmaUtil {
	// 定义接口CLibrary，继承自com.sun.jna.Library
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量
		CLibrary Instance = (CLibrary) Native.loadLibrary("ide.dll",CLibrary.class);
		boolean EP_RegCheckKey(String Name, String Key);
		boolean EP_RegSaveKey(String Name, String Key);
		int EP_RegKeyDaysLeft();

		boolean EP_RegLoadKey(PointerByReference prName,PointerByReference prKey);
	}
	
	private static String name = null;
	private static String key = null;
	
	public static String RegLoadKey_Name(){
		if (name != null)
			return name;
		PointerByReference  prName = new PointerByReference();
		PointerByReference  prKey = new PointerByReference();
		if (!CLibrary.Instance.EP_RegLoadKey(prName, prKey))
			return null;
		Pointer pName = prName.getPointer();
		Pointer pKey = prKey.getPointer();
		name = pName.getString(0);
		key = pKey.getString(0);
		return name;
	}
	public static String RegLoadKey_Key(){
		if (key != null)
			return key;
		PointerByReference  prName = new PointerByReference();
		PointerByReference  prKey = new PointerByReference();
		if (!CLibrary.Instance.EP_RegLoadKey(prName, prKey))
			return null;
		Pointer pName = prName.getPointer();
		Pointer pKey = prKey.getPointer();
		name = pName.getString(0);
		key = pKey.getString(0);
		return key;
	}
	// 检查key是否有效
	public static boolean RegCheckKey(String name, String key) {
		return CLibrary.Instance.EP_RegCheckKey(name, key);
	}

	// 保存key
	public static boolean RegSaveKey(String name, String key) {
		return CLibrary.Instance.EP_RegSaveKey(name, key);
	}

	// 得到过期时间
	public static String RegKeyExpirationDate() {
		return null;
	}

	// 得到还剩多少天过期
	public static int RegKeyDaysLeft() {
		return CLibrary.Instance.EP_RegKeyDaysLeft();
	}
}
