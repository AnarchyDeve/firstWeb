const API_BASE_URL = "http://localhost:8080/api";

const api = {
    async request(path, options = {}) {
        const token = localStorage.getItem("accessToken");
        const headers = { 'Content-Type': 'application/json' };
        if (token) headers['Authorization'] = `Bearer ${token}`;

        const response = await fetch(`${API_BASE_URL}${path}`, { ...options, headers });

        if (response.status === 204 || response.headers.get("content-length") === "0") return null;

        // 응답 타입 판별 (텍스트/JSON 모두 수용)
        const contentType = response.headers.get("content-type");
        let data;
        if (contentType && contentType.includes("application/json")) {
            data = await response.json();
        } else {
            data = await response.text();
        }

        if (!response.ok) {
            const errorMsg = (typeof data === 'object') ? (data.message || "요청 실패") : data;
            throw new Error(errorMsg);
        }
        return data;
    },

    join(email, password, name) {
        return this.request("/members/join", {
            method: 'POST',
            body: JSON.stringify({ email, password, name })
        });
    },

    async login(email, password) {
        const data = await this.request("/members/login", {
            method: 'POST',
            body: JSON.stringify({ email, password })
        });
        if (data.accessToken) localStorage.setItem("accessToken", data.accessToken);
        return data;
    },

    getCars() { return this.request("/cars"); },

    reserve(carId, startTime, endTime) {
        return this.request("/reservations", {
            method: 'POST',
            body: JSON.stringify({ carId, startTime, endTime })
        });
    },

    getMyReservations() { return this.request("/reservations/me"); },

    cancelReservation(reservationId) {
        return this.request(`/reservations/${reservationId}`, { method: 'DELETE' });
    }
};