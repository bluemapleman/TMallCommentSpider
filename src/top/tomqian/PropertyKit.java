package top.tomqian;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Tom Qian
 * @email tomqianmaple@outlook.com
 * @github https://github.com/bluemapleman
 * @date 2017年8月18日
 */
public class PropertyKit
{
	Properties prop;
	public PropertyKit(String configFileName)
	{
		try
		{
			prop=new Properties();
			prop.load(new FileInputStream(System.getProperty("user.dir") + "/res/" + configFileName + ".properties"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getProperty(String key){
		return prop.getProperty(key);
	}
	
}

