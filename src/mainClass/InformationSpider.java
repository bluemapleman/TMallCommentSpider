package mainClass;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author qianxinyao
 * @email tomqianmaple@gmail.com
 * @github https://github.com/bluemapleman
 * @date 2017年3月7日
 */
public class InformationSpider
{
	public static void main(String[] args){
		List<String> idList=DB.getItemIDList(0,100000000);
//		String mode="picture";
		String mode="comment";
		iterateCatch(idList,mode);
	}
	
	
	public static void iterateCatch(List<String> idList,String mode){
		int count=0;
		int catchSize=idList.size();
		for(String id:idList){
			int totalCommentNum=DB.getGoodCommentCount(id);
			totalCommentNum=totalCommentNum>2000?2000:totalCommentNum;
			System.out.println("此处抓取的是第"+(++count)+"个商品"+id+"还剩"+(catchSize-count)+"个商品待抓取");
			System.out.println("此处抓取的是:"+id+"它的评论数有：>"+totalCommentNum);
			String HTMLURL="https://rate.tmall.com/list_detail_rate.htm?itemId="+id+"&spuId=424778200&sellerId=619123122&order=3&currentPage=1";
			String tagHtml="https://rate.tmall.com/listTagClouds.htm?itemId="+id+"&isAll=true";
			if(mode.equals("comment")){
				System.out.println("有标签评论抓取开始！");
				Map commentTagMap=new InformationSpider().getTagComment(HTMLURL,tagHtml,id,mode);
				DB.insertComments(commentTagMap);
				System.out.println("有标签评论抓取+入库结束!");
				System.out.println("无标签评论抓取开始!");
				Map commentNoTagMap=new InformationSpider().getNoTagComment(HTMLURL,mode);
				DB.insertComments(commentNoTagMap);
				System.out.println("无标签评论抓取+入库结束!");
			}
			else if(mode.equals("picture")){
				System.out.println("有标签评论picture抓取开始！");
				Map commentTagMap=new InformationSpider().getTagComment(HTMLURL,tagHtml,id,mode);
				DB.insertPictures(commentTagMap);
				System.out.println("有标签评论picture抓取+入库结束!");
				System.out.println("无标签评论picture抓取开始!");
				Map commentNoTagMap=new InformationSpider().getNoTagComment(HTMLURL,mode);
				DB.insertPictures(commentNoTagMap);
				System.out.println("无标签评论picture抓取+入库结束!");
			}
			else{
				;
			}
			System.out.println("已抓取:"+(++count)+"个商品评论，还剩："+(catchSize-count));
			if((catchSize-count)<0)
				break;
		}
		
		
		
	}
	
