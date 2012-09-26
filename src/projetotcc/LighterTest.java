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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LighterTest implements Test {
	private Connection sqlConn;
	
	public LighterTest(String db) throws SQLException{
		sqlConn = Main.createSQLConn(Main.sqlTestsDBFolder + db);
	}
	
	@Override
	public Question getQuestion(int id){
		Question returnv = null;
		try{
			Statement stat = sqlConn.createStatement();
			String query = "SELECT * from `" + Main.sqlTestsQuestionsTable + "` WHERE `id`="+id+";";
			ResultSet result =  stat.executeQuery(query);
			while(result.next()){
				returnv = new Question(result);
			}
			stat.close();
		} catch(Exception e) {}
		return returnv;
	}
	
	public void close(){
		try {
			sqlConn.close();
		} catch (SQLException e) {}
	}

	@Override
	public Integer getQuestionsNum() {
		Integer returnv = null;
		try{
			Statement stat = sqlConn.createStatement();
			String query = "SELECT COUNT(*) AS `num` FROM `" + Main.sqlTestsQuestionsTable + "`;";
			ResultSet result =  stat.executeQuery(query);
			while(result.next()){
				returnv = result.getInt(1);
			}
			stat.close();
			return returnv;
		} catch(SQLException e) {return 0;}
	}
}
