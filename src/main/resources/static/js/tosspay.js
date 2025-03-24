const clientKey = "test_gck_ZLKGPx4M3MNEZm1G79g7VBaWypv1";
const customerKey = generateRandomString();
const tossPayments = TossPayments(clientKey);
/* 회원 결제
// @docs https://docs.tosspayments.com/sdk/v2/js#tosspaymentswidgets
const widgets = tossPayments.widgets({
	customerKey,
});
*/
const widgets = tossPayments.widgets({customerKey: TossPayments.ANONYMOUS});

function generateRandomString() {
	return window.btoa(Math.random()).slice(0, 20);
}