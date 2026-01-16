package test;


/**
 * @Description: 邮件
 * @ClassName: Mail
 * @auther RyanChou
 * @date 2013-4-12 
 * @time 下午2:14:28
 */
public class Mail {

    private Integer id;
    /**
     * 发件人
     */
    private String senderName;
    /**
     * 发件人邮箱
     */
    private String senderMail;
    /**
     * 发件人网址
     */
    private String senderWebSite;
    /**
     * 收件人
     */
    private String receiverName;
    /**
     * 收件人邮箱
     */
    private String receiverMail;
    /**
     * 内容
     */
    private String content;
    /**
     * 日期
     */
    private String date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderMail() {
        return senderMail;
    }

    public void setSenderMail(String senderMail) {
        this.senderMail = senderMail;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMail() {
        return receiverMail;
    }

    public void setReceiverMail(String receiverMail) {
        this.receiverMail = receiverMail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSenderWebSite() {
        return senderWebSite;
    }

    public void setSenderWebSite(String senderWebSite) {
        this.senderWebSite = senderWebSite;
    }
    
    public String testfunction(String str){
    	return "String is "+str;
    }
    public String testfunction(String str,String str2){
    	return "String is "+str + " and "+str2;
    }
    public boolean testboolean(){
    	return true;
    }

}