import axios from 'axios';

const API = axios.create({
    baseURL: 'http://localhost:8080',
});

export const postOfficeAPI = {
    getAll: () => API.get('/post-offices'),
    getById: (id) => API.get(`/post-offices/${id}`),
    create: (data) => API.post('/post-offices', data),
    update: (id, data) => API.put(`/post-offices/${id}`, data),
    delete: (id) => API.delete(`/post-offices/${id}`),
    getNewspapers: (id) => API.get(`/post-offices/${id}/newspapers`),
    getPrintHouses: (id) => API.get(`/post-offices/${id}/printing-houses`),
    getMaxCost: () => API.get('/post-offices/max-cost'),
    getMostReceived: () => API.get('/post-offices/most-received')
};

export const printingHouseAPI = {
    getAll: () => API.get('/printing-houses'),
    getById: (id) => API.get(`/printing-houses/${id}`),
    create: (data) => API.post('/printing-houses', data),
    update: (id, data) => API.put(`/printing-houses/${id}`, data),
    delete: (id) => API.delete(`/printing-houses/${id}`),
    getNewspapers: (id) => API.get(`/printing-houses/${id}/newspapers`),
    getMaxEditor: (id) => API.get(`/printing-houses/${id}/max-editor`)
};

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