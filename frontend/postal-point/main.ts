import { createRouter, createWebHistory } from 'vue-router'
import NewspapersView from '@/views/Newspapers.vue'
import { createApp } from 'vue';
import App from '@/App.vue';

const app = createApp(App);
const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/newspapers', component: NewspapersView }
    ]
})

app.use(router)