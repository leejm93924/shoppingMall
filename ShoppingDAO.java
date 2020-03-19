package shoppingDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import shoppingVO.AskVO;
import shoppingVO.BaesongjiVO;
import shoppingVO.CartVO;
import shoppingVO.CouponVO;
import shoppingVO.CustomerVO;
import shoppingVO.ReviewVO;
import shoppingVO.SangpumVO;

public class ShoppingDAO { 
	
	private Connection con;
	private DataSource dataFactory;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public ShoppingDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context)ctx.lookup("java:comp/env");
			dataFactory = (DataSource)envContext.lookup("jdbc/oracle");			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void pstmtClose() throws SQLException {
		if(pstmt != null) {
			pstmt.close();
		}
	}
	
	
	public CustomerVO signIn(String id, String pw) throws SQLException {
		
		String sql = "select * from customer where (customer_id=? AND customer_password=?)";
		CustomerVO tv= null;
		
		try {
		con = dataFactory.getConnection();
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, id);
		pstmt.setString(2, pw);
		
		rs = pstmt.executeQuery();
		while(rs.next()) {
			String customer_id = rs.getString("customer_id");
			String customer_password = rs.getString("customer_password");
			String customer_name = rs.getString("customer_name");
			Date customer_birth = rs.getDate("customer_birth");
			String customer_phone = rs.getString("customer_phone");
			String customer_email = rs.getString("customer_email");
			
			
				tv = new CustomerVO(customer_id, customer_password, customer_name, customer_birth,
						customer_phone, customer_email);
		}
		pstmt.close();
		rs.close();
		con.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return tv;
		
		}
		
		
	
	public boolean signUp(String id, String pw, String name, 
			String birth, String phone, String email) throws SQLException {
		
		String sql = "insert into customer values (?,?,?,?,?,?)";
		
		try {
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			pstmt.setString(4, birth);
			pstmt.setString(5, phone);
			pstmt.setString(6, email);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			return false;
		}
		pstmt.close();
		con.close();
		return true;
		
	}
	
	public boolean idCheck(String inputId) throws SQLException {
		
		String sql = "select * from customer where customer_id=?";
		boolean flag;
		
		try {
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, inputId);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (rs.next()) { // 중복이면
			flag = true; 
		} else { // 중복이 아니면
			flag = false; 
		}
		pstmt.close();
		con.close();
		rs.close();
		return flag;
	}
	
	public String idSearch(String name, String email) throws SQLException {
		
		String sql = "select customer_id from customer"
				+ " where (customer_name=? AND customer_email=?)";
		
		String searchName = "not found";
		
		try {
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, email);
			rs = pstmt.executeQuery();	
			while (rs.next()) {
				searchName = rs.getString(1);
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return searchName;
		
	}
	
	
	// 입력된 정보를 바탕으로 해당되는 계정을 찾고, 이메일을 반환하는 메소드.
	public String pwSearch(String name, String email, String id) throws SQLException {
		
		String sql = "select * from customer"
				+ " where (customer_name=? AND customer_email=? AND customer_id=?)";
		String searchMail=null;
		
		try {
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, email);
			pstmt.setString(3, id);
			rs= pstmt.executeQuery();
			while(rs.next()) {
				searchMail = rs.getString("customer_email");
			}		
			rs.close();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return searchMail;
		
	}


	// 비밀번호를 초기화하고, 임시 비밀번호를 반환하는 메소드
	public String pwReset(String searchMail) throws SQLException {
		
		String sql = "update customer set customer_password=?"
				+ " where customer_email=?";
		
		String imsiPw = imsiPw();
		
		try {
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, imsiPw);
			pstmt.setString(2, searchMail);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		pstmt.close();
		con.close();
		return imsiPw;
	}

	
	// 임시 비밀번호를 생성하여 반환하는 메소드
	private String imsiPw() {
		
		String tmp="abcdefghijklmnopqrstuvwxyz0123456789";
		char[] tmp2 = tmp.toCharArray();
		
		Random rnd = new Random();
		char[] tmp3 = new char[10];
		
		int length = tmp2.length;
		
		for (int i=0; i<10; i++) {
			int index = rnd.nextInt(length);
			tmp3[i] = tmp2[index];
		}
		String pw = tmp3.toString();
		return pw;
	}

	
	// 상품 번호를 입력하여 상품 하나의 정보를 가져오는 메소드
	public SangpumVO sangpumSearch(String sangpumNumber) throws SQLException {
		
		String sql = "select * from sangpum where sangpum_number=?";
		SangpumVO result = null;
		
		try {			
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, sangpumNumber);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(rs.next()) {
			
			String number = sangpumNumber;
			String name = rs.getString(2);			
			int price = rs.getInt(3);
			int jaego = rs.getInt(4);
			String detail = rs.getString(5);
			int click = rs.getInt(6);
			String inform = rs.getString(7);
			String category = rs.getString(8);
			String imgsrc = rs.getString(9);
			
			result = new SangpumVO(number, name, price, jaego,
						detail, click, inform, category, imgsrc);
		}
		pstmt.close();
		con.close();
		return result;
		
	}	
	
	
	// 해당 상품에 대한 문의 전체를 반환하는 메소드.
	public ArrayList<AskVO> askSearch(String sangpumNumber) throws SQLException {
		
		String sql = "select * from sangpum_ask"
				+ " where sangpum_number=?"
				+ " order by asking_date desc";
		
		ArrayList<AskVO> resultSet = new ArrayList<AskVO>();
		AskVO result = null;
		
		try {
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, sangpumNumber);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String number = rs.getString("sangpum_number");
				String askName = rs.getString("asker");
				String asking = rs.getString("asking");
				String tmp = rs.getString("secrets");
				boolean secrets;
				if (tmp.contentEquals("true")) {
					secrets=true;
				} else {
					secrets=false;
				}
				Date date = rs.getDate("asking_date");
				String answer = rs.getString("answer");
				Date answerDate = rs.getDate("answer_date");
				
				result = new AskVO(number, askName, asking, secrets, date, answer, answerDate);
				resultSet.add(result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pstmt.close();
		con.close();
		return resultSet;
	}
	
	
	// 상품번호를 입력받아 리뷰전체를 반환하는 메소드
	public ArrayList<ReviewVO> reviewSearch(String sangpumNumber) throws SQLException {
		
		String sql = "select * from sangpum_review"
				+ " where sangpum_number=?"
				+ " order by review_date desc";
		
		ArrayList<ReviewVO> resultSet = new ArrayList<ReviewVO>();
		ReviewVO result = null;
		
		try {
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, sangpumNumber);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(rs.next()) {
			String number = sangpumNumber;
			int stars = rs.getInt("stars");
			String review = rs.getString("review");
			String name = rs.getString("reviewer");
			String reviewup = rs.getString("review_up");
			Date reviewDate = rs.getDate("review_date");
						
			result = new ReviewVO(number, stars, review, name, reviewup, reviewDate);
			resultSet.add(result);
		}
		pstmt.close();
		con.close();
		return resultSet;
		
	}
	
	
	// 회원이 자신의 이메일, 휴대폰번호를 수정하는 메소드
	public boolean updateCustomer(String id, String newEmail, String newPhone) throws SQLException {
		
		String sql = "update customer"
				+ " set customer_email=?, customer_phone=?"
				+ " where customer_id=?";
		
		try {
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, newEmail);
			pstmt.setString(2, newPhone);
			pstmt.setString(3, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		pstmt.close();
		con.close();
		return true;
	}
	
	
	// 배송지 리스트를 넘겨주는 메소드
	public ArrayList<BaesongjiVO> baesongjiSearch(String id) throws SQLException {
		
		String sql = "select *"
				+ " from baesongji"
				+ " where customer_id=?";
		
		ArrayList<BaesongjiVO> resultSet = new ArrayList<BaesongjiVO>();
		BaesongjiVO result = null;
		
		try {
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(rs.next()) {
			String customerId = rs.getString("customer_id");
			String address = rs.getString("customer_address");
			
			result = new BaesongjiVO(customerId,address);
			resultSet.add(result);
		}
		pstmt.close();
		con.close();
		return resultSet;
	}
	
	
	// 배송지 정보를 변경하는 메소드
	public boolean baesongjiUpdate(String id, String newBaesongji) throws SQLException {
		
		String sql = "update baesongji"
				+ " set customer_address=?"
				+ " where customer_id=?";
		
		try {
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, newBaesongji);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pstmt.close();
		con.close();
		return false;
		
	}
	
	
	
	// 쿠폰 목록을 넘겨주는 메소드
	public ArrayList<CouponVO> couponSearch(String id) throws SQLException {
		
		String sql = "select * from coupon"
				+ " where customer_id=?";
		
		ArrayList<CouponVO> resultSet = new ArrayList<CouponVO>();
		CouponVO result = null;
		
		try {
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(rs.next()) {
			String number = rs.getString("coupon_number");
			String type = rs.getString("coupon_type");
			String discount = rs.getString("discount");
			String customer_id = rs.getString("customer_id");
			
			result = new CouponVO(number,type,discount, customer_id);
			resultSet.add(result);
		}
		pstmt.close();
		con.close();
		return resultSet;
	}
	
	
	// 상품 상세페이지에서 장바구니에 물건 하나를 담는 메소드
	public boolean cartAdding(String customerId, String sangpumNumber, int sangpumCount) throws SQLException {
				
		String name="";
		int price=0;
		String imgsrc="";
		
		String sql = "select sangpum_name, sangpum_price, sangpum_image"
				+ " from sangpum"
				+ " where sangpum_number=?";
		
		con = dataFactory.getConnection();
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, sangpumNumber);
		rs = pstmt.executeQuery();
		
		while(rs.next()) {
			name = rs.getString("sangpum_name");
			price = rs.getInt("sangpum_price");
			imgsrc = rs.getString("sangpum_image");						
		}
		
		pstmt.close();
		con.close();
		
		sql = "insert into cart values (?,?,?,?,?,?)";
		
		try {
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, customerId);
			pstmt.setString(2, sangpumNumber);
			pstmt.setString(3, name);
			pstmt.setInt(4, sangpumCount);
			pstmt.setString(5, imgsrc);
			pstmt.setInt(6, price);
			pstmt.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		pstmt.close();
		con.close();
		return true;
	}
	
	
	// 장바구니 목록 전체를 가져오는 메소드.
	public ArrayList<CartVO> cartCall(String customerId) throws SQLException {
		
		String sql = "select * from cart"
				+ " where customer_id=?"
				+ " order by sangpum_name";
		
		ArrayList<CartVO> resultSet = new ArrayList<CartVO>();
		CartVO result = null;
		
		con = dataFactory.getConnection();
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, customerId);
		rs = pstmt.executeQuery();
		
		while(rs.next()) {
			String id = rs.getString("customer_id");
			String number = rs.getString("sangpum_number");
			String name = rs.getString("sangpum_name");
			int count = rs.getInt("sangpum_count");
			String imgsrc = rs.getString("sangpum_image");
			int price = rs.getInt("sangpum_price");
			
			result = new CartVO(id, number, name, count, imgsrc, price);
			resultSet.add(result);
		}
		pstmt.close();
		con.close();
		return resultSet;
	}
	
	
	// 카테고리와 검색명을 받아서 상품목록을 조회수 순으로 정렬하여 반환하는 메소드
	public ArrayList<SangpumVO> productSearch(String category, String searchName) throws SQLException {
		
		String sql = "select * from sangpum"
				+ " where (sangpum_category=? AND sangpum_name like '%?%' "
				+ " order by sangpum_click desc";
		SangpumVO result = null;
		ArrayList<SangpumVO> resultSet = new ArrayList<SangpumVO>();
		
		con = dataFactory.getConnection();
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, category);
		pstmt.setString(2, searchName);
		rs = pstmt.executeQuery();
		
		while(rs.next()) {
			String number = rs.getString(1);
			String name = rs.getString(2);			
			int price = rs.getInt(3);
			int jaego = rs.getInt(4);
			String detail = rs.getString(5);
			int click = rs.getInt(6);
			String inform = rs.getString(7);
			String category2 = rs.getString(8);
			String imgsrc = rs.getString(9);
			
			result = new SangpumVO(number, name, price, jaego,
						detail, click, inform, category2, imgsrc);
			resultSet.add(result);
		}
		pstmt.close();
		con.close();
		return resultSet;
	}
	
	
	// 모든 상품을 조회수로만 정렬하여 반환하는 메소드
	public ArrayList<SangpumVO> allProductSearch() throws SQLException {
		
		String sql = "select * from sangpum"				
				+ " order by sangpum_click desc";
		SangpumVO result = null;
		ArrayList<SangpumVO> resultSet = new ArrayList<SangpumVO>();
		
		con = dataFactory.getConnection();
		pstmt = con.prepareStatement(sql);
		rs = pstmt.executeQuery();
		
		while(rs.next()) {
			String number = rs.getString(1);
			String name = rs.getString(2);			
			int price = rs.getInt(3);
			int jaego = rs.getInt(4);
			String detail = rs.getString(5);
			int click = rs.getInt(6);
			String inform = rs.getString(7);
			String category = rs.getString(8);
			String imgsrc = rs.getString(9);
			
			result = new SangpumVO(number, name, price, jaego,
						detail, click, inform, category, imgsrc);
			resultSet.add(result);
		}
		pstmt.close();
		con.close();
		return resultSet;
	}
	
	
	// 상품문의를 입력받아 저장하는 메소드
	public boolean askInput(String sangpumNumber, String asker, 
				String asking, boolean secret) throws SQLException {
		
		String sql = "insert into sangpum_ask values (?,?,?,?,?)";
		boolean flag = false;
		try {
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, sangpumNumber);
			pstmt.setString(2, asker);
			pstmt.setString(3, asking);
			if (secret == true)
				pstmt.setString(4, "true");
			else
				pstmt.setString(5, "false");
			String askingDate = nowtime();
			pstmt.setString(5, askingDate);		
			pstmt.executeUpdate();
			flag = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = false;
		} finally {
			pstmt.close();
			con.close();
		}
		
		return flag;
	}

	
	// 현재 시간을 "YY-MM-DD HH:mm:ss" 형식으로 반환하는 메소드
	private String nowtime() {
		
		Date d1 = new Date();
		int year = d1.getYear()+1900;
		int month = d1.getMonth()+1;
		int day = d1.getDate();
		int hour = d1.getHours();
		int min = d1.getMinutes();
		int sec = d1.getSeconds();		
		
		String s = ""+year+"-"+month+"-"+day+" "+hour+":"+min+":"+sec;		
		
		return s;
	}
	
	
	
	// 가장 상위 카테고리만 반환하는 메소드
		public ArrayList<String> getCategory() throws SQLException {
			
			String sql = "select distinct sangpum_category from sangpum";
			ArrayList<String> categories = new ArrayList<String>();
			
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String s = rs.getString(1).split(">")[0];
				categories.add(s);
			}
			pstmt.close();
			con.close();
			return categories;
		}
	
	// 상위 카테고리에 해당하는 하위 카테고리를 반환하는 메소드
		public ArrayList<String> getSubcategory(String category) throws SQLException {
			
			String sql = "select distinct sangpum_category from sangpum"
					+ " where sangpum_category like '?>%'";
			
			ArrayList<String> subCategories = new ArrayList<String>();
			
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, category);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String s = rs.getString(1).split(">")[1];
				subCategories.add(s);
			}
			pstmt.close();
			con.close();
			return subCategories;
		}
	
	// 카테고리에 해당하는 상품을 반환하는 메소드
		public ArrayList<SangpumVO> categorySearch(String category) throws SQLException {
			
			String sql = "select * from sangpum"
					+ " where sangpum_category=?"				
					+ " order by sangpum_click desc";
			SangpumVO result = null;
			ArrayList<SangpumVO> resultSet = new ArrayList<SangpumVO>();
			
			con = dataFactory.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, category);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String number = rs.getString(1);
				String name = rs.getString(2);			
				int price = rs.getInt(3);
				int jaego = rs.getInt(4);
				String detail = rs.getString(5);
				int click = rs.getInt(6);
				String inform = rs.getString(7);
				String category2 = rs.getString(8);
				String imgsrc = rs.getString(9);
				
				result = new SangpumVO(number, name, price, jaego,
							detail, click, inform, category2, imgsrc);
				resultSet.add(result);
			}
			pstmt.close();
			con.close();
			return resultSet;
		}
		
		// 장바구니에서 목록을 제거하는 메소드
		public void deletCartList(String[] sangpum_number) {
			String sql = "delete from cart where sangpum_number = ?";
			try {
				
				con = dataFactory.getConnection();
				for(int i = 0; i < sangpum_number.length; i++) {
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, sangpum_number[i]);
					pstmt.executeUpdate();
				}
				pstmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		
		// 문의사항을 입력받는 메소드
		public boolean askInput(String sangpum_number, String customer_id, String asking, String secrets) {
			
			String sql = "insert into sangpum_ask values (?,?,?,?,?,?,?)";
			boolean flag = false;
			try {
				con = dataFactory.getConnection();
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, sangpum_number);
				pstmt.setString(2, customer_id);
				pstmt.setString(3, asking);
				pstmt.setString(4, secrets);
				pstmt.setString(5, "2020-03-05");
				pstmt.setString(6, "답변란 입니다");
				pstmt.setString(7, "2020-03-08");
				pstmt.executeUpdate();
				pstmt.close();
				con.close();
				flag = true;
				} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return flag;
		}

		// 리뷰를 입력받는 메소드
		public boolean reviewInput(String sangpum_number44, String reviewer, String review44) {
			
			String sql = "insert into sangpum_review values(?,?,?,?,?,?)";
			boolean flag = false;
			
			try {
				con = dataFactory.getConnection();
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, sangpum_number44);
				pstmt.setInt(2, 3);
				pstmt.setString(3, review44);
				pstmt.setString(4, reviewer);
				pstmt.setInt(5, 0);
				pstmt.setString(6, "2020-03-18");
				pstmt.executeUpdate();
				pstmt.close();
				con.close();
				flag = true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// TODO Auto-generated method stub
			return flag;
		}
		
		// main-paging
		public ArrayList getPaging(int pageNum, int itemNum) {
			ArrayList<SangpumVO> resultSet = new ArrayList<SangpumVO>();
			try {
				ArrayList<SangpumVO> list = allProductSearch();
				System.out.println(list.size());
				System.out.println(pageNum);
				System.out.println(itemNum);
			
				
				for(int i = (pageNum - 1) * itemNum; i < ((pageNum-1) * itemNum) + itemNum; i++) {
					if(i == list.size()) {
						return resultSet;
					}
					//11 9  
					resultSet.add(list.get(i));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultSet;
		}
		
		// 회원이 자신의 이메일을 수정하는 메소드
		public boolean updateEmail(String id, String newEmail) {
			
			String sql = "update customer"
					+ " set customer_email=?"
					+ " where customer_ID=?";
			
			try {
				con = dataFactory.getConnection();
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, newEmail);
				pstmt.setString(2, id);
				pstmt.executeUpdate();
				pstmt.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			return true;
		}
		
		// 회원이 자신의 휴대폰을 수정하는 메소드
			public boolean updatePhone(String id, String newPhone) {
				
				String sql = "update customer"
						+ " set customer_phone=?"
						+ " where customer_ID=?";
				
				try {
					con = dataFactory.getConnection();
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, newPhone);
					pstmt.setString(2, id);
					pstmt.executeUpdate();
					pstmt.close();
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				
				return true;
			}
			// 회원이 자신의 비밀번호를 수정하는 메소드
					public boolean updatePW(String id, String pw, String newPW) {
						
						String sql = "update customer"
								+ " set customer_password=?"
								+ " where (customer_ID=? AND customer_password=?)";
						
						try {
							con = dataFactory.getConnection();
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, newPW);
							pstmt.setString(2, id);
							pstmt.setString(3, pw);
							pstmt.executeUpdate();
							pstmt.close();
							con.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return false;
						}
						
						return true;
					}
			public CustomerVO getCustomerInfo(String id) {//////
			CustomerVO tv = null;
			String sql = "SELECT * FROM CUSTOMER where customer_id = ?";
			try {
				con = dataFactory.getConnection();
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, id);
				rs = pstmt.executeQuery();
				while(rs.next()){
					String customer_id = rs.getString("customer_id");
					String customer_password = rs.getString("customer_password");
					String customer_name = rs.getString("customer_name");
					Date customer_birth = rs.getDate("customer_birth");
					String customer_phone = rs.getString("customer_phone");
					String customer_email = rs.getString("customer_email");
				
					tv = new CustomerVO(customer_id, customer_password, customer_name, customer_birth,customer_phone, customer_email);
				}
				pstmt.close();
				con.close();
			}catch(SQLException e) {
					e.printStackTrace();
			}
			return tv;
		}
		
}
