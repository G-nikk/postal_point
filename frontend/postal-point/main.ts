import { createRouter, createWebHistory } from 'vue-router'
import NewspapersView from 'src/views/Newspapers.vue'
import app from "@/App.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/newspapers', component: NewspapersView }
    ]
})

app.use(router)