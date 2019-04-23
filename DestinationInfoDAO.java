package com.internousdev.venus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.internousdev.venus.dto.DestinationInfoDTO;
import com.internousdev.venus.util.DBConnector;

public class DestinationInfoDAO {

	// 宛先情報登録機能

	public int insert(String userId, String familyName, String firstName, String familyNameKana, String firstNameKana,
			String userAddress, String telNumber, String email) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int count = 0;
		String sql = "INSERT INTO destination_info(user_id, family_name, first_name, family_name_kana, first_name_kana, email, tel_number, user_address, regist_date, update_date)"
				+ " values(?,?,?,?,?,?,?,?,now(),now())";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, familyName);
			ps.setString(3, firstName);
			ps.setString(4, familyNameKana);
			ps.setString(5, firstNameKana);
			ps.setString(6, email);
			ps.setString(7, telNumber);
			ps.setString(8, userAddress);
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

	public DestinationInfoDTO select(int userId) {
		DestinationInfoDTO destinationDTO = new DestinationInfoDTO();
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		String sql = "SELECT * FROM destination_info WHERE user_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				destinationDTO.setFamilyName(rs.getString("family_name"));
				destinationDTO.setFirstName(rs.getString("first_name"));
				destinationDTO.setFamilyNameKana(rs.getString("family_name_kana"));
				destinationDTO.setFirstNameKana(rs.getString("first_name_kana"));
				destinationDTO.setEmail(rs.getString("email"));
				destinationDTO.setTelNumber(rs.getString("tel_number"));
				destinationDTO.setUserAddress(rs.getString("user_address"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return destinationDTO;
	}

	public ArrayList<DestinationInfoDTO> select(String user_id) {
		ArrayList<DestinationInfoDTO> destinationInfoDTOList = new ArrayList<DestinationInfoDTO>();
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		String sql = "SELECT id, user_id, family_name, family_name_kana, first_name_kana, first_name, email, tel_number, user_address FROM destination_info where user_id = ? ORDER BY regist_date ASC";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user_id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				DestinationInfoDTO destinationInfoDTO = new DestinationInfoDTO();
				destinationInfoDTO.setId(rs.getInt("id"));
				destinationInfoDTO.setUserId(rs.getString("user_id"));
				destinationInfoDTO.setFamilyName(rs.getString("family_name"));
				destinationInfoDTO.setFirstName(rs.getString("first_name"));
				destinationInfoDTO.setFamilyNameKana(rs.getString("family_name_kana"));
				destinationInfoDTO.setFirstNameKana(rs.getString("first_name_kana"));
				destinationInfoDTO.setEmail(rs.getString("email"));
				destinationInfoDTO.setTelNumber(rs.getString("tel_number"));
				destinationInfoDTO.setUserAddress(rs.getString("user_address"));
				destinationInfoDTOList.add(destinationInfoDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return destinationInfoDTOList;
	}
}
