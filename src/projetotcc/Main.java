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

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Main {

	protected static JFrame visual = null;
	protected static final String webSQVNormalAddr = "http://dl.dropbox.com/u/44637513/sqv/";
	protected static final String webSQVBackupAddr = "http://www.pedrohlc.com/sqv/";
	protected static String webSQVAddr = webSQVNormalAddr;
	protected static final String webSQVOfflineAddr =  "http://0.0.0.0/";
	protected static final String webSQVMinVersion = "minv.txt";
	protected static final String localSQVMinVersion = "minv.txt";
	protected static final String sqlConnector = "org.sqlite.JDBC";
	protected static final String sqlPrefix = "jdbc:sqlite:";
	protected static final String sqlSufix = ".dat";
	protected static final String sqlMainDB = "maindb";
	protected static final String sqlConfigTable = "config";
	protected static final String sqlTestsListDBWeb = "testslistdb.dat";
	protected static final String sqlTestsListDB = "testslistdb";
	protected static final String sqlHistoryDB = "historydb";
	protected static final String sqlTestsDBFolderWeb = "tests/";
	protected static final String sqlTestsDBFolder = "tests_";
	protected static final String sqlTestsQuestionsTable = "questoes";
	protected static final String sqlTestsTable = "list";
	protected static final String dwlSuffix = ".part";
	protected static final String titleError = "Um erro ocorreu no SQV";
	protected static String dataFolder = getHomePath() + "/.sqv/";
	protected static Connection mainConn = null;
	protected static final int version = 1;
	
	
	public static Runnable checkVersionAndConnection = new Runnable(){
		@Override
		public void run(){
			try {
				System.out.println("Tentando link: " + (webSQVNormalAddr + webSQVMinVersion));
				FileDownloader.instantDownloadFile(webSQVNormalAddr + webSQVMinVersion, dataFolder + localSQVMinVersion); //*WEB
				Main.webSQVAddr = webSQVNormalAddr;
			} catch (Exception e) {
				try{
					System.out.println("Tentando link reserva: " + (webSQVBackupAddr + webSQVMinVersion));
					FileDownloader.instantDownloadFile(webSQVBackupAddr + webSQVMinVersion, dataFolder + localSQVMinVersion); //*WEB
					Main.webSQVAddr = webSQVBackupAddr;
				} catch (Exception e2) {
					System.out.println("Vai ser offline mesmo :P");
					Main.webSQVAddr = webSQVOfflineAddr;
				}
			}
			String minversion = fileToString(dataFolder + localSQVMinVersion);
			if(minversion != null)
				if(new Integer(minversion) > version){
					showMessage("É necessário atualizar o aplicativo para novas provas.", "Aviso");
					Main.webSQVAddr = webSQVOfflineAddr;
			}
		}
	};
	
	public static String fileToString(String file) {
        String result = null;
        DataInputStream in = null;

        try {
            File f = new File(file);
            byte[] buffer = new byte[(int) f.length()];
            in = new DataInputStream(new FileInputStream(f));
            in.readFully(buffer);
            result = new String(buffer);
        } catch (IOException e) {
        } finally {
            try {
            	if(in != null)
            		in.close();
            } catch (IOException e) {}
        }
        
        return result;
    }
	
	public static void showMessage(String content, String title){
		System.out.println(title + ": " + content);
		JOptionPane.showMessageDialog(Main.visual, content);
	}
	
	public static String getDBSQLPath(String db){	return sqlPrefix + dataFolder + db + sqlSufix; }
	public static String getDBFilePath(String db){	return dataFolder + db + sqlSufix; }
	
	protected static Connection createSQLConn(String db){
		try {
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection(getDBSQLPath(db));
		} catch (SQLException e) {
			showMessage("Database \"" + db + "\" corrompida.", Main.titleError);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			showMessage("Um item do aplicativo não pode ser encontrado.\n" +
					"Reinstale o aplicativo e tente novamente.", Main.titleError);
			e.printStackTrace();
			
		}
		return null;
	}
	
	private static void createMainConn() {
		mainConn = createSQLConn(sqlMainDB);
		try {
			Statement stat = mainConn.createStatement();
			stat.executeUpdate("" +
					"CREATE TABLE IF NOT EXISTS config(label char(32) NOT NULL, value char(1024) NOT NULL, PRIMARY KEY(label));");
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		MainConfig.createBase();
	}
	
	private static void createDataFolder() {
		File dataFolderFile = new File(dataFolder);
		if(!dataFolderFile.exists())
			dataFolderFile.mkdir();
	}
	
	private static boolean copyFile(String sourcePath, String newPath, boolean sourceDelete){
		File fromF = new File(sourcePath);
		File toF = new File(newPath);
		FileInputStream from = null;
	    FileOutputStream to = null;
	    try {
	    	from = new FileInputStream(fromF);
	    	to = new FileOutputStream(toF);
	    	byte[] buffer = new byte[4096];
	    	int bytesRead;
	    	while ((bytesRead = from.read(buffer)) != -1)
		    	to.write(buffer, 0, bytesRead); // write
	    } catch (IOException e) {
			return false;
		} finally {
	    	if (from != null){
	    		try {
			    	from.close();
			    } catch (IOException e) {
			    	return false;
			    } finally{
			    	if(sourceDelete)
			    		fromF.delete();
			    }
	    	}
	    	if (to != null)
	    		try {
	    			to.close();
	    		} catch (IOException e) {
	    			return false;
	    		}
	    }
	    return true;
	}
	
	public static boolean copyFile(String sourcePath, String newPath){
		return copyFile(sourcePath, newPath, false);
	}
	
	public static boolean moveFileForced(String sourcePath, String newPath){
		return copyFile(sourcePath, newPath, true);
	}
	
	public static void changeVisualTo(JFrame newFrame){
		JFrame oldFrame = null;
		if(Main.visual != null){
			oldFrame = Main.visual;
			oldFrame.setVisible(false);
		}
		Main.visual = newFrame;
		changeFrameIcon("/res/app.png", newFrame);
		Main.visual.setTitle("Simulador de Questões de Vestibulares");
		if(oldFrame != null){
			Main.visual.setLocation(oldFrame.getLocation());
			oldFrame.dispose();
		}
		Main.visual.setVisible(true);
	}
	
	public static void newVisual(JFrame newFrame){
		changeFrameIcon("/res/app.png", newFrame);
		newFrame.setTitle("Simulador de Questões de Vestibulares");
		newFrame.setLocation(Main.visual.getLocation());
		newFrame.setVisible(true);
	}
	
	public static String getHomePath(){
		String hp = System.getProperty("user.home");
		return ((hp == null | hp.length() < 1) ? "." : hp);
	}
	
	public static String unescapeString(String string) {
		return string.replace("\\n", "\n").replace("\\34", "\"").replace("\\t", "\t");
	}
	
	public static void changeFrameIcon(String path, JFrame frame){
		try{
			BufferedImage iconimg = ImageIO.read(frame.getClass().getResource(path));
			frame.setIconImage(iconimg);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String getNowTime(String format) {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter= 
				new SimpleDateFormat(format); // "dd/mm/yy HH:mm:ss"
		String dateNow = formatter.format(currentDate.getTime());
		return dateNow;
	}
	
	public static String getTodayDate() {
		return getNowTime("dd-MM-yy");
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Args[" + args.length + "]: " + args);
		try {
        	UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		createDataFolder();
		createMainConn();		
		System.out.println("Testing config DB: " + MainConfig.getConfig("test"));
		new Thread(checkVersionAndConnection).start();
		changeVisualTo(new MainVisual());
		if(args.length > 0)
			Main.visual.setLocation(new Point(new Integer(args[0]), new Integer(args[1])));
	}
}
