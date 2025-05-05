var element_wrap = document.getElementById('wrap3');
var element_wrap2 = document.getElementById('wrap2');
function foldDaumPostcode() {
    // iframe을 넣은 element를 안보이게 한다.
    element_wrap.style.display = 'none';
}
function foldDaumPostcode2() {
    // iframe을 넣은 element를 안보이게 한다.
    element_wrap2.style.display = 'none';
}

// 우편번호 찾기
function execDaumPostcode(){
	if(typeof daum === 'undefined'){
	    alert("다음 우편번호 postcode.v2.js 파일이 로드되지 않았습니다.");
	    return false;
	}
	
	// 현재 scroll 위치를 저장해놓는다.
    var currentScroll = Math.max(document.body.scrollTop, document.documentElement.scrollTop);
    new daum.Postcode({
        oncomplete: function(data) {
            // 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                // document.getElementById("sample3_extraAddress").value = extraAddr;
            
            } else {
                // document.getElementById("sample3_extraAddress").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('o_postNum').value = data.zonecode;
            document.getElementById("o_addr").value = addr;

            // iframe을 넣은 element를 안보이게 한다.
            // (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
            element_wrap.style.display = 'none';

            // 우편번호 찾기 화면이 보이기 이전으로 scroll 위치를 되돌린다.
            document.body.scrollTop = currentScroll;
        },
        // 우편번호 찾기 화면 크기가 조정되었을때 실행할 코드를 작성하는 부분. iframe을 넣은 element의 높이값을 조정한다.
        onresize : function(size) {
            element_wrap.style.height = size.height+'px';
        },
        width : '100%',
        height : '100%'
    }).embed(element_wrap);

    // iframe을 넣은 element를 보이게 한다.
    element_wrap.style.display = 'block';				
}
// 우편번호 찾기
function execDaumPostcode2(){
	if(typeof daum === 'undefined'){
	    alert("다음 우편번호 postcode.v2.js 파일이 로드되지 않았습니다.");
	    return false;
	}
	
	// 현재 scroll 위치를 저장해놓는다.
    var currentScroll = Math.max(document.body.scrollTop, document.documentElement.scrollTop);
    new daum.Postcode({
        oncomplete: function(data) {
            // 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                // document.getElementById("sample3_extraAddress").value = extraAddr;
            
            } else {
                // document.getElementById("sample3_extraAddress").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('r_postNum').value = data.zonecode;
            document.getElementById("r_addr").value = addr;

            // iframe을 넣은 element를 안보이게 한다.
            // (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
            element_wrap2.style.display = 'none';

            // 우편번호 찾기 화면이 보이기 이전으로 scroll 위치를 되돌린다.
            document.body.scrollTop = currentScroll;
        },
        // 우편번호 찾기 화면 크기가 조정되었을때 실행할 코드를 작성하는 부분. iframe을 넣은 element의 높이값을 조정한다.
        onresize : function(size) {
            element_wrap2.style.height = size.height+'px';
        },
        width : '100%',
        height : '100%'
    }).embed(element_wrap2);

    // iframe을 넣은 element를 보이게 한다.
    element_wrap2.style.display = 'block';				
}

//이메일 직접입력 셀렉트박스
function res(r){
	if(r ==""){
		$("#o_email2").attr("readonly",false);
	} else {
		$("#o_email2").attr("readonly",true);
	}
	document.getElementById("o_email2").value=r;

	var email1=document.getElementById("o_email").value;
	var email2=document.getElementById("o_email2").value;
	
	document.getElementById("od_email").value=email1+"@"+email2;
}
//주문자와 동일
$("#check1").click(function(){
	let checked = $("#check1").is(":checked");
	if(checked){
		$("#r_name").val($("#o_name").val());
		$("#r_tel").val($("#o_tel").val());
		$("#r_phone").val($("#o_phone").val());
		$("#r_postNum").val($("#o_postNum").val());
		$("#r_addr").val($("#o_addr").val());
		$("#r_addrDetail").val($("#o_addrDetail").val());
	}
});

