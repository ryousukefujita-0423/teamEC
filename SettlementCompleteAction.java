package com.internousdev.venus.action;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.venus.dao.CartInfoDAO;
import com.internousdev.venus.dao.PurchaseHistoryInfoDAO;
import com.internousdev.venus.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class SettlementCompleteAction extends ActionSupport implements SessionAware {
	public Map<String, Object> session;
	private String destination_id;
	private PurchaseHistoryInfoDAO purchaseHistoryInfoDAO = new PurchaseHistoryInfoDAO();
	private CartInfoDAO cartInfoDAO = new CartInfoDAO();

	public String execute() throws SQLException {
		if (session.isEmpty()) {
			return "sessionTimeout";
		}

		String result = ERROR;
		int count = 0;
		@SuppressWarnings("unchecked")
		List<CartInfoDTO> cartInfoDTOList = (List<CartInfoDTO>) session.get("cartInfoDTOList");
		for (CartInfoDTO dto : cartInfoDTOList) {
			count += purchaseHistoryInfoDAO.insert(String.valueOf(session.get("userId")), dto.getProductId(),
					dto.getProductCount(), dto.getPrice(), destination_id);
		}

		if (count > 0) {
			count = 0;
			count = cartInfoDAO.deleteAll(String.valueOf(session.get("userId")));
			if (count > 0) {
				result = SUCCESS;
			} else {
				result = ERROR;
			}
		} else {
			result = ERROR;
		}
		return result;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getDestination_id() {
		return destination_id;
	}

	public void setDestination_id(String destination_id) {
		this.destination_id = destination_id;
	}

}
