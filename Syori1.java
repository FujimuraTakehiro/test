package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Syori1
 */
@WebServlet("/Syori1")
public class Syori1 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//パラメータ取得
		String name=request.getParameter("name");
		String gender=request.getParameter("G");
		String age=request.getParameter("age");
		try {
			Class.forName("org.h2.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection con=null;
		List<querysystem> list = new ArrayList<>();

		try {
			//DB接続
			con=DriverManager.getConnection("jdbc:h2:tcp://localhost/~/example","sa","");
			//sql文初期化
			String sql="select*from PRACTICE where 1=1";
			PreparedStatement pstmt=null;
			//SQL判別処理フラグ
			int flg=0;
			//名前入力済みパターン
			if(name !="") {
				sql +="and NAME=? and GENDER=?";
				flg=1;
				//名前入力済みかつ年齢入力済みパターン
				if(age !="") {
					sql+="and AGE=?";
					flg=2;
					//名前入力済みかつ年齢未入力パターン
				}else {
					sql+="and GENDER=?";
					flg=3;
				}
				//名前未入力パターン
			}else {
				sql+="and GENDER=?";
				flg=4;
				//名前未入力かつ年齢入力済みパターン
				if(age !="") {
					sql+="and AGE=?";
					flg=5;
					//名前未入力かつ年齢未入力パターン
				}else {
					flg=6;
				}
			}
			//flgによる場合分け

			ResultSet rs=null;

			pstmt=con.prepareStatement(sql);
			if(flg==1) {
				pstmt.setString(1, name);
				pstmt.setString(2, gender);
			}else if(flg==2) {
				int Age=Integer.parseInt(age);
				pstmt.setString(1, name);
				pstmt.setString(2, gender);
				pstmt.setInt(3,Age);
			}else if(flg==3) {
				pstmt.setString(1, name);
				pstmt.setString(2, gender);
			}else if(flg==4) {
				int Age=Integer.parseInt(age);
				pstmt.setString(2, gender);
				pstmt.setInt(3, Age);
			}else if(flg==5) {
				int Age=Integer.parseInt(age);
				pstmt.setString(1, gender);
				pstmt.setInt(2, Age);
			}else if(flg==6) {
				pstmt.setString(1, gender);
			}

			rs=pstmt.executeQuery();

			//List<ResultSet> dtlist=new ArrayList<>();

			if(flg==6 || flg==7) {

				while(rs.next()) {
					querysystem q1=new querysystem();

					String namae=rs.getString("NAME");
					String seibetu=rs.getString("GENDER");
					int hp=rs.getInt("HP");
					int nennrei=rs.getInt("AGE");

					q1.setnamae(namae);
					q1.setnennrei(nennrei);
					q1.setseibetu(seibetu);
					q1.sethp(hp);

					list.add(q1);
//					request.setAttribute("NAMAE", namae);
//					request.setAttribute("SEIBETU", seibetu);
//					request.setAttribute("HP", hp);
//					request.setAttribute("NENNREI", nennrei);
				}
			}else {
				if(rs.next()) {
					String namae=rs.getString("NAME");
					String seibetu=rs.getString("GENDER");
					int hp=rs.getInt("HP");
					int nennrei=rs.getInt("AGE");

					request.setAttribute("NAMAE", namae);
					request.setAttribute("SEIBETU", seibetu);
					request.setAttribute("HP", hp);
					request.setAttribute("NENNREI", nennrei);
				}
			}

			rs.close();
			pstmt.close();
			RequestDispatcher dispatcher = request.getRequestDispatcher("result.jsp");
			dispatcher.forward(request, response);

		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(con !=null) {
				try {
					con.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

}
