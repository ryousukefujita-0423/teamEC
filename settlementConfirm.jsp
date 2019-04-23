<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/venus.css">
<link href="https://fonts.googleapis.com/css?family=Great+Vibes&amp;subset=latin-ext" rel="stylesheet">
<title>決済確認画面</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<div class="top">
		<h1>決済確認画面</h1>
	</div>
	<s:if test="flag == 0">
		<div class="info">
			<s:property value="message" />
		</div>
	</s:if>
	<s:elseif test="flag == 1">
		<div class="info">
			<s:property value="message" />
		</div>
		<s:form action="SettlementCompleteAction" id="cartForm">
			<table class="horizontal-list-table">
				<tr>
					<th>#</th>
					<th>姓</th>
					<th>名</th>
					<th>ふりがな</th>
					<th>住所</th>
					<th>電話番号</th>
					<th>メールアドレス</th>
				</tr>
				<s:iterator value="destinationInfoDTOList" status="st">
					<tr>
						<td><s:if test="#st.index == 0">
								<input type="radio" name="destination_id" checked="checked"
									value="<s:property value='id'/>" />
							</s:if> <s:else>
								<input type="radio" name="destination_id"
									value="<s:property value='id'/>" />
							</s:else></td>
						<td><s:property value="familyName" /></td>
						<td><s:property value="firstName" /></td>
						<td><s:property value="familyNameKana" /> <s:property
								value="firstNameKana" /></td>
						<td><s:property value="userAddress" /></td>
						<td><s:property value="telNumber" /></td>
						<td><s:property value="email" /></td>
					</tr>
				</s:iterator>

			</table>
			<br>
			<div class="submit_btn_box">
				<s:submit value="決済" class="submit_btn" />
			</div>
		</s:form>
	</s:elseif>
	<div class="submit_btn_box">
		<a href='<s:url action="CreateDestinationAction" />'><s:submit
				value="新規宛先登録" class="submit_btn" /></a>
	</div>
</body>
</html>