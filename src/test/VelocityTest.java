package test;


import java.io.FileReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import util.parse.TplFun;


/**
 * @Description: 测试类
 * @ClassName: VelocityTest
 * @auther RyanChou
 * @date 2013-4-12 
 * @time 下午2:13:53
 */
public class VelocityTest {

    public static final String HELLO_WORLD_VM_PATH = "src\\test\\helloWorld.vtl";
    public static final String USER_INFO_VM_PATH = "userInfo.vm";
    public static final String EMAIL_TEMPLATE_VM_PATH = "src\\test\\emailTemplate.vm";

    public static void main(String[] args) throws Exception {
        sayHelloFromVM(HELLO_WORLD_VM_PATH);
//        testUser(USER_INFO_VM_PATH);
//        testEmail(EMAIL_TEMPLATE_VM_PATH);
    }

    /**
     * 简单的hello world
     * 
     * @param fileVM
     * @throws Exception
     */
    public static void sayHelloFromVM(String fileVM) throws Exception {
        VelocityContext context = new VelocityContext();
        
        //3. 往上下文中添加参数, hello参数的值为Hello world!
        context.put("hello", "Hello world");
        Mail mail = new Mail();
        mail.setContent("2013年腾讯开发者新扶持政策解读及创业机会所在");
        mail.setReceiverMail("hongtenzone@foxmail.com");
        mail.setReceiverName("Hongten");
        mail.setSenderMail("opensns_noreply@tencent.com");
        mail.setSenderName("TENCENT_OPEN_PLATFORM");
        mail.setSenderWebSite("open.qq.com");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mail.setDate(simpleDateFormat.format(new Date()));
        TplFun fun = new TplFun();
        context.put("fun", fun);
        context.put("mail", mail);

        //4. 实例化StringWriter对象,将上下文与字符流进行合并到模板中
        StringWriter sw = new StringWriter();

        FileReader fr = new FileReader(fileVM);
        
		Velocity.evaluate(context, sw, "log", fr);
        
        //5. 字符流中的字符串
        System.out.println(sw.toString());
		
		sw.close();
		
		fr.close();
    }

    /**
     * test User
     * 
     * @param fileVM
     * @throws Exception
     */
    public static void testUser(String fileVM) throws Exception {
//        VelocityEngine ve = new VelocityEngine();
//        ve.init();
//
//        Template template = ve.getTemplate(fileVM);
//        VelocityContext velocityContext = new VelocityContext();
//        
//        User user = new User();
//        user.setEmail("hongtenzone@foxmail.com");
//        user.setName("hongten");
//        user.setBirthday("1990-11-18");
//        velocityContext.put("user", user);
//        
//        StringWriter stringWriter = new StringWriter();
//        template.merge(velocityContext, stringWriter);
//
//        System.out.println(stringWriter.toString());
    }

    /**
     * test email
     * 
     * @param fileVM
     * @throws Exception
     */
    public static void testEmail(String fileVM) throws Exception {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();

        Template template = velocityEngine.getTemplate(fileVM);
        VelocityContext velocityContext = new VelocityContext();
        
        Mail mail = new Mail();
        mail.setContent("2013年腾讯开发者新扶持政策解读及创业机会所在");
        mail.setReceiverMail("hongtenzone@foxmail.com");
        mail.setReceiverName("Hongten");
        mail.setSenderMail("opensns_noreply@tencent.com");
        mail.setSenderName("TENCENT_OPEN_PLATFORM");
        mail.setSenderWebSite("open.qq.com");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mail.setDate(simpleDateFormat.format(new Date()));
        TplFun fun = new TplFun();
        velocityContext.put("fun", fun);
        velocityContext.put("mail", mail);
        
        StringWriter stringWriter = new StringWriter();
        template.merge(velocityContext, stringWriter);

        System.out.println(stringWriter.toString());
    }
}