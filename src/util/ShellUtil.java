package util;

import java.awt.Toolkit;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

public class ShellUtil {
	public static Point locationCenter(Shell parent, Shell child) {
		int px = parent.getLocation().x;
		int py = parent.getLocation().y;
		int pw = parent.getSize().x;
		int ph = parent.getSize().y;

		// 获取对象窗口高度和宽度
		int ch = child.getBounds().height;
		int cw = child.getBounds().width;

		// 如果对象窗口高度超出屏幕高度，则强制其与屏幕等高
		if (ch > ph)
			ch = ph;

		// 如果对象窗口宽度超出屏幕宽度，则强制其与屏幕等宽
		if (cw > pw)
			cw = pw;
		
		return new Point( px + (pw - cw) / 2, py + ((ph - ch) / 2));
	}

	/**
	 * 设置窗口位于屏幕中间
	 * 
	 * @param shell
	 *            要调整位置的窗口对象
	 */
	public static Point locationCenter(Shell shell) {
		// 获取屏幕高度和宽度
		int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
		// 获取对象窗口高度和宽度
		int shellH = shell.getBounds().height;
		int shellW = shell.getBounds().width;

		// 如果对象窗口高度超出屏幕高度，则强制其与屏幕等高
		if (shellH > screenH)
			shellH = screenH;

		// 如果对象窗口宽度超出屏幕宽度，则强制其与屏幕等宽
		if (shellW > screenW)
			shellW = screenW;

		// 定位对象窗口坐标
		return new Point(((screenW - shellW) / 2), ((screenH - shellH) / 2));
	}
}
