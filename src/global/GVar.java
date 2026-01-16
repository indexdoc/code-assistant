package global;


import entity.TplSet;
import entity.UModel;
import main.Win;
import util.Config;

public class GVar {
	static public UModel gModel = new UModel();
	static public TplSet  gTplSet = null;
	static public Win gWin = null;
	static public boolean isTrial = true;
	static public Config config = null;
}
