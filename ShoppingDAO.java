package shoppingDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import shoppingDBConn.ShoppingDBConn;
import shoppingVO.AskVO;
import shoppingVO.CustomerVO;
import shoppingVO.ReviewVO;
import shoppingVO.SangpumVO;

public class ShoppingDAO { 
	
	private Connection con;
		
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public ShoppingDAO() throws ClassNotFoundException, SQLException {
		con= new ShoppingDBConn().getConnection();
						
	}
	
	public void pstmtClose() throws SQLException {
		if(pstmt != null) {
			pstmt.close();
		}
	}
	
	
	public CustomerVO signIn(String id, String pw) throws SQLException {
		
		String sql = "select * from customer where (customer_id=? AND customer_password=?)";
		CustomerVO tv= null;
		
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
			
			try {
				tv = new CustomerVO(customer_id, customer_password, customer_name, customer_birth,
						customer_phone, customer_email);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return tv;
		
	}
	
	public boolean signUp(String id, String pw, String name, String birth, String phone, String email) {
		
		String sql = "insert into customer values (?,?,?,?,?,?)";
		
		try {
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
		return true;
		
	}
	
	public boolean idCheck(String inputId) throws SQLException {
		
		String sql = "select * from customer where customer_id=?";
		boolean flag;
		
		try {
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
		
		return flag;
	}
	
	public String idSearch(String name, String email) throws SQLException {
		
		String sql = "select customer_id from customer"
				+ "where (customer_name=? AND customer_email=?)";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, email);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String searchName = null;		
		searchName =  pstmt.executeQuery().getString(1);
		
		return searchName;
		
	}
	
	
	// 입력된 정보를 바탕으로 해당되는 계정을 찾고, 이메일을 반환하는 메소드.
	public String pwSearch(String name, String email, String id) throws SQLException {
		
		String sql = "select * from customer"
				+ "where (customer_name=? AND customer_email=? AND customer_id=?)";
		String searchMail=null;
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, email);
			pstmt.setString(3, id);
			rs= pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		searchMail = rs.getString("customer_email");
		
		return searchMail;
		
	}


	// 비밀번호를 초기화하고, 임시 비밀번호를 반환하는 메소드
	public String pwReset(String searchMail) {
		
		String sql = "update customer set customer_password=?"
				+ "where customer_email=?";
		
		String imsiPw = imsiPw();
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, imsiPw);
			pstmt.setString(2, searchMail);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
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
		
		return result;
		
	}
	
	// 해당 상품에 대한 문의 전체를 반환하는 메소드.
	public ArrayList<AskVO> askSearch(String sangpumNumber) throws SQLException {
		
		String sql = "select * from sangpumask"
				+ "where sangpum_number=?"
				+ "order by asking_date desc";
		
		ArrayList<AskVO> resultSet = new ArrayList<AskVO>();
		AskVO result = null;
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, sangpumNumber);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(rs.next()) {
			String number = sangpumNumber;
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
			
			result = new AskVO(number, askName, asking, secrets, date);
			resultSet.add(result);
		}
		
		return resultSet;
	}
	
	
	// 상품번호를 입력받아 리뷰전체를 반환하는 메소드
	public ArrayList<ReviewVO> reviewSearch(String sangpumNumber) throws SQLException {
		
		String sql = "select * from sangpumreview"
				+ "where sangpum_number=?"
				+ "order by review_date desc";
		
		ArrayList<ReviewVO> resultSet = new ArrayList<ReviewVO>();
		ReviewVO result = null;
		
		try {
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
		
		return resultSet;
				
	}
	
	
	// 회원이 자신의 이메일, 휴대폰번호를 수정하는 메소드
	public boolean updateCustomer(String id, String newEmail, String newPhone) {
		
		String sql = "update customer"
				+ "set customer_email=?, customer_phone=?"
				+ "where customer_id=?";
		
		try {
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
		
		return true;
	}
	
	
	
	// 배송지 리스트를 넘겨주는 메소드
	public ArrayList<BaesongjiVO> receiveAddress(String id) throws SQLException {
		
		String sql = "select *"
				+ "from baesongji"
				+ "where customer_id=?";
		
		ArrayList<BaesongjiVO> resultSet = new ArrayList<BaesongjiVO>();
		BaesongjiVO result = null;
		
		try {
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
		return resultSet;
	}
	
	// 배송지 정보를 변경하는 메소드
	public boolean baesongjiUpdate(String id, String newBaesongji) {
		
		String sql = "update baesongji"
				+ "set customer_address=?"
				+ "where customer_id=?";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, newBaesongji);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	
	// 쿠폰 목록을 넘겨주는 메소드
	public ArrayList<CouponVO> couponSearch(String id) throws SQLException {
		
		String sql = "select * from coupon"
				+ "where customer_id=?";
		
		ArrayList<CouponVO> resultSet = new ArrayList<CouponVO>();
		CouponVO result = null;
		
		try {
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
		
		return resultSet;
	}
	
	// 장바구니에 물건 하나를 담는 메소드
	public boolean cartAdding(String customerId, String sangpumNumber, int sangpumCount) throws SQLException {
				
		String name="";
		int price=0;
		String imgsrc="";
		
		String sql = "select sangpum_name, sangpum_price, sangpum_image"
				+ "from sangpum"
				+ "where sangpum_number=?";
		
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, sangpumNumber);
		rs = pstmt.executeQuery();
		
		while(rs.next()) {
			name = rs.getString("sangpum_name");
			price = rs.getInt("sangpum_price");
			imgsrc = rs.getString("sangpum_image");						
		}
		
		sql = "insert into cart values (?,?,?,?,?,?)";
		
		try {
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
		
		return true;
	}
	
	// 장바구니 목록 전체를 가져오는 메소드.
	public ArrayList<CartVO> cartCall(String customerId) throws SQLException {
		
		String sql = "select * from cart"
				+ "where customer_id=?"
				+ "order by sangpum_name";
		
		ArrayList<CartVO> resultSet = new ArrayList<CartVO>();
		CartVO result = null;
		
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
		
		return resultSet;
	}
	
	// 카테고리와 검색명을 받아서 상품목록을 조회수 순으로 정렬하여 반환하는 메소드
	public ArrayList<SangpumVO> productSearch(String category, String searchName) throws SQLException {
		
		String sql = "select * from sangpum"
				+ "where (sangpum_category=? AND sangpum_name like '%?%' "
				+ "order by sangpum_click desc";
		SangpumVO result = null;
		ArrayList<SangpumVO> resultSet = new ArrayList<SangpumVO>();
		
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
		return resultSet;
	}
	
	// 모든 상품을 조회수로만 정렬하여 반환하는 메소드
	public ArrayList<SangpumVO> allProductSearch() throws SQLException {
		
		String sql = "select * from sangpum"				
				+ "order by sangpum_click desc";
		SangpumVO result = null;
		ArrayList<SangpumVO> resultSet = new ArrayList<SangpumVO>();
		
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
		return resultSet;
	}
}
