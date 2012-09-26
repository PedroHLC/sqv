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

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Vector;

public class HeavyTest implements Test {
	private Vector<Question> questions;
	
	@Override
	public Question getQuestion(int id){
		return questions.get(id);
	}
	
	@Override
	public Integer getQuestionsNum(){
		return questions.size();
	}
	
	public HeavyTest(String db) throws SQLException{
		questions = new Vector<Question>();
		Connection sqlConn = Main.createSQLConn(Main.sqlTestsDBFolder + db);
		Statement stat = sqlConn.createStatement();
		String query = "SELECT * from `" + Main.sqlTestsQuestionsTable + "`;";
		ResultSet result =  stat.executeQuery(query);
		while(result.next()){
			questions.add(new Question(result));
		}
		stat.close();
		sqlConn.close();
	}
}
