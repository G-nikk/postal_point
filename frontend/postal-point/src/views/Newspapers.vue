<template>
  <div class="newspapers-page">
    <h1>Управление газетами</h1>

    <!-- Таблица газет -->
    <div v-if="loading">Загрузка...</div>
    <div v-else-if="error">{{ error }}</div>
    <div v-else>
      <button @click="showCreateModal = true">Добавить газету</button>

      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Название</th>
          <th>Индекс</th>
          <th>Редактор</th>
          <th>Цена</th>
          <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="newspaper in newspapers" :key="newspaper.newspaperID">
          <td>{{ newspaper.newspaperID }}</td>
          <td>{{ newspaper.name }}</td>
          <td>{{ newspaper.indexEdition }}</td>
          <td>{{ newspaper.editor }}</td>
          <td>{{ newspaper.price }} ₽</td>
          <td>
            <button @click="openEditModal(newspaper)">✏️</button>
            <button @click="deleteNewspaper(newspaper.newspaperID)">🗑️</button>
            <button @click="showPrintingHouses(newspaper.newspaperID)">🏭</button>
            <button @click="showTotalCost(newspaper.newspaperID)">💰</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Модальное окно создания/редактирования -->
    <div v-if="showCreateModal || showEditModal" class="modal">
      <h2>{{ showEditModal ? 'Редактировать' : 'Добавить' }} газету</h2>
      <form @submit.prevent="submitForm">
        <label>Название: <input v-model="form.name" required /></label>
        <label>Индекс: <input v-model="form.indexEdition" required /></label>
        <label>Редактор: <input v-model="form.editor" required /></label>
        <label>Цена: <input v-model="form.price" type="number" step="0.01" required /></label>
        <button type="submit">Сохранить</button>
        <button type="button" @click="closeModal">Отмена</button>
      </form>
    </div>

    <!-- Модальное окно с типографиями -->
    <div v-if="printingHousesModal" class="modal">
      <h2>Типографии для газеты</h2>
      <ul>
        <li v-for="ph in printingHouses" :key="ph.printingHouseID">
          {{ ph.name }} ({{ ph.address }})
        </li>
      </ul>
      <button @click="printingHousesModal = false">Закрыть</button>
    </div>

    <!-- Модальное окно с общей стоимостью -->
    <div v-if="totalCostModal" class="modal">
      <h2>Общая стоимость тиража</h2>
      <p>Стоимость: {{ totalCost }} ₽</p>
      <button @click="totalCostModal = false">Закрыть</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
//@ts-ignore
import { useNewspaperStore } from '../stores/newspaper_store';

const newspaperStore = useNewspaperStore();
const { newspapers, loading, error, fetchNewspapers, createNewspaper, updateNewspaper, deleteNewspaper: deleteNewspaperStore, getPrintingHouses, getTotalCost } = newspaperStore;

// Состояние модальных окон
const showCreateModal = ref(false);
const showEditModal = ref(false);
const printingHousesModal = ref(false);
const totalCostModal = ref(false);

// Данные формы
const form = ref({
  newspaperID: null as number | null,
  name: '',
  indexEdition: '',
  editor: '',
  price: 0,
});

// Доп. данные
const printingHouses = ref([] as any[]);
const totalCost = ref(0);

// Загружаем газеты при монтировании
onMounted(() => {
  fetchNewspapers();
});

// Открытие формы редактирования
const openEditModal = (newspaper: any) => {
  form.value = { ...newspaper };
  showEditModal.value = true;
};

// Удаление газеты
const deleteNewspaper = async (id: number) => {
  if (confirm('Удалить газету?')) {
    await deleteNewspaperStore(id);
  }
};

// Отправка формы
const submitForm = async () => {
  if (form.value.newspaperID) {
    await updateNewspaper(form.value.newspaperID, form.value);
  } else {
    await createNewspaper(form.value);
  }
  closeModal();
};

// Закрытие модального окна
const closeModal = () => {
  showCreateModal.value = false;
  showEditModal.value = false;
  form.value = { newspaperID: null, name: '', indexEdition: '', editor: '', price: 0 };
};

// Показать типографии
const showPrintingHouses = async (newspaperId: number) => {
  printingHouses.value = await getPrintingHouses(newspaperId);
  printingHousesModal.value = true;
};

// Показать общую стоимость
const showTotalCost = async (newspaperId: number) => {
  totalCost.value = await getTotalCost(newspaperId);
  totalCostModal.value = true;
};
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
}
th, td {
  border: 1px solid #ddd;
  padding: 8px;
}
.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 20px;
  border: 1px solid #ccc;
}
</style>