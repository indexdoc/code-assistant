package test;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import bizcompos.TplForm;

public class CreateOwnDialogExample {
	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);

		shell.setSize(400, 100);
		shell.setLayout(new FillLayout());

		Button b1 = new Button(shell, SWT.PUSH);
		b1.setText("Open mail Dialog ...");
		b1.pack();
		b1.setLocation(50, 50);
		b1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent se) {

//				MailDialog dial = new MailDialog(shell, new String[] { "mail",	"gan" });
				Dialog dial = new CenterStorageManagement3(shell);
				dial.open();
			}
		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

class MailDialog extends TitleAreaDialog {
	public static final int OPEN = 9999;

	public static final int DELETE = 9998;

	List list;

	String[] items;

	String[] itemsToOpen;

	public MailDialog(Shell shell, String[] items) {
		super(shell);
		this.items = items;
	}

	public void create() {
		super.create();
		setTitle("Mail");
		setMessage("You have mail! \n It could be vital for this evening...");
	}

	// 重写createDialogArea方法，创建对话框区域
	protected Control createDialogArea(Composite parent) {
		// 添加对话框区域的父面板
		final Composite area = new TplForm(parent, SWT.NULL,TplForm.NEW,"",null);
		// final GridLayout gridLayout = new GridLayout();
		// gridLayout.marginWidth = 15;
		// gridLayout.marginHeight = 10;
		// // 设置父面板的布局方式
		// area.setLayout(gridLayout);
		// // 在对话框区域中添加List组件
		// list = new List(area, SWT.BORDER | SWT.MULTI);
		// final GridData gridData = new GridData();
		// gridData.widthHint = 200;
		// list.setLayoutData(gridData);
		// list.addSelectionListener(new SelectionAdapter() {
		// public void widgetSelected(SelectionEvent e) {
		// validate();
		// }
		// });
		// for (int i = 0; i < items.length; i++) {
		// list.add(items[i]);
		// }
		return area;
	}

	// 添加自定义对话框的校验方法
	private void validate() {
		boolean selected = (list.getSelectionCount() > 0);
		getButton(OPEN).setEnabled(selected);
		getButton(DELETE).setEnabled(selected);
		if (!selected)
			setErrorMessage("Select at least one entry!");
		else
			setErrorMessage(null);
	}

	// 重写Dialog的createButtonsForButtonBar方法创建对话框按钮
	protected void createButtonsForButtonBar(Composite parent) {
		// 添加“Open”按钮
		Button openButton = createButton(parent, OPEN, "Open", true);
		// 初始状态为禁用
		openButton.setEnabled(false);
		// 为“Open”按钮添加事件
		openButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				itemsToOpen = list.getSelection();
				setReturnCode(OPEN);
				close();
			}
		});
		// 添加“Delete”按钮
		Button deleteButton = createButton(parent, DELETE, "Delete", false);
		deleteButton.setEnabled(false);
		// 为“Delete”按钮添加事件
		deleteButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int selectedItems[] = list.getSelectionIndices();
				list.remove(selectedItems);
				// 校验当前数据
				validate();
			}
		});
		Button cancelButton = createButton(parent, CANCEL, "Cancel", false);
		// 添加“Cancel”按钮
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	public String[] getItemsToOpen() {
		return itemsToOpen;
	}

}

class CenterStorageManagement3 extends Dialog {
	protected CenterStorageManagement3(Shell parentShell) {
		super(parentShell);
	}

	public static final String ID = "cn.net.easyway.arts.client.om.tb.editors.CenterStorageManagement3";

	protected Control createDialogArea(Composite parent) {
		Composite shell = (Composite) super.createDialogArea(parent);
		return shell;
	}

	// 设置对话框初始大小
	protected Point getInitialSize() {
		return new Point(600, 400);
	}

	// 设置对话框窗口标题和图标
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("区域中心票卷入库管理");
	}

	protected Button createButton(Composite parent, int id, String label,
			boolean defaultButton) {
		return null;
	}

	// 创建对话框按钮
	protected void createButtonsForButtonBar(final Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
	}
}