//주문하기
fnSubmit = function(){
	const pattern = /^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$/;
	const scriptTag = /[~^&()|<>?]/;
	const emailPattern = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-za-z0-9\-]+/;
	
	let o_name		= $("#o_name").val();
	let o_pass		= $("#o_pass").val();
	let o_phone		= $("#o_phone").val();
	let o_postNum	= $("#o_postNum").val();
	let o_addr		= $("#o_addr").val();
	let o_addrDetail= $("#o_addrDetail").val();
	let o_email		= $("#o_email").val();
	let o_email2	= $("#o_email2").val();
	
	let delivKind	= $('input[name=delivKind]').is(":checked");
	let packaging	= $('input[name=packaging]').is(":checked");
	let delivTime	= $('input[name=delivTime]').is(":checked");
	let r_name		= $("#r_name").val();
	let r_phone		= $("#r_phone").val();
	let r_postNum	= $("#r_postNum").val();
	let r_addr		= $("#r_addr").val();
	let r_addrDetail= $("#r_addrDetail").val();
	
	if(o_name.replaceAll(" ", "") == ""){
		alert("주문하시는 분 이름을 입력해 주세요.");
		setTimeout( function(){ $("#o_name").focus(); }, 1 );
		return;
	} else if(!pattern.test(o_name) == true){
		alert("공백 혹은 특수문자가 입력되었습니다.");
		setTimeout( function(){ $("#o_name").focus(); }, 1 );
		return;
	}
	if(o_pass.replaceAll(" ", "") == ""){
		alert("비밀번호를 입력해 주세요.");
		setTimeout( function(){ $("#o_pass").focus(); }, 1 );
		return;
	} else if(!pattern.test(o_pass) == true){
		alert("공백 혹은 특수문자가 입력되었습니다.");
		setTimeout( function(){ $("#o_pass").focus(); }, 1 );
		return;
	} else if(o_pass.length < 3){
		alert("세 자리 이상 입력해 주세요.");
		setTimeout( function(){ $("#o_pass").focus(); }, 1 );
		return;
	}
	if(o_phone.replaceAll(" ", "") == ""){
		alert("핸드폰번호를 입력해 주세요.");
		setTimeout( function(){ $("#o_phone").focus(); }, 1 );
		return;
	}
	if(o_postNum.replaceAll(" ", "") == ""){
		alert("주소 검색을 해주세요.");
		setTimeout( function(){ $("#o_postNum").focus(); }, 1 );
		return;
	} else if(!pattern.test(o_postNum) == true){
		alert("공백 혹은 특수문자가 입력되었습니다.");
		setTimeout( function(){ $("#o_postNum").focus(); }, 1 );
		return;
	}
	if(o_addr.replaceAll(" ", "") == ""){
		alert("주소 검색을 해주세요.");
		setTimeout( function(){ $("#o_addr").focus(); }, 1 );
		return;
	}
	if(o_addrDetail.replaceAll(" ", "") == ""){
		alert("상세 주소를 입력해 주세요.");
		setTimeout( function(){ $("#o_addrDetail").focus(); }, 1 );
		return;
	}
	if(o_email.replaceAll(" ", "") == ""){
		alert("이메일을 입력해 주세요.");
		setTimeout( function(){ $("#o_email").focus(); }, 1 );
		return;
	} else if(o_email2.replaceAll(" ", "") == ""){
		alert("이메일을 입력해 주세요.");
		setTimeout( function(){ $("#o_email2").focus(); }, 1 );
		return;
	}
	
	$("#od_email").val(o_email+"@"+o_email2);
	
	if(r_name.replaceAll(" ", "") == ""){
		alert("받으시는 분 이름을 입력해 주세요.");
		setTimeout( function(){ $("#r_name").focus(); }, 1 );
		return;
	} else if(!pattern.test(r_name) == true){
		alert("공백 혹은 특수문자가 입력되었습니다.");
		setTimeout( function(){ $("#r_name").focus(); }, 1 );
		return;
	}
	if(r_phone.replaceAll(" ", "") == ""){
		alert("핸드폰번호를 입력해 주세요.");
		setTimeout( function(){ $("#r_phone").focus(); }, 1 );
		return;
	}
	if(r_postNum.replaceAll(" ", "") == ""){
		alert("주소 검색을 해주세요.");
		setTimeout( function(){ $("#r_postNum").focus(); }, 1 );
		return;
	} else if(!pattern.test(r_postNum) == true){
		alert("공백 혹은 특수문자가 입력되었습니다.");
		setTimeout( function(){ $("#r_postNum").focus(); }, 1 );
		return;
	}
	if(r_addr.replaceAll(" ", "") == ""){
		alert("주소 검색을 해주세요.");
		setTimeout( function(){ $("#r_addr").focus(); }, 1 );
		return;
	}
	if(r_addrDetail.replaceAll(" ", "") == ""){
		alert("상세 주소를 입력해 주세요.");
		setTimeout( function(){ $("#r_addrDetail").focus(); }, 1 );
		return;
	}
	if(!delivKind){
		alert("배송 유형을 선택해 주세요.");
		setTimeout( function(){ $("#radio1").focus(); }, 1 );
		return;
	}
	if(!packaging){
		alert("포장가방을 선택해 주세요.");
		setTimeout( function(){ $("#radio3").focus(); }, 1 );
		return;
	}
	if(!delivTime){
		alert("배달시간을 선택해 주세요.");
		setTimeout( function(){ $("#radio6").focus(); }, 1 );
		return;
	}
	if(!$("#agree1").is(":checked")){
		alert("결제정보 확인 및 구매진행에 동의하셔야 주문이 가능합니다.");
		return;
	}
	let orderVO = $("#orderForm").serializeObject();
	orderVO.orderNo = /*[[${orderNo}]]*/""; 
	
	$.ajax({
		url: "/rest/order/orderPayment",
		type: "POST",
		dataType: "json",
		data: 	orderVO,
		success: function (result) {
			if(result.responseCode == "SUCCESS"){
				if(result.data.resultCode == "SUCCESS"){
									
					var orderId = /*[[${orderNo}]]*/"";
					var amount = {
						currency: "KRW",
						value: result.data.TOTAL_AMOUNT,
					};
					// 비회원 결제
					// 스크립트 태그 연동방식
					let order = /*[[${order.productList}]]*/"";
					let orderSize = order.length;
					let orderName = "밥수니반찬세트";
					if(orderSize > 1){
						orderName = order[0].menuDay + " 외 " + Number(orderSize-1) +" 건"
					} else if(orderSize == 1){
						orderName = order[0].menuDay
					}
					payments(amount,orderId,orderName);
					$("#modalPay").trigger("click");
				} else {
					alert(result.data.resultMsg);
					if(result.data.returnUrl != ""){
						location.href = result.data.returnUrl;
					}
				}
			} else {
				alert("관리자에게 문의 부탁드립니다.");
	        }
		},
		error: function (e) {
			alert("장바구니 리스트 삭제에 실패했습니다.");
		}
	});
}