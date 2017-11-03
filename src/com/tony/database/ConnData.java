package com.tony.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.sun.xml.internal.ws.wsdl.writer.UsingAddressing;
import com.tony.method.UsualStrMethod;

public class ConnData {
	private UsualStrMethod usualStrMethod;
	public static Connection getConnection() {		
		 Connection con=null;
		InitialContext initC;
		try {
			initC = new InitialContext();//创建InitialContext对象			
			Context context = (Context)initC.lookup("java:comp/env");//不变
			DataSource ds = (DataSource)context.lookup("jdbc/bjwx");//tomcat服务器配置文件中数据源名称
			if (ds != null) 
				con = ds.getConnection();
		} catch (Exception e) {
			try {
				initC = new InitialContext();
				Context context = (Context)initC.lookup("java:comp/env");//不变
				DataSource ds = (DataSource)context.lookup("jdbc/bjwx");//tomcat服务器配置文件中数据源名称
				con = ds.getConnection();
			} catch (Exception e1) {
				System.out.println("数据库连接失败！");
				e1.printStackTrace();
			}
		}
		return con;	
	}
	//
	//查询===SQL语句，LEN是字段长度
		public List<String[]> findResult(String sql,int len){
			List<String[]> list=new ArrayList<String[]>();
			Connection con=null;
			Statement stmt=null;
			ResultSet  rs=null;
			try {
				con=ConnData.getConnection();
				stmt = con.createStatement();
				 rs = stmt.executeQuery(sql);    
				 while (rs.next()) {    
				      String[] ss=new String[len];
				      for(int i=0;i<len;i++){
				    	  ss[i]=rs.getObject(i+1)==null?"":(rs.getObject(i+1).toString());
				      }
				      list.add(ss);
				}    
			} catch (SQLException e) {
				list=null;
				e.printStackTrace();
			} finally{
				try {
					if(rs!=null)
						rs.close();
					if(stmt!=null)
						stmt.close();
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}		
			return list;
		}
		public List findResult(String sql,int len,Statement stmt,ResultSet  rs) throws SQLException{
			List<String[]> list=new ArrayList<String[]>();
				 rs = stmt.executeQuery(sql);    
				 while (rs.next()) {    
				      String[] ss=new String[len];
				      for(int i=0;i<len;i++){
				    	  ss[i]=usualStrMethod.isNull(rs.getObject(i+1));
				      }
				      list.add(ss);
				}    
		
			return list;
		}
		//查询===SQL语句，LEN是字段长度
				public List findResult(String[] sql,int[] len){
					List<List> lt=new ArrayList();
					Connection con=null;
					Statement stmt=null;
					ResultSet  rs=null;
					try {
						con=ConnData.getConnection();
						stmt = con.createStatement();
						for(int m=0;m<sql.length;m++){
							 rs = stmt.executeQuery(sql[m]);    
							 List<String[]> list=new ArrayList<String[]>();
							 while (rs.next()) {    
							      String[] ss=new String[len[m]];
							      for(int i=0;i<len[m];i++){
							    	  ss[i]=usualStrMethod.isNull(rs.getObject(i+1));
							      }
							      list.add(ss);
							}    
							 lt.add(list);
						}						
					} catch (SQLException e) {
						lt=null;
						e.printStackTrace();
					} finally{
						try {
							if(rs!=null)
								rs.close();
							if(stmt!=null)
								stmt.close();
							con.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}		
					return lt;
				}
		//=====只查询一个，例如  count(*)
		public int findOneResult(String sql){
			int result=0;
			Connection con=null;
			Statement stmt=null;
			ResultSet  rs=null;
			try {
				con=ConnData.getConnection();
				stmt = con.createStatement();
				 rs = stmt.executeQuery(sql);    
				 while (rs.next()) {    
					 result=rs.getInt(1);
				}    
			} catch (SQLException e) {
				result=0;
				e.printStackTrace();
			} finally{
				try {
					if(rs!=null)
						rs.close();
					if(stmt!=null)
						stmt.close();		
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}		
			return result;
		}
	
		//插入===批量
		public boolean insertData(List list){
			Connection con=null;
			boolean flag=false;
			Statement stmt=null;
			try {
				con=ConnData.getConnection();
				con.setAutoCommit(false);// 更改JDBC事务的默认提交方式
				stmt = con.createStatement();
				for(int i=0;i<list.size();i++){
					stmt.execute(list.get(i).toString());
				}				
				flag=true;
			} catch (SQLException e) {
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}//回滚JDBC事务
				e.printStackTrace();
			} finally{
				try {
					con.commit();//提交JDBC事务
					con.setAutoCommit(true);// 恢复JDBC事务的默认提交方式
					stmt.close();
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}		
			return flag;
		}
		//单个插入，返回查询的数字
		public int insertDataOne(String sql){
			Connection con=null;
			Statement stmt=null;
			ResultSet rs=null;
			int zid=0;
			try {
				con=ConnData.getConnection();
					con.setAutoCommit(false);// 更改JDBC事务的默认提交方式
					stmt = con.createStatement();
					stmt.execute(sql,Statement.RETURN_GENERATED_KEYS);
					rs = stmt.getGeneratedKeys();					
					if(rs.next()){  
						zid=rs.getInt(1);  
					}
				} catch (SQLException e) {
					zid=-1;
					try {
						con.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}//回滚JDBC事务
					e.printStackTrace();
				} finally{
					try {
						con.commit();//提交JDBC事务
						con.setAutoCommit(true);// 恢复JDBC事务的默认提交方式
						if(rs!=null)
							rs.close();
						if(stmt!=null)
							stmt.close();
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}					
			return zid;
		}
		//单个插入，返回zid
		public int insertOneBackZid(String sql,String tabname){
					Connection con=null;
					Statement stmt=null;
					ResultSet rs=null;
					int zid=0;
					try {
						con=ConnData.getConnection();
							con.setAutoCommit(false);// 更改JDBC事务的默认提交方式
							stmt = con.createStatement();
							stmt.execute(sql);
							//rs = stmt.getGeneratedKeys();
						} catch (SQLException e) {
							zid=-1;
							try {
								con.rollback();
							} catch (SQLException e1) {
								e1.printStackTrace();
							}//回滚JDBC事务
							e.printStackTrace();
						} finally{
							try {
								con.commit();//提交JDBC事务								
								con.setAutoCommit(true);// 恢复JDBC事务的默认提交方式
								//stmt.close();
								String ms="select max(zid) from "+tabname;
								//stmt = con.createStatement();
								rs = stmt.executeQuery(ms);    
								 while (rs.next()) {    
									 zid=rs.getInt(1);
								}  
								if(rs!=null)
									rs.close();
								if(stmt!=null)
									stmt.close();
								con.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}					
					return zid;
				}
		//删除
		public int deleteDataOne(String sql){
			Connection con=null;
			Statement stmt=null;
			ResultSet rs=null;
			int zid=0;
			try {
				con=ConnData.getConnection();
					con.setAutoCommit(false);// 更改JDBC事务的默认提交方式
					stmt = con.createStatement();
					stmt.execute(sql);
					zid=1;
				} catch (SQLException e) {
					zid=-1;
					try {
						con.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}//回滚JDBC事务
					e.printStackTrace();
				} finally{
					try {
						con.commit();//提交JDBC事务
						con.setAutoCommit(true);// 恢复JDBC事务的默认提交方式
						if(rs!=null)
							rs.close();
						if(stmt!=null)
							stmt.close();
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}					
			return zid;
		}

		public List pageFenYe(String sql,int len,int pagesize,int startnum){
			List<String[]> list=new ArrayList<String[]>();
			Connection con=null;
			Statement stmt=null;
			ResultSet  rs=null;
			try {
				con=ConnData.getConnection();
				stmt = con.createStatement();				
				String cx=sql+" limit "+pagesize*startnum+","+startnum;
				 rs = stmt.executeQuery(cx);    
				 while (rs.next()) {    
				      String[] ss=new String[len];
				      for(int i=0;i<len;i++){
				    	  ss[i]=rs.getObject(i+1)==null?"":(rs.getObject(i+1).toString());
				      }
				      list.add(ss);
				}    
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
					try {
						if(rs!=null)
							rs.close();
						if(stmt!=null)
							stmt.close();
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}		
			return list;			
		}
		
		public void insertNote(String zdate,String script,String ip,int zcode){
			String sql="insert into tbnote(zdate,zcode,zscript,zip) values('"+zdate+"',"+zcode+",'"+script+"','"+ip+"')";
			this.insertDataOne(sql);
		}
		//====定时器任务=============
		public boolean timerMake(){
			List list=new ArrayList();
			boolean flag=false;
			try{
				list.add("truncate table cabinet_main_temp");
				list.add("truncate table cabinet_temp");
				flag=this.insertData(list);
			}catch(Exception e){
				e.printStackTrace();
			}
			return flag;
		}

		//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		public UsualStrMethod getUsualStrMethod() {
			return usualStrMethod;
		}
		public void setUsualStrMethod(UsualStrMethod usualStrMethod) {
			this.usualStrMethod = usualStrMethod;
		}
}
