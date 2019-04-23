package com.internousdev.venus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.venus.dto.PurchaseHistoryInfoDTO;
import com.internousdev.venus.util.DBConnector;

public class PurchaseHistoryInfoDAO {
	public List<PurchaseHistoryInfoDTO> getPurchaseHistoryList(String userId) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		List<PurchaseHistoryInfoDTO> purchaseHistoryInfoDTOList = new ArrayList<PurchaseHistoryInfoDTO>();

		String sql = "SELECT phi.id as id, phi.user_id as user_id, phi.product_id as product_id, pi.product_name as product_name,"
				+ " pi.product_name_kana as product_name_kana, pi.image_file_path as image_file_path,"
				+ " pi.image_file_name as image_file_name, pi.release_company as release_company, "
				+ " pi.release_date as release_date, phi.price as price, phi.product_count as product_count,"
				+ " phi.price*phi.product_count as totalPrice"
				+ " FROM purchase_history_info as phi LEFT JOIN product_info as pi ON phi.product_id=pi.product_id"
				+ " WHERE phi.user_id=? ORDER BY phi.regist_date DESC";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				PurchaseHistoryInfoDTO purchaseHistoryInfoDTO = new PurchaseHistoryInfoDTO();
				purchaseHistoryInfoDTO.setId(rs.getInt("id"));
				purchaseHistoryInfoDTO.setUserId(rs.getString("user_id"));
				purchaseHistoryInfoDTO.setProductId(rs.getInt("product_id"));
				purchaseHistoryInfoDTO.setProductName(rs.getString("product_name"));
				purchaseHistoryInfoDTO.setProductNameKana(rs.getString("product_name_kana"));
				purchaseHistoryInfoDTO.setImageFilePath(rs.getString("image_file_path"));
				purchaseHistoryInfoDTO.setImageFileName(rs.getString("image_file_name"));
				purchaseHistoryInfoDTO.setReleaseCompany(rs.getString("release_company"));
				purchaseHistoryInfoDTO.setReleaseDate(rs.getDate("release_date"));
				purchaseHistoryInfoDTO.setPrice(rs.getInt("price"));
				purchaseHistoryInfoDTO.setProductCount(rs.getInt("product_count"));
				purchaseHistoryInfoDTO.setTotalPrice(rs.getInt("totalPrice"));


				purchaseHistoryInfoDTOList.add(purchaseHistoryInfoDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return purchaseHistoryInfoDTOList;
	}

	public int insert(String user_id, int product_id, int product_count, int price, String destination_id)
			throws SQLException {
		int count = 0;
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		String sql = "INSERT INTO purchase_history_info(user_id, product_id, product_count, price, destination_id, regist_date, update_date) VALUES(?,?,?,?,?,now(),now())";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user_id);
			ps.setInt(2, product_id);
			ps.setInt(3, product_count);
			ps.setInt(4, price);
			ps.setString(5, destination_id);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return count;
	}


	public int deleteAll(String userId){
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int count=0;

		String sql="DELETE FROM purchase_history_info WHERE user_id=?";
		try{
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, userId);

			count=ps.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return count;
	}

}
