//package mainClass;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.HashSet;
//
///**
// * @author Tom Qian
// * @email tomqianmaple@outlook.com
// * @github https://github.com/bluemapleman
// * @date 2017年4月11日
// */
//public class Debug
//{
//	public static void main(String[] args) throws FileNotFoundException
//	{
//		FileReader fr=new FileReader(new File("/Users/hanbo/Desktop/边城/work_space/TMallExpe/src/htmlText.txt"));
//		BufferedReader bufr=new BufferedReader(fr);
//		try
//		{
//			String line=bufr.readLine();
//			int index=-1;
//			String [] lines=new String[3000];
//			int count=0;
//			while((index=line.indexOf(')'))!=-1){
//				System.out.println(line.substring(0,index+1));
//				line=line.substring(index+1,line.length());
//				lines[count++]=line;
//			}
//			HashSet<String> set=new HashSet<String>();
//			for(int i=0;i<count-1;i++){
//				String id="";
//				if(!set.contains(id=lines[i].substring(3, 15)))
//					set.add(id);
//				else
//					System.out.println("重复id:"+id);
//			//map中包含：260条数据,id不重复的数据有：236
//			}
//			System.out.println(set.size());
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//}
