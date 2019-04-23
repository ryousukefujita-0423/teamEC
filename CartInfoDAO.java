package com.internousdev.venus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.venus.dto.CartInfoDTO;
import com.internousdev.venus.util.DBConnector;

public class CartInfoDAO {

	public List<CartInfoDTO> getCartInfoDTOList(String userId) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		List<CartInfoDTO> cartInfoDTOList = new ArrayList<CartInfoDTO>();
		String sql = " SELECT ci.id,ci.user_id,ci.product_id,ci.product_count,"
				+ "pi.product_name,pi.product_name_kana,pi.price,pi.image_file_path,pi.image_file_name,pi.release_date,pi.release_company,pi.regist_date,"
				+ "pi.update_date,pi.status,(ci.price * ci.product_count) as totalPrice "
				+ "FROM cart_info ci LEFT JOIN product_info pi ON pi.product_id=ci.product_id WHERE ci.user_id =? ORDER BY ci.update_date desc, ci.regist_date desc ";

		try {
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CartInfoDTO cartInfoDTO = new CartInfoDTO();
				cartInfoDTO.setId(rs.getInt("id"));
				cartInfoDTO.setUserId(rs.getString("user_id"));
				cartInfoDTO.setProductId(rs.getInt("product_id"));
				cartInfoDTO.setProductCount(rs.getInt("Product_count"));
				cartInfoDTO.setProductName(rs.getString("product_name"));
				cartInfoDTO.setProductNameKana(rs.getString("product_name_kana"));
				cartInfoDTO.setPrice(rs.getInt("price"));
				cartInfoDTO.setImageFilePath(rs.getString("image_file_path"));
				cartInfoDTO.setImageFileName(rs.getString("image_file_name"));
				cartInfoDTO.setReleaseDate(rs.getDate("release_date"));
				cartInfoDTO.setReleaseCompany(rs.getString("release_company"));
				cartInfoDTO.setStatus(rs.getString("status"));
				cartInfoDTO.setTotalPrice(rs.getInt("totalPrice"));

				cartInfoDTOList.add(cartInfoDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return cartInfoDTOList;
	}

	// カート内商品個別削除
	public int delete(String productId, String userId) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		String sql = "DELETE FROM cart_info WHERE product_id = ? AND user_id = ?";
		int count = 0;

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, productId);
			ps.setString(2, userId);

			count = ps.executeUpdate();

		} catch (

		Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	// カート内商品全て削除
	public int deleteAll(String user_id) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int count = 0;
		String sql = "DELETE FROM cart_info where user_id = ?";
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user_id);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

		}
		return count;
	}

	// カートの商品全ての合計金額
	public int getAllTotalPrice(String userId) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int allTotalPrice = 0;
		String sql = "SELECT sum(product_count * price) as total_price FROM cart_info WHERE user_id=? GROUP BY user_id";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				allTotalPrice = rs.getInt("total_price");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return allTotalPrice;
	}

	// 商品をカートに追加
	public int addCart(String userId, int productId, int productCount, int price) {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int count = 0;

		String sql = "INSERT INTO cart_info(user_id,product_id,product_count,price,regist_date,update_date) VALUES (?,?,?,?,now(),now())";

		try {
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, userId);
			ps.setInt(2, productId);
			ps.setInt(3, productCount);
			ps.setInt(4, price);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	// カート内の重複した商品をひとまとめにする
	public int updateProductCount(String userId, int productId, int productCount) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int count = 0;
		String sql = "UPDATE cart_info SET product_count = (product_count + ?) ,update_date = now() WHERE user_id = ? AND product_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, productCount);
			ps.setString(2, userId);
			ps.setInt(3, productId);

			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	// 重複しているかどうかの判定
	public boolean isExistsCartInfo(String userId, int productId) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		String sql = "SELECT COUNT(id) AS abc FROM cart_info WHERE user_id = ? AND product_id = ? ";

		boolean result = false;
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setInt(2, productId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getInt("abc") > 0) {
					result = true;
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 未ログイン時にカートの決済ボタン→新規登録→カート画面に遷移する際に、
	// 未ログイン時で得たカート情報を登録したユーザーIDに置き換える
	public int linkToUserId(String tempUserId, String userId, int productId) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int count = 0;
		String sql = "UPDATE cart_info SET user_id = ?,update_date = now() WHERE user_id = ? AND product_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, tempUserId);
			ps.setInt(3, productId);

			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}
}

