package bizcompos;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import util.model.DbmsUtil;
import util.model.XmlUtil;
import entity.UModel;
import global.GVar;
import org.eclipse.wb.swt.SWTResourceManager;

public class DbmsDialog extends TitleAreaDialog {
	// jdbc:oracle:thin:@192.168.1.100:14523:xe
	private Text tx_url;
	private Text tx_user;
	private Text tx_passwd;

	private String url;
	private String user;
	private String passwd;
	private String schema;
	
	public UModel model = null;
	private Combo cb_schema;
	private Label lb_schema;
	private Button btn_ok;
	private Button btn_connect;
	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public DbmsDialog(Shell parentShell) {
		super(parentShell);
		setHelpAvailable(false);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitleImage(null);
		setMessage("连接数据库");
		setTitle("连接数据库");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FormLayout());
		GridData gd_container = new GridData(GridData.FILL_BOTH);
		gd_container.widthHint = 369;
		container.setLayoutData(gd_container);

		Label lb_url = new Label(container, SWT.NONE);
		FormData fd_lb_url = new FormData();
		fd_lb_url.top = new FormAttachment(0, 20);
		fd_lb_url.left = new FormAttachment(0, 15);
		lb_url.setLayoutData(fd_lb_url);
		lb_url.setText("JDBC连接地址：");

		tx_url = new Text(container, SWT.BORDER);
		FormData fd_tx_url = new FormData();
		fd_tx_url.bottom = new FormAttachment(lb_url, 2, SWT.BOTTOM);
		fd_tx_url.right = new FormAttachment(100, -30);
		fd_tx_url.left = new FormAttachment(lb_url, 0);
		tx_url.setLayoutData(fd_tx_url);

		Label lb_user = new Label(container, SWT.NONE);
		FormData fd_lb_user = new FormData();
		fd_lb_user.top = new FormAttachment(lb_url, 10);
		fd_lb_user.right = new FormAttachment(lb_url, 0, SWT.RIGHT);
		lb_user.setLayoutData(fd_lb_user);
		lb_user.setText("用户名：");

		tx_user = new Text(container, SWT.BORDER);
		FormData fd_tx_user = new FormData();
		fd_tx_user.right = new FormAttachment(tx_url, 0, SWT.RIGHT);
		fd_tx_user.bottom = new FormAttachment(lb_user, 2, SWT.BOTTOM);
		fd_tx_user.left = new FormAttachment(lb_user, 0);
		tx_user.setLayoutData(fd_tx_user);

		Label lb_passwd = new Label(container, SWT.NONE);
		FormData fd_lb_passwd = new FormData();
		fd_lb_passwd.top = new FormAttachment(lb_user, 10);
		fd_lb_passwd.right = new FormAttachment(lb_user, 0, SWT.RIGHT);
		lb_passwd.setLayoutData(fd_lb_passwd);
		lb_passwd.setText("密码：");

		tx_passwd = new Text(container, SWT.BORDER | SWT.PASSWORD);
		FormData fd_tx_passwd = new FormData();
		fd_tx_passwd.right = new FormAttachment(tx_user, 0, SWT.RIGHT);
		fd_tx_passwd.bottom = new FormAttachment(lb_passwd, 2, SWT.BOTTOM);
		fd_tx_passwd.left = new FormAttachment(lb_passwd, 0);
		tx_passwd.setLayoutData(fd_tx_passwd);

		btn_connect = new Button(container, SWT.NONE);
		btn_connect.setText("连接");
		btn_connect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell s = e.display.getActiveShell();
				url = tx_url.getText().trim();
				user = tx_user.getText().trim();
				passwd = tx_passwd.getText().trim();
				if (url.equals("")){
					MessageDialog.openError(s, "错误！", "数据库连接串不正确！");
					return;
				}
				s.setCursor(new Cursor(null,SWT.CURSOR_WAIT));
				ArrayList<String> al = DbmsUtil.GetSchemas(url, user, passwd);
				s.setCursor(new Cursor(null,SWT.NONE));
				if (al == null){
					MessageDialog.openError(s, "错误！", "连接数据库失败！");
					return;
				}
				lb_schema.setEnabled(true);
				String[] items = new String[al.size()];
				cb_schema.setItems((al.toArray(items)));
				cb_schema.setEnabled(true);
				for (int i=0;i<items.length;++i){
					if (items[i].equalsIgnoreCase((user))){
						cb_schema.select(i);
						btn_ok.setEnabled(true);
						schema = cb_schema.getText();
						break;
					}
				}
			}
		});
		FormData fd_btn_connect = new FormData();
		fd_btn_connect.top = new FormAttachment(tx_passwd, 6);
		fd_btn_connect.right = new FormAttachment(tx_url, 0, SWT.RIGHT);
		btn_connect.setLayoutData(fd_btn_connect);

		Label lb_sep = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_lb_sep = new FormData();
		fd_lb_sep.top = new FormAttachment(btn_connect, 6);
		fd_lb_sep.left = new FormAttachment(0, 0);
		fd_lb_sep.right = new FormAttachment(100, 0);
		lb_sep.setLayoutData(fd_lb_sep);

		lb_schema = new Label(container, SWT.NONE);
		lb_schema.setEnabled(false);
		FormData fd_lb_schema = new FormData();
		fd_lb_schema.top = new FormAttachment(0, 139);
		fd_lb_schema.left = new FormAttachment(0, 15);
		lb_schema.setLayoutData(fd_lb_schema);
		lb_schema.setText("选择数据库用户名：");

		cb_schema = new Combo(container, SWT.NONE);
		cb_schema.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btn_ok.setEnabled(true);
				schema = cb_schema.getText();
			}
		});
		cb_schema.setEnabled(false);
		FormData fd_cb_schema = new FormData();
		fd_cb_schema.right = new FormAttachment(tx_url, 0, SWT.RIGHT);
		fd_cb_schema.bottom = new FormAttachment(lb_schema, 2, SWT.BOTTOM);
		fd_cb_schema.left = new FormAttachment(lb_schema, 0);
		cb_schema.setLayoutData(fd_cb_schema);

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		btn_ok = createButton(parent, IDialogConstants.OK_ID, "确定", true);
		btn_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell s = e.display.getActiveShell();
//				String url = tx_url.getText().trim();
//				String user = tx_user.getText().trim();
//				String passwd = tx_passwd.getText().trim();
//				String schema = cb_schema.getText();
				s.setCursor(new Cursor(null,SWT.CURSOR_WAIT));
				model = DbmsUtil.OpenDBMS(url, user, passwd, schema);
				if (model == null){
					MessageDialog.openError(s, "错误！", "获取数据模型失败！");
					return ;
				}
				s.setCursor(new Cursor(null,SWT.NONE));
			}
		});
		btn_ok.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
		btn_connect.setFocus();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(412, 339);
	}
}
