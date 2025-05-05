function cardMethod(issuerCode){
	switch (issuerCode) {
		case "3K": return "기업비씨";
		case "46": return "광주카드";
		case "71": return "롯데카드";
		case "30": return "산업은행";
		case "31": return "비씨카드";
		case "51": return "삼성카드";
		case "38": return "새마을금고";
		case "41": return "신한카드";
		case "62": return "신협카드";
		case "36": return "씨티카드";
		case "33": return "우리카드(BC)";
		case "W1": return "우리카드";
		case "37": return "우체국카드";
		case "39": return "저축은행카드";
		case "35": return "전북카드";
		case "42": return "제주카드";
		case "15": return "카카오뱅크카드";
		case "3A": return "케이뱅크카드";
		case "24": return "토스뱅크카드";
		case "21": return "하나카드";
		case "61": return "현대카드";
		case "11": return "국민카드";
		case "91": return "농협카드";
		case "34": return "수협카드";
		case "PCP": return "페이코카드";
		case "KBS": return "KB증권카드";
		default: return "기타카드";
	}
}