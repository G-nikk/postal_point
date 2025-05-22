import { defineStore } from 'pinia';
import { NewspaperApi } from '../api/newspaper_api.js';

export const useNewspaperStore = defineStore('newspaper', {
    state: () => ({
        newspapers: [] as any[],
        loading: false,
        error: null as string | null,
    }),

    actions: {
        async fetchNewspapers() {
            this.loading = true;
            try {
                this.newspapers = await NewspaperApi.getAll();
            } catch (error) {
                this.error = 'Ошибка загрузки газет';
            } finally {
                this.loading = false;
            }
        },

        async createNewspaper(newspaper: any) {
            try {
                await NewspaperApi.create(newspaper);
                await this.fetchNewspapers(); // Обновляем список
            } catch (error) {
                this.error = 'Ошибка создания газеты';
            }
        },

        async updateNewspaper(id: number, newspaper: any) {
            try {
                await NewspaperApi.update(id, newspaper);
                await this.fetchNewspapers();
            } catch (error) {
                this.error = 'Ошибка обновления газеты';
            }
        },

        async deleteNewspaper(id: number) {
            try {
                await NewspaperApi.delete(id);
                await this.fetchNewspapers();
            } catch (error) {
                this.error = 'Ошибка удаления газеты';
            }
        },

        async getPrintingHouses(newspaperId: number) {
            return await NewspaperApi.getPrintingHouses(newspaperId);
        },

        async getTotalCost(newspaperId: number) {
            return await NewspaperApi.getTotalCost(newspaperId);
        },
    },
});