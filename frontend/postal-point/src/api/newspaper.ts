import axios from 'axios';

const API_URL = 'http://localhost:8080/newspapers';

export const NewspaperApi = {
    // Получить все газеты
    async getAll() {
        const response = await axios.get(API_URL);
        return response.data;
    },

    // Получить газету по ID
    async getById(id: number) {
        const response = await axios.get(`${API_URL}/${id}`);
        return response.data;
    },

    // Создать газету
    async create(newspaper: any) {
        const response = await axios.post(API_URL, newspaper);
        return response.data;
    },

    // Обновить газету
    async update(id: number, newspaper: any) {
        const response = await axios.put(`${API_URL}/${id}`, newspaper);
        return response.data;
    },

    // Удалить газету
    async delete(id: number) {
        await axios.delete(`${API_URL}/${id}`);
    },

    // Доп. методы из вашего бэкенда
    async getPrintingHouses(newspaperId: number) {
        const response = await axios.get(`${API_URL}/${newspaperId}/printing-houses`);
        return response.data;
    },

    async getTotalCost(newspaperId: number) {
        const response = await axios.get(`${API_URL}/total-cost?newspaperId=${newspaperId}`);
        return response.data;
    },
};