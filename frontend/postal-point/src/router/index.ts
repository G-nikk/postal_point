import { createRouter, createWebHistory } from 'vue-router'
import NewspapersView from '../views/Newspapers.vue' // Импортируем ваш компонент

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/newspapers', // URL путь
            name: 'newspapers', // Имя маршрута (для навигации)
            component: NewspapersView // Ваш компонент со страницей газет
        },
        // ... другие маршруты
    ]
})

export default router