	public Map getNoTagComment(String commentURL, String mode){
			String url=commentURL;
			Map<Integer,Map> commentMap=new HashMap<Integer,Map>();
			int pageNum=0;
			{
				Document doc;
				try
				{
					doc = Jsoup.connect(url).get();
					String html=doc.toString();
					if(html.indexOf("lastPage")!=-1)
						pageNum=Integer.parseInt(html.substring(html.indexOf("lastPage")+10, html.charAt(html.indexOf("lastPage")+11)==','?html.indexOf("lastPage")+11:html.indexOf("lastPage")+12));
					else pageNum=0;
					System.out.println(pageNum+"个页面待抓！");
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(int pageIndex=1;pageIndex<=pageNum+1;pageIndex++){
				String pageUrl=getIndexedPage(url,pageIndex);
				//第三个参数置null表示是抓取无标签的评论
				if(mode.equals("comment"))
					insertComments(commentMap, pageUrl, null);
				else if(mode.equals("picture"))
					insertPicturesInComments(commentMap, pageUrl, null);
				else{
					;
				}
			}
			return commentMap;
	}
	
    
	
	/**
	 * 给定初始商品的评论页面，抓取大概在给定数量左右的评论，但不能超过2000（TMall提供的评论数上限）
	 * @param mode 
	 * @param HTMLURL:形如"https://rate.tmall.com/list_detail_rate.htm?itemId=10670699278&spuId=268191144&sellerId=619123122&order=3&currentPage=1"
	 * @param num
	 */
	
	public Map getTagComment(String commentURL,String tagURL,String itemId, String mode){
		//},{"count":832,"id":"520","posi":true,"tag":"便宜","weight":0},{"
		//先获取所有的tag，再根据tag，去分别获得每个tag对应的标签
		Document doc=null;
		Map<Integer,Map> tagMap=new HashMap<Integer,Map>();
		HashMap<Integer,HashMap<String,String>> commentMap=new HashMap<Integer,HashMap<String,String>>();
		try
		{
			doc = Jsoup.connect(tagURL).get();
			String tagHtml=doc.toString();
			String regex="\\{\"count.*?\\}";
			Matcher tagMat=getMathcer(regex, tagHtml);
			int count=0;
			while(tagMat.find()){
				count++;
				HashMap<String,String> map=new HashMap<String,String>();
				map.put("id",getStringBetween(tagMat.group(),"id\":\"", "\",\"posi"));
				map.put("count", getStringBetween(tagMat.group(), "count\":", ",\"id"));
				map.put("content", getStringBetween(tagMat.group(), "tag\":\"", "\",\"weight"));
				tagMap.put(count, map);
			}
			//按照标签，逐个去取每个标签对应的所有评论数据
			Set<Integer> keys=tagMap.keySet();
			count=0;
			for(Integer key:keys){
				Map<String,String> tag=tagMap.get(key);
				String tagId=tag.get("id");
				int num=Integer.valueOf(tag.get("count"));
				System.out.println("tag contnet:"+tag.get("content"));
				System.out.println("tag id:"+tag.get("id"));
				String commentHtml="https://rate.tmall.com/list_detail_rate.htm?itemId="+itemId+"&sellerId=619123122&order=3&append=0&content=1&tagId="+tagId+"&posi=1&picture=0&currentPage=1";
//				https://rate.tmall.com/list_detail_rate.htm?itemId=533226198411&sellerId=619123122&order=3&append=0&content=1&tagId=1837&posi=1&picture=0&currentPage=22
				//按照标签给定的数量，抓取相应数目的评论
				int pageNum=num/20>100?100:num/20+1;
				System.out.println("PageNum:"+pageNum);
				for(int pageIndex=1;pageIndex<=pageNum;pageIndex++){
					String url=getIndexedPage(commentHtml, pageIndex);
					if(mode.equals("comment"))
						insertComments(commentMap, url,tagId);
					else if(mode.equals("picture"))
						insertPicturesInComments(commentMap, url, tagId);
					else{
						;
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return commentMap;
	}
	
	/**
	 * 给定URL和存储评论的List，返回抓取的评论数目
	 * @param commentList
	 * @param url
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int insertComments(Map commentMap,String url,String tagId){
		int num=0;
		try
		{
			Document doc=Jsoup.connect(url).get();
			String html=doc.toString();
			html=html.replaceAll("<b>","");
			html=html.replaceAll("</b>","");
			//关键去掉文本的换行符，否则正则匹配可能匹配不上
			html=html.replaceAll("\n","");
			html=html.replace("'","/");
			String regex=null;
			
			//评论是否带图片，有图片的会包含图片链接，没图片的为""
			regex="\"id\":.*?,\"pics\":\".*?\",\"picsSmall";
			Matcher picMat=getMathcer(regex, html);
			
			//评论内容
			regex="rateContent\":\".*?\",\"rateDate\"";
			Matcher contentMat = getMathcer(regex, html);
			
			//评论日期
			regex="rateDate\":\".*?\",\"reply";
			Matcher dateMat=getMathcer(regex, html);
			
			//评论id
			regex="id\":.*?,\"pics";
			Matcher idMat=getMathcer(regex, html);
			
			//"appendComment":{"commentId":"","commentTime":"2017-03-13 19:19:46","content":"东东棒棒的，下次再来购买",
			//追评
			regex="appendComment\":\\{.*?content\":\".*?\",\"days";
			Matcher appendMat=getMathcer(regex, html);
			
			while(contentMat.find() && dateMat.find() && idMat.find()){
				HashMap<String,String> map=new HashMap<String,String>();
				if(tagId!=null)
					map.put("tagId", tagId);
				else
					map.put("tagId", null);
				
				map.put("content",getStringBetween(contentMat.group(), "rateContent\":\"", "\",\"rateDate\""));
				map.put("date",getStringBetween(dateMat.group(), "rateDate\":\"","\",\"rep"));
				map.put("id",getStringBetween(idMat.group(), "id\":",",\"pics"));
				map.put("gid",getStringBetween(url, "itemId=", "&s"));
				
				
				//有追评
				if(appendMat.find()){
					map.put("appendDate", getStringBetween(appendMat.group(), "commentTime\":\"", "\",\"content"));
					map.put("appendContent", getStringBetween(appendMat.group(), "content\":\"", "\",\"days"));
				}
				else{
					map.put("appendContent", null);
					map.put("appendDate", null);
				}
				commentMap.put(commentMap.size()+1, map);
				num++;
			}
		}
		catch (IOException e)
		{
//			e.printStackTrace();
		}
		System.out.println("page:"+num);
		return num;
	}
	
	private int insertPicturesInComments(Map commentMap,String url,String tagId){
		int num=0;
		try
		{
			Document doc=Jsoup.connect(url).get();
			String html=doc.toString();
			html=html.replaceAll("<b>","");
			html=html.replaceAll("</b>","");
			//关键去掉文本的换行符，否则正则匹配可能匹配不上
			html=html.replaceAll("\n","");
			html=html.replace("'","/");
			String regex=null;
			
			//评论是否带图片，有图片的会包含图片链接，没图片的为""
			regex="\"id\":.*?,\"pics\":\".*?\",\"picsSmall";
			Matcher picMat=getMathcer(regex, html);
			
			//评论id
			regex="id\":.*?,\"pics";
			Matcher idMat=getMathcer(regex, html);
			
			
			while(idMat.find()){
				HashMap<String,String> map=new HashMap<String,String>();
				
				map.put("id",getStringBetween(idMat.group(), "id\":",",\"pics"));
				
				//评论中的图片
				if(picMat.find()){
					String pic=getStringBetween(picMat.group(),"pics\":",",\"picsSmall");
					if(pic.equals("\"\"")){
						map.put("picture",null);
					}else{
						map.put("picture",pic);
					}
				}
				
				commentMap.put(commentMap.size()+1, map);
				num++;
			}
		}
		catch (IOException e)
		{
//			e.printStackTrace();
		}
		System.out.println("page:"+num);
		return num;
	}
	
	private String getIndexedPage(String HTMLURL,int pageIndex){
		return HTMLURL.substring(0,HTMLURL.length()-1)+pageIndex;
	}

	
	public static String getStringBetween(String string,String start,String end){
		String result="";
		if(start==null){
			result=string.substring(0,string.indexOf(end));
		}
		else if(end==null){
			result=string.substring(string.indexOf(start)+start.length(),string.length());
		}
		else{
			result=string.substring(string.indexOf(start)+start.length(),string.indexOf(end));
		}
		return result;
	}
	
	
	
	private Matcher getMathcer(String regex,String html){
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(html);
	}
	
}

