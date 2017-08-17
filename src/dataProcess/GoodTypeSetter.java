//package dataProcess;
//
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.HashMap;
//import java.util.Set;
//
//import mainClass.DB;
//
///**
// * @author Tom Qian
// * @email tomqianmaple@outlook.com
// * @github https://github.com/bluemapleman
// * @date 2017年4月28日
// */
//public class GoodTypeSetter
//{
//	public static void main(String[] args)
//	{
//		HashMap<Integer,String[]> typeMap=new HashMap<Integer,String[]>();
//		//坚果特卖
//		String[] JianguoArr=new String[]{"夏威夷果","碧根果","腰果","巴旦木","松子","甘栗","开心果","豆","瓜子","花生","坚果","炒货"};
//		typeMap.put(1, JianguoArr);
//		//果干果脯
//		String[] GuoganArr=new String[]{"芒果干","榴莲干","葡萄干","草莓干","红枣","梅子","山楂","黄桃果干","蔓越莓干","果干","果脯"};
//		typeMap.put(2, GuoganArr);
//		//肉脯鱼干
//		String[] roufuArr=new String[]{"肉脯","猪","牛","鸡","鸭","鱼干","肠"};
//		typeMap.put(3, roufuArr);
//		//海味零食
//		String[] haiweiArr=new String[]{"豆干","鱿鱼","扇贝","海带","海苔","海味"};
//		typeMap.put(4, haiweiArr);
//		//糕点饼干
//		String[] gaodianArr=new String[]{"糕点","饼干","薯片","薯条","锅巴"};
//		typeMap.put(5, gaodianArr);
//		//甜蜜糖果
//		String[] tianmiArr=new String[]{"巧克力","棉花糖","果冻","布丁"};
//		typeMap.put(6, tianmiArr);
//		//冲饮茶类
//		String[] chongyinArr=new String[]{"冲饮","茶"};
//		typeMap.put(7, chongyinArr);
//		Statement stmt=null;
//		try
//		{
//			stmt=DB.getConnection().createStatement();
//			ResultSet rs=stmt.executeQuery("select * from goods");
//			int count=0;
//			while(rs.next()){
//				int type=0;
//				String name=rs.getString(2);
//				System.out.println(name);
//				String id=rs.getString(1);
//				Set<Integer> keys=typeMap.keySet();
//				for(Integer key:keys){
//					String[] keywords=typeMap.get(key);
//					for(String keyword:keywords){
//						if(name.contains(keyword)){
//							type=key;
//							break;
//						}
//					}
//					if(type!=0)
//						break;
//				}
//				DB.getConnection().createStatement().executeUpdate("update goods set gtype="+type+" where gid='"+id+"'");
//				System.out.println(++count);
//			}
//		}
//		catch (SQLException e)
//		{
//			e.printStackTrace();
//		}
//		finally{
//			try
//			{
//				stmt.close();
//			}
//			catch (SQLException e)
//			{
//				e.printStackTrace();
//			}
//		}
//		
//	}
//}
//
