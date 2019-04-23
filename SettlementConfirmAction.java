package com.internousdev.venus.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.venus.dao.CartInfoDAO;
import com.internousdev.venus.dao.DestinationInfoDAO;
import com.internousdev.venus.dto.CartInfoDTO;
import com.internousdev.venus.dto.DestinationInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class SettlementConfirmAction extends ActionSupport implements SessionAware {
	public Map<String, Object> session;
	private String message;
	public int flag = 0;
	public CartInfoDAO cartInfoDAO = new CartInfoDAO();
	private DestinationInfoDAO destinationInfoDAO = new DestinationInfoDAO();
	public ArrayList<DestinationInfoDTO> destinationInfoDTOList = new ArrayList<DestinationInfoDTO>();
	public List<CartInfoDTO> cartInfoDTOList = new ArrayList<CartInfoDTO>();

	public String execute() throws SQLException {
		if (session.isEmpty()) {
			return "sessionTimeout";
		}

		String result = "login";
		if (session.get("logined").equals(0)) {
			session.put("cartFlag", 1);
			cartInfoDTOList = cartInfoDAO.getCartInfoDTOList(String.valueOf(session.get("tempUserId")));
			session.put("cartInfoDTOList", cartInfoDTOList);
			result = "login";
		} else if (session.get("logined").equals(1)) {
			cartInfoDTOList = cartInfoDAO.getCartInfoDTOList(String.valueOf(session.get("userId")));
			session.put("cartInfoDTOList", cartInfoDTOList);

			destinationInfoDTOList = destinationInfoDAO.select(String.valueOf(session.get("userId")));
			if (destinationInfoDTOList.size() <= 0) {
				flag = 0;
				setMessage("宛先情報がありません。");
				result = SUCCESS;
			} else {
				session.put("destinationInfoDTOList", destinationInfoDTOList);
				flag = 1;
				setMessage("宛先情報を選択してください。");
				result = SUCCESS;
			}
		}
		return result;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ArrayList<DestinationInfoDTO> getDestinationInfoDTOList() {
		return destinationInfoDTOList;
	}

	public void setDestinationInfoDTOList(ArrayList<DestinationInfoDTO> destinationInfoDTOList) {
		this.destinationInfoDTOList = destinationInfoDTOList;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
