package top.tomqian;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysql.jdbc.Connection;

/**
 * @author qianxinyao
 * @email tomqianmaple@gmail.com|tomqianmaple@outlook.com
 * @github https://github.com/bluemapleman
 * @date 2017年3月6日
 */

public class DB
{
	private static Connection conn=null;
	private static String DBConfigFileName=null;
	public static Connection getConnection(String configFileName){
		if(conn==null){
			try
			{
				PropertyKit prop=new PropertyKit(configFileName);
				String url=prop.getProperty("url")+prop.getProperty("database")+"?user="+prop.getProperty("user")+
						"&password="+prop.getProperty("password")+prop.getProperty("coding");
				Class.forName("com.mysql.jdbc.Driver");
				conn=(Connection) DriverManager.getConnection(url);
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return conn;
	}
	public static Connection getConnection(){
		if(conn==null){
			return getConnection(DB.DBConfigFileName);
		}
		return conn;
	}
	
	public static int insertComments(Map<Integer,Map<String,String>> commentMap){
		HashSet<String> idSet=new HashSet<String>();
		try
		{
			Statement stmt=getConnection().createStatement();
			String insertContents="insert ignore into comments values ";
			Set<Integer> keys=commentMap.keySet();
			for(Integer key:keys){
				ArrayList<String> list=new ArrayList<String>();
				Map<String,String> map=commentMap.get(key);
				list.add(map.get("id"));
				if(map.get("content").equals("此用户没有填写评论!")){
					continue;
				}
				list.add(map.get("content"));
				list.add(map.get("date"));
//				list.add(map.get("tagId"));
				list.add(map.get("appendContent"));
				list.add(map.get("appendDate"));
				list.add(map.get("gid"));			
				String insertAddition=ensembleInsertObject(list,insertContents);
				if(insertAddition.equals("()"))
					continue;
				insertContents+=insertAddition+",";
			}
			String sql=insertContents.substring(0,insertContents.length()-1);
			for(Integer key:commentMap.keySet()){
				idSet.add(commentMap.get(key).get("id"));
			}
//			System.out.println("map中包含："+commentMap.size()+"条数据,id不重复的数据有："+idSet.size());
//			int num=0;
			if(!sql.equals("insert ignore into comments values"))
				stmt.executeUpdate(sql);
//			System.out.println("此商品共插入"+num+"条数据！");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		  
		return idSet.size();
	}
	
	public static void getCommentsBetween(){
		String sql="select count(*) from comments where ctime>='2016-12-01' and ctime<='2016-12-31'";
		Statement stmt;
		try
		{
			stmt = getConnection().createStatement();
			stmt.executeQuery(sql);
			System.out.println();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return;
	}
	

	public static List<String> getItemIDList(int lowerBound,int upperBound){
		String sql="select goods.gid from goods where gcommentcount<"+upperBound +" and gcommentcount>"+lowerBound+" ORDER BY RAND()";
		List<String> list=new ArrayList<String>();
		Statement stmt;
		try
		{
			stmt = getConnection().createStatement();
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()){
				list.add(rs.getString(1));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return list;
	}
	
	public static String ensembleInsertObject(List<String> list,String formerInsert){
//		if(!formerInsert.contains(list.get(0))){
			String returnStr="(";
			for(String str:list){
				if(str==null)
					returnStr+=str+",";
				else
					returnStr+="'"+str+"',";
				
			}
			returnStr=returnStr.substring(0, returnStr.length()-1);
			returnStr+=")";
			return returnStr;
//		}
//		else{
//			return "()";
//		}
	}
	
	public static int getGoodCommentCount(String id){
		String sql="select gcommentcount from goods where gid='"+id+"'";
		Statement stmt;
		int num=0;
		try
		{
			stmt = getConnection().createStatement();
			ResultSet rs=stmt.executeQuery(sql);
			if(rs.next())
				num=rs.getInt(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return num;
	}

	/**
	 * @param commentMap
	 */
	public static int insertPictures(Map<Integer,Map<String,String>> commentMap)
	{
		HashSet<String> idSet=new HashSet<String>();
		try
		{
			Statement stmt=getConnection().createStatement();
			Set<Integer> keys=commentMap.keySet();
			stmt = getConnection().createStatement();
			for(Integer key:keys){
				Map<String,String> map=commentMap.get(key);
				String id=map.get("id");
				idSet.add(id);
				String pic=map.get("picture");
				String sql=null;
				if(pic==null){
					sql="update comments set picture=null where cid='"+id+"'";
					stmt.executeUpdate(sql);
				}
				else{
					sql="update comments set picture='"+pic+"' where cid='"+id+"'";
				    stmt.executeUpdate(sql);
				}
				System.out.println(sql);
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		  
		return idSet.size();
		
	}
	/**
	 * 
	 */
	public static void initialize(String DBConfigFileName)
	{
		DB.DBConfigFileName=DBConfigFileName;
	}
}