<template>
  <section class="card">
    <h2>Операции</h2>
    <div class="controls">
      <button @click="load">Обновить</button>
    </div>
    <table class="tbl">
      <thead>
        <tr>
          <th>ID</th>
          <th>Товар</th>
          <th>Тип</th>
          <th>Количество</th>
          <th>Дата и время</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="t in txns" :key="t.id">
          <td>{{ t.id }}</td>
          <td>{{ productName(t.productId) }}</td>
          <td>{{ t.type }}</td>
          <td>{{ t.quantity }}</td>
          <td>{{ t.createdAt ? t.createdAt : '' }}</td>
        </tr>
      </tbody>
    </table>

    <h3>Добавить</h3>
    <form @submit.prevent="create">
      <label>Товар:
        <select v-model.number="form.productId" required>
          <option v-for="p in products" :key="p.id" :value="p.id">
            {{ p.name }}
          </option>
        </select>
      </label>

      <label>Тип:
        <select v-model="form.type">
          <option>IN</option>
          <option>OUT</option>
        </select>
      </label>

      <label>Количество:
        <input v-model.number="form.quantity" type="number" min="1" required />
      </label>

      <button type="submit">Добавить</button>
      <span class="msg">{{ msg }}</span>
    </form>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const TXN_URL = 'http://localhost:8080/transactions'
const INV_URL = 'http://localhost:8080/inventory'

const txns = ref([])
const products = ref([])
const form = ref({ productId: null, type: 'IN', quantity: 1 })
const msg = ref('')

async function fetchProducts() {
  try {
    const res = await fetch(INV_URL)
    if (!res.ok) throw new Error('Не удалось загрузить товары (' + res.status + ')')
    products.value = await res.json()
    // если productId ещё не выбран, выставляем первый доступный
    if (!form.value.productId && products.value.length > 0) {
      form.value.productId = products.value[0].id
    }
  } catch (e) {
    console.error(e)
    msg.value = 'Ошибка загрузки товаров: ' + e.message
  }
}

async function load() {
  msg.value = 'Загрузка...'
  try {
    const res = await fetch(TXN_URL)
    if (!res.ok) throw new Error('HTTP ' + res.status)
    txns.value = await res.json()
    msg.value = ''
  } catch (e) {
    msg.value = 'Ошибка: ' + e.message
  }
}

function productName(id) {
  const p = products.value.find(x => x.id === id)
  return p ? p.name : `#${id}`
}

async function create() {
  msg.value = 'Отправка...'
  try {
    // валидация клиентская
    if (!form.value.productId) throw new Error('Выберите товар')
    if (!form.value.quantity || form.value.quantity <= 0) throw new Error('Неверное количество')

    const res = await fetch(TXN_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value)
    })

    if (!res.ok) {
      // пытаемся прочитать тело ошибки (если есть)
      let text = await res.text()
      throw new Error(res.status + ' ' + (text || 'Ошибка сервера'))
    }

    const created = await res.json()
    msg.value = 'Создано id=' + created.id
    await load()           // обновляем транзакции
    await fetchProducts()  // обновляем список товаров (на случай изменения остатков)
  } catch (e) {
    msg.value = 'Ошибка: ' + e.message
  }
}

onMounted(async () => {
  await fetchProducts()
  await load()
})
</script>

<style scoped>
.card{background:#fff;padding:12px;border-radius:8px;box-shadow:0 1px 3px rgba(0,0,0,.06)}
.controls{margin-bottom:8px}
.tbl{width:100%;border-collapse:collapse}
.tbl th,.tbl td{padding:6px 8px;border-bottom:1px solid #eee;text-align:left}
form label{display:inline-flex;gap:8px;align-items:center;margin-right:8px}
.msg{margin-left:12px;color:#666}
select, input { padding:6px 8px; border:1px solid #ddd; border-radius:4px }
button { background:#2b7a78; color:#fff; border:none; padding:6px 10px; border-radius:4px; cursor:pointer }
</style>

