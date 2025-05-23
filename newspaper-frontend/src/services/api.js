import axios from 'axios';

const API = axios.create({
    baseURL: 'http://localhost:8080',
});

export const newspaperAPI = {
    // Основные CRUD операции
    getAll: () => API.get('/newspapers'),
    getById: (id) => API.get(`/newspapers/${id}`),
    create: (data) => API.post('/newspapers', data),
    update: (id, data) => API.put(`/newspapers/${id}`, data),
    delete: (id) => API.delete(`/newspapers/${id}`),

    // Специальные запросы
    getPrintHouses: (id) => API.get(`/newspapers/${id}/printing-houses`),
    getEditorInfo: (newspaperId, printingHouseId) =>
        API.get(`/newspapers/editor?printingHouseId=${printingHouseId}&newspaperId=${newspaperId}`),
    getTotalCost: (id) => API.get(`/newspapers/total-cost?newspaperId=${id}`),

    // Новые методы для почтовых отделений
    getPostOffices: (newspaperId, printingHouseId) =>
        API.get(`/post-offices?newspaperId=${newspaperId}&printingHouseId=${printingHouseId}`),
    getMaxDelivery: () => API.get('/post-offices/max-delivery'),
    getMaxCost: () => API.get('/post-offices/max-cost')
};