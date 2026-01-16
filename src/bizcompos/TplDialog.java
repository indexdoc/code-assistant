package bizcompos;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import entity.Tpl;
import org.eclipse.wb.swt.SWTResourceManager;

public class TplDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	public TplForm tplForm;
	private String state;
	private String workPath;
	private Tpl tpl;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 * @param workPath 
	 */
	public TplDialog(Shell parent, int style, String state, String workpath) {
		super(parent, style);
		if (state == TplForm.EDIT){
			setText("编辑模板文件");
		}
		else {
			setText("新建模板文件");
		}
		this.workPath= workpath;
		this.tpl=null;
		this.state = state;
	}
	/**
	 * @wbp.parser.constructor
	 */
	public TplDialog(Shell parent, int style, String state, String workpath,Tpl t) {
		super(parent, style);
		if (state == TplForm.EDIT){
			setText("编辑模板文件");
		}
		else {
			setText("新建模板文件");
		}
		this.workPath= workpath;
		this.tpl = t;
		this.state = state;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.setLocation(util.ShellUtil.locationCenter(getParent(),shell));
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	public Tpl getTpl(){
		if (tplForm!=null)
			return tplForm.getTpl();
		return null;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shell.setImage(SWTResourceManager.getImage(TplDialog.class, "/res/128.ico"));
		shell.setMinimumSize(new Point(600, 400));
		shell.setSize(900, 600);
		shell.setText(getText());
		shell.setLayout(new FormLayout());
		
		tplForm = new TplForm(shell, SWT.NONE, state, workPath,tpl);
		FormData fd_tplForm = new FormData();
		fd_tplForm.bottom = new FormAttachment(100);
		fd_tplForm.right = new FormAttachment(100);
		fd_tplForm.top = new FormAttachment(0);
		fd_tplForm.left = new FormAttachment(0);
		tplForm.setLayoutData(fd_tplForm);

	}
}
