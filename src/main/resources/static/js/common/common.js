function getToday() {
	const d = new Date();
	d.setHours(0, 0, 0, 0); // 시간 제거 (중요)
	return d;
}

function addDays(date, days) {
	const d = new Date(date);
	d.setDate(d.getDate() + days);
	return d;
}

function addMonths(date, months) {
	const d = new Date(date);
	d.setMonth(d.getMonth() + months);
	return d;
}