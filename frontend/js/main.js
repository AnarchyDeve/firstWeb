document.addEventListener('DOMContentLoaded', () => {
    if (localStorage.getItem("accessToken")) renderAll();
});

async function renderAll() {
    await renderCarList();
    await renderMyReservations();
}

// 회원가입
async function handleJoin() {
    const email = document.getElementById('joinEmail').value;
    const password = document.getElementById('joinPassword').value;
    const name = document.getElementById('joinName').value;
    try {
        const msg = await api.join(email, password, name);
        alert(msg); // "회원가입 성공!" 알림
    } catch (e) { alert(e.message); }
}

// 로그인
async function handleLogin() {
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;
    try {
        await api.login(email, password);
        alert("로그인 성공!");
        renderAll();
    } catch (e) { alert("로그인 실패: " + e.message); }
}

// 차량 목록 출력
async function renderCarList() {
    try {
        const cars = await api.getCars();
        const carListEl = document.getElementById('carList');
        carListEl.innerHTML = cars.map(car => `
            <div class="car-card">
                <h3>${car.modelName}</h3>
                <p>번호: ${car.carNumber}</p>
                <p><strong>${car.pricePerDay.toLocaleString()}원</strong> / 일</p>
                <button class="btn-reserve" onclick="handleReserve(${car.id})" ${!car.isAvailable ? 'disabled' : ''}>
                    ${car.isAvailable ? '예약하기' : '대여중'}
                </button>
            </div>
        `).join('');
    } catch (e) { console.error(e); }
}

// 예약하기
async function handleReserve(carId) {
    try {
        await api.reserve(carId, "2025-12-25T10:00:00", "2025-12-26T10:00:00");
        alert("예약 완료!");
        renderAll();
    } catch (e) { alert(e.message); }
}
async function renderMyReservations() {
    try {
        console.log("내 예약 내역 불러오는 중...");
        const res = await api.getMyReservations();
        console.log("서버에서 받은 데이터:", res);

        const resListEl = document.getElementById('reservationList');

        if (!res || res.length === 0) {
            resListEl.innerHTML = "<div class='no-data'>아직 예약한 내역이 없습니다.</div>";
            return;
        }

        resListEl.innerHTML = res.map(r => {
            // 1. 날짜 포맷 안전하게 처리 (배열이든 문자열이든 대응)
            const formatDate = (dateStr) => {
                if (!dateStr) return "날짜 없음";
                const s = String(dateStr);
                return s.includes('T') ? s.split('T')[0] : s;
            };

            // 2. 보내주신 DTO의 필드명인 'reservationId' 사용
            const rId = r.reservationId;

            return `
                <div class="res-card" style="border:1px solid #ddd; padding:15px; margin-bottom:10px; border-radius:8px; background:#fff;">
                    <div style="display:flex; justify-content:space-between; align-items:center;">
                        <div>
                            <h4 style="margin:0; color:var(--primary);">예약 번호: ${rId}</h4>
                            <p style="margin:5px 0; font-size:14px; color:#333;">
                                <strong>${r.modelName}</strong> (${r.carNumber})
                            </p>
                            <p style="margin:0; font-size:13px; color:#666;">
                                기간: ${formatDate(r.startTime)} ~ ${formatDate(r.endTime)}
                            </p>
                        </div>
                        <button class="btn-cancel" 
                                onclick="handleCancel(${rId})" 
                                style="background:#ff4757; color:white; border:none; padding:8px 15px; border-radius:5px; cursor:pointer;">
                            취소
                        </button>
                    </div>
                </div>
            `;
        }).join('');

    } catch (e) {
        console.error("내역 로드 실패:", e);
        document.getElementById('reservationList').innerHTML = "<p>내역을 불러오는 중 에러가 발생했습니다.</p>";
    }
}
// 취소하기
async function handleCancel(resId) {
    if (!confirm("취소하시겠습니까?")) return;
    try {
        await api.cancelReservation(resId);
        alert("취소되었습니다.");
        renderAll();
    } catch (e) { alert(e.message); }
}