//package mainClass;
//import java.io.IOException;
//
//import org.xml.sax.SAXException;
//
//import com.meterware.httpunit.GetMethodWebRequest;
//import com.meterware.httpunit.WebConversation;
//import com.meterware.httpunit.WebRequest;
//import com.meterware.httpunit.WebResponse;
//
///**
// * @author Tom Qian
// * @email tomqianmaple@outlook.com
// * @github https://github.com/bluemapleman
// * @date 2017年3月11日
// */
//public class HttpUnitTest
//{
//	   public void testUserHttpUnit() throws IOException, SAXException{  
//		   System.out.println("使用Get方式向服务器发送数据，然后获取网页内容：");
////		   String url="https://zhidao.baidu.com/question/1639064102332262540.html?qbl=relate_question_0&word=%D2%B3%C3%E6%B7%C3%CE%CA403";
//		   String url="https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14632554911.121.iF2jwX&id=40495942405";
//	        // 建立一个WebConversation实例  
//	        WebConversation wc = new WebConversation();  
//	        // 向指定的URL发出请求  
//	        WebRequest req = new GetMethodWebRequest(url);  
//	        // 获取响应对象  
//	        WebResponse resp = wc.getResponse(req);  
//	  
//	        // 用getText方法获取相应的全部内容  
//	        // 用System.out.println将获取的内容打印在控制台上  
//	        System.out.println(resp.getText());
//	   }  
//	   
//	public static void main(String[] args) throws IOException, SAXException
//	{
//		new HttpUnitTest().testUserHttpUnit();
//	}
//}
//
