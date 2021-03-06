/**
    Copyright (C) 2012 Pedro Henrique Lara Campos, Felipe Rodrigues Varjão

    This file is part of SQV.

    SQV is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    SQV is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with SQV.  If not, see <http://www.gnu.org/licenses/>.
 */

package projetotcc;

import java.sql.*;
import java.util.HashMap;

public class MainConfig {
	private static HashMap<String, Object> cache = new HashMap<String, Object>();
	
	public static void createBase(){
		MainConfig.createConfig("test", "okay", true);
		MainConfig.createConfig("downloadedtests", "", true);
	}
	
	public static Object getConfig(String var){
		if(cache.containsKey(var))
			if(cache.get(var) != null)
				return cache.get(var);
		try {
			Statement stat = Main.mainConn.createStatement();
			String query = "SELECT `value` from `" +
					Main.sqlConfigTable + "` where `label`=\""+  var + "\";";
			ResultSet result =  stat.executeQuery(query);
			if (result.isClosed()) return null;
			Object value = result.getString(1);
			stat.close();
			cache.put(var, value);
			return value;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected static boolean createConfig(String var, Object value){
		try {
			Statement stat = Main.mainConn.createStatement();
			String query = "INSERT INTO `" + Main.sqlConfigTable + "` VALUES(\"" + var + "\",\"" + value.toString() + "\");";
			stat.executeUpdate(query);
			cache.put(var, value);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	protected static int createConfig(String var, Object value, boolean onlyIfNotExists){
		if((onlyIfNotExists) & (getConfig(var)!=null))
			return 0;
		try {
			Statement stat = Main.mainConn.createStatement();
			String query = "INSERT INTO `" + Main.sqlConfigTable + "` VALUES(\"" + var + "\",\"" + value.toString() + "\");";
			stat.executeUpdate(query);
			cache.put(var, value);
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void setConfig(String var, Object value){
		if(getConfig(var)==null){
			createConfig(var, value);
			return;
		}
		try {
			Statement stat = Main.mainConn.createStatement();
			String query = "UPDATE `" + Main.sqlConfigTable + "` SET `value`=\"" + value.toString() + "\" WHERE `label`=\"" + var  + "\";";
			stat.executeUpdate(query);
			cache.put(var, value);
		} catch (SQLException e) {
			Main.showMessage("Dados não serão salvos.", "Erro");
			return;
		}
	}
	
	public static boolean isTestDownloaded(int id){
		String downloadedtests = (String) getConfig("downloadedtests");
		if(downloadedtests == null)
			return false;
		String sid = new String(String.valueOf(id));
		for(String test : downloadedtests.split("\1"))
			if(sid.equals(test))
				return true;
		return false;
	}
}